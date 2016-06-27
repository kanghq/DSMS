package com.hqkang.DSMS.test;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.text.DecimalFormat;

import com.hqkang.DSMS.Client.ManagerClient;

public class ClientThread implements Runnable {
	private int c = 0;
	private String location;
	private ManagerClient user1;
	ClientThread(String adName) {
		try {
			user1 = new ManagerClient(adName);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		c++;
		while (c <= 50) {
			
			try {
				
				String id = user1.addDR("FN"+c, "LN"+c, "Montreal", "51422234"+c, "Surgery", location);
				user1.editRecord(id, "firstName", (new BigInteger(130, (new SecureRandom()))).toString(32));
				user1.addNR("FN"+c, "LN"+c, "junior", "active", "2016-01-01");
				c++;
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		try {
			System.out.println(user1.getRecordCounts());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	

}
