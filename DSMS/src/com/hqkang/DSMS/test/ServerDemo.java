package com.hqkang.DSMS.test;

import com.hqkang.DSMS.Client.ManagerClient;
import com.hqkang.DSMS.Server.ClinicServer;
import com.hqkang.DSMS.Server.UDPThread;

public class ServerDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
		ClinicServer serverMTL = new ClinicServer("mtl");
		serverMTL.exportServer(2020);
		UDPThread uSrvMTL = new UDPThread("localhost", 3020);
		uSrvMTL.bindManagerServer(serverMTL);
		ClinicServer serverLVL = new ClinicServer("lvl");
		serverLVL.exportServer(2021);
		UDPThread uSrvLVL = new UDPThread("localhost", 3021);
		uSrvLVL.bindManagerServer(serverLVL);
		ClinicServer serverDDO = new ClinicServer("ddo");
		serverDDO.exportServer(2022); 
		UDPThread uSrvDDO = new UDPThread("localhost", 3022);
		uSrvDDO.bindManagerServer(serverDDO);
		
		serverMTL.connect();
		serverDDO.connect();
		serverLVL.connect();
		new Thread(uSrvMTL).start();
		new Thread(uSrvDDO).start();
		new Thread(uSrvLVL).start();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	

	}

}
