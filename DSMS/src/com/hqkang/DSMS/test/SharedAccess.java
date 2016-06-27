package com.hqkang.DSMS.test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.hqkang.DSMS.Client.ManagerClient;

public class SharedAccess implements Runnable {
	String val;
	SharedAccess(String value) {
		val = value;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ManagerClient user1 = new ManagerClient("mtl0001");
			user1.editRecord("DR00001", "lastName", val);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
