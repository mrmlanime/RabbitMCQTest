package org.yanixmrml.services;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.yanixmrml.dao.DataDAO;
import org.yanixmrml.model.Data;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class RecvCLI {
	
	private final static String QUEUE_NAME = "test1";
	private DataDAO dataDAO;
	private double inputNumber;
	private String host;
	
	public RecvCLI() {
		this.host = "localhost";
		dataDAO = new DataDAO();
	}
	
	public void init() throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(host);
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		System.out.println(" [*] Waiting for messages: To exit press CTRL+C" );
	
		DeliverCallback deliverCallback = (consumerTag , delivery)->{
			try {
				String message = new String(delivery.getBody(),"UTF-8");
				inputNumber = Double.parseDouble(message);
				dataDAO.saveData(new Data(Math.pow(inputNumber, 2)));
				System.out.println("[x] Received '" + message + "', Double input is " + Math.pow(inputNumber, 2) + ", Checked DB");
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		};	
		channel.basicConsume(QUEUE_NAME, true,deliverCallback,consumerTag->{});
	}

	public Data getLatestData() {
		List<Data> dataList = dataDAO.getData();
		return dataList.get(dataList.size()-1);
	}
}
