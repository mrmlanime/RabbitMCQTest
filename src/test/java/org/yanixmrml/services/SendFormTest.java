package org.yanixmrml.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.junit.jupiter.api.Test;
import org.yanixmrml.model.Data;

class SendFormTest {

	@Test
	void test() throws Exception {
		SendCLI send = new SendCLI();
		RecvCLI recv = new RecvCLI();
		recv.init();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String cont = "Y";
		while(cont.equalsIgnoreCase("y")) {
			System.out.println("Enter the number");
			String input = reader.readLine();
			send.inputData(input);
			send.sendData();
			Thread.sleep(1500); //Wait for the receiver to received and save to DB;
			double number = Double.parseDouble(input);
			Data data = recv.getLatestData();
			System.out.println("Data in DB : " + data.getNumber());
			assertEquals(Math.pow(number, 2),data.getNumber(),"Test Failed");
			System.out.println("Continue? ");
			cont = reader.readLine();
		}
		reader.close();
	}

}
