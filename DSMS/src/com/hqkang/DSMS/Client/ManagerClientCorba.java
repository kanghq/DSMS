package com.hqkang.DSMS.Client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.omg.CosNaming.NamingContext;

import com.hqkang.DSMS.Server.ServerInterface;

public class ManagerClientCorba extends ManagerClient {
	

	public ManagerClientCorba(String managerID, NamingContext ncRef) throws MalformedURLException, RemoteException, NotBoundException {
		//super(managerID);
		// TODO Auto-generated constructor stub
		
			
		writeLog(managerID, "Logged in");
		ServerInterface tempSrv =  new ClientAdapter("rmi://localhost:2020/srv", ncRef);
		srvURLMap = tempSrv.getSrvURLList();
		srvUDPMap = tempSrv.getSrvUDPList();
		Iterator<Entry<String,String>> ite = srvURLMap.entrySet().iterator();
			
			
		while(ite.hasNext()) {
			Entry<String, String> e = ite.next();
			String srvURL = e.getValue();
			ServerInterface srv = (ServerInterface) new ClientAdapter(srvURL, ncRef);
			srvMap.put(e.getKey(), srv);
		}
			login(managerID);
			
	}

}
