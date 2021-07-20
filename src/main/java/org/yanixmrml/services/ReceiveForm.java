package org.yanixmrml.services;

import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.yanixmrml.dao.DataDAO;
import org.yanixmrml.model.Data;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveForm {
	
	private final String QUEUE_NAME = "rabbitmcqtest";
	private final String host = "localhost";
	private DataDAO dataDAO;
	private JFrame mainFrame;
	private JTextArea textArea;
	
	public ReceiveForm() {
		dataDAO = new DataDAO();
		init();
	}

	private void init() {
		//UI
		mainFrame = new JFrame();
		JPanel panel = new JPanel();
		panel.setSize(new Dimension(400, 300));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		textArea = new JTextArea(20,20);
		textArea.setEditable(false);
		textArea.setFont(new Font("Arial", Font.BOLD, 12));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setSize(new Dimension(400,300));
		//scrollPane.add(textArea);
		panel.add(textArea);
		mainFrame.add(panel);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainFrame.setPreferredSize(new Dimension(450, 350));
	    mainFrame.pack();
		mainFrame.setVisible(true);
		
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(host);
		Connection connection;
		try {
			connection = connectionFactory.newConnection();
			Channel channel = connection.createChannel();
			
			channel.queueDeclare(QUEUE_NAME,false,false,false,null);
			System.out.println(" [*] Waiting for messages: To exit press CTRL+C" );
		
			DeliverCallback deliverCallback = (consumerTag , delivery)->{
				String message = new String(delivery.getBody(),"UTF-8");
				System.out.println("[x] Received '" + message + "'");
				try {
					double number = Double.parseDouble(message);
					dataDAO.saveData(new Data(Math.pow(number, 2)));
					textArea.setText(textArea.getText() + "\n[x] Received '" + message + "',Double Received Number is : " + (Math.pow(number, 2)) +  ", Checked data_db.");
				}catch(Exception e) {
					System.out.println(e.getMessage());
					textArea.setText(textArea.getText() + "\n" + e.getMessage());
				}
			};	
			channel.basicConsume(QUEUE_NAME, true,deliverCallback,consumerTag->{});
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]) {
		ReceiveForm receiveForm = new ReceiveForm();
		System.out.println("Running " + receiveForm.getClass());
	}
}
