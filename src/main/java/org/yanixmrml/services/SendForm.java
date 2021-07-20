package org.yanixmrml.services;

import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SendForm {
	
	private final String QUEUE_NAME = "rabbitmcqtest";
	private final String host = "localhost";
	private JFrame mainFrame;
	private JTextField textField;
	private JButton submitButton;
	
	public SendForm() {
		init();
	}
	
	public void init() {
		mainFrame = new JFrame();
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Enter Number");
		panel.add(label);
		textField = new JTextField();
		textField.setColumns(20);
		textField.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(textField);
		submitButton = new JButton("Submit");
		panel.add(submitButton);
		mainFrame.add(panel);
		submitButton.addActionListener((actionEvent)->{
			submitData();	
		});
		mainFrame.add(panel);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainFrame.setPreferredSize(new Dimension(450, 150));
	    mainFrame.pack();
		mainFrame.setVisible(true);
	}
	
	public void submitData() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		try(Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()){
			channel.queueDeclare(QUEUE_NAME,false,false,false,null);

			int number = 0;
			try {
				number = Integer.parseInt(textField.getText());
				String message = Integer.toString(number);
				channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
				System.out.println("[x] Sent '" + message + "'");
				JOptionPane.showMessageDialog(mainFrame, message, "Input Message",JOptionPane.INFORMATION_MESSAGE);
			}catch(Exception e) {
				System.out.println(e.getMessage());
				JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error Message",JOptionPane.ERROR_MESSAGE);
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void showDialog(String message) {
		
	}
	
	public static void main(String args[]) {
		SendForm sendForm = new SendForm();
		System.out.println("Running " + sendForm.getClass());
	}
}
