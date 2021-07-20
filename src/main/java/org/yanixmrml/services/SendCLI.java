package org.yanixmrml.services;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SendCLI {
	
	private final static String QUEUE_NAME = "test1";
	private double inputNumber;
	private String host;
	
	public SendCLI() {
		this.host = "localhost";
	}
	
	public void inputData(String input) throws Exception {
		this.inputNumber = Double.parseDouble(input);
	}
	
	public void sendData() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		try(Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()){
			channel.queueDeclare(QUEUE_NAME,false,false,false,null);
			String message = Double.toString(inputNumber);
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println("[x] Sent '" + message + "'");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
