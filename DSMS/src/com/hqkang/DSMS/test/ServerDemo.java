package com.hqkang.DSMS.test;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import com.hqkang.DSMS.Server.ClinicServer;
import com.hqkang.DSMS.Server.ClinicServletC;
import com.hqkang.DSMS.Server.UDPThread;


public class ServerDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			
			
			
			
			
			
			
			ORB orb = ORB.init(args, null);
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		     rootpoa.the_POAManager().activate();
		     
			
			
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			/*
			java.lang.Object sync = new java.lang.Object();
			synchronized(sync) {
				sync.wait();
			}*/
			 
			 
			ClinicServer serverMTL = new ClinicServer("mtl");
			serverMTL.exportServer(2020);
			ClinicServletC CserverMTL = new ClinicServletC(serverMTL);
			UDPThread uSrvMTL = new UDPThread("localhost", 3020);
			uSrvMTL.bindManagerServer(serverMTL);
			CserverMTL.export(rootpoa, ncRef);
			
			ClinicServer serverLVL = new ClinicServer("lvl");
			ClinicServletC CserverLVL = new ClinicServletC(serverLVL);
			serverLVL.exportServer(2021);
			UDPThread uSrvLVL = new UDPThread("localhost", 3021);
			uSrvLVL.bindManagerServer(serverLVL);
			CserverLVL.export(rootpoa, ncRef);
			
			ClinicServer serverDDO = new ClinicServer("ddo");
			ClinicServletC CserverDDO = new ClinicServletC(serverDDO);
			serverDDO.exportServer(2022); 
			UDPThread uSrvDDO = new UDPThread("localhost", 3022);
			uSrvDDO.bindManagerServer(serverDDO);
			CserverDDO.export(rootpoa, ncRef);
			
			serverMTL.connect();
			serverDDO.connect();
			serverLVL.connect();
			new Thread(uSrvMTL).start();
			new Thread(uSrvDDO).start();
			new Thread(uSrvLVL).start();
			orb.run();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	

	}

}
