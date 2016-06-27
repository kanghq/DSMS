package com.hqkang.DSMS.test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.hqkang.DSMS.Client.ManagerClient;

public class ClientDemo {
	public static void main(String[] args) throws Exception {
		ManagerClient user1 = new ManagerClient("mtl0001");
		user1.addDR("FN1", "LN1", "Montreal", "514222343", "Surgery", "mtl");
		user1.addDR("FN2", "LN2", "Montreal", "514222343", "Surgery", "lvl");
		user1.addDR("FN3", "LN3", "Montreal", "514222343", "Surgery", "mtl");
		user1.addNR("FN1", "LN1", "junior", "active", "2015-01-01 12:00:31");
		user1.editRecord("DR00001", "lastName", "LASTNAME");
		System.out.println(user1.getRecordCounts());
		ClientThread th = new ClientThread("mtl0002"); //administrator 1, location mtl
		(new Thread(th)).start();
		(new Thread(th)).start();
		(new Thread(th)).start();
		ClientThread thlvl = new ClientThread("lvl0003"); //admin 2 location lvl
		(new Thread(thlvl)).start();
		(new Thread(thlvl)).start();
		(new Thread(thlvl)).start();
		SharedAccess sa = new SharedAccess("firstMod");
		SharedAccess sb = new SharedAccess("secondMod");
		
		(new Thread(sa)).start();
		(new Thread(sb)).start();
		
		
	}

}
