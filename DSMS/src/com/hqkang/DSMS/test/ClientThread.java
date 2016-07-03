package com.hqkang.DSMS.test;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.hqkang.DSMS.Client.ManagerClient;

public class ClientThread implements Runnable {
	private int c = 0;
	private String location;
	private ManagerClient user1;
	private String admin;
	private Random random = new Random();
	private String[] locArr = null; 

	ClientThread(String adName) {
		Set<String> locSet = new HashSet<String>();
		locSet.add("lvl");
		locSet.add("ddo");
		locSet.add("mtl");
		
		try {
			user1 = new ManagerClient(adName);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		admin = adName;
		switch(adName.substring(0, 3)) {
		case "mtl": location = "mtl";
		break;
		case "lvl": location = "lvl";
		break;
		case "ddo": location = "ddo";
		break;
		}
		locSet.remove(location);
		locArr = locSet.toArray(new String[locSet.size()]);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		c++;
		while (c <= 50) {
			
			try {
				int t = (random.nextInt(2));
				String remoteAdd = locArr[t];
				
				String id = user1.addDR("FN"+c, "LN"+c, "Montreal", "51422234"+c, "Surgery", location);
				user1.editRecord(id, "firstName", (new BigInteger(130, (new SecureRandom()))).toString(32));
				user1.transferRecord(id, remoteAdd, admin);
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
