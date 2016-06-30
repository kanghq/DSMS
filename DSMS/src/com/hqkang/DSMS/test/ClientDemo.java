package com.hqkang.DSMS.test;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import com.hqkang.DSMS.Client.ManagerClient;
import com.hqkang.DSMS.Client.ManagerClientCorba;

public class ClientDemo {
	public static void main(String[] args) throws Exception {
		ORB orb = ORB.init(args, null);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContext ncRef = NamingContextHelper.narrow(objRef);
		ManagerClient user1 = new ManagerClientCorba("mtl0001", ncRef);
		
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
