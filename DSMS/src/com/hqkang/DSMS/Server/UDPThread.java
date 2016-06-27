package com.hqkang.DSMS.Server;

import java.io.IOException;
import java.io.PipedInputStream;
import java.net.SocketException;

public class UDPThread implements Runnable {
	private UDPServer uSrv;
	private boolean status = true;
	private String message;
	private ServerInterface srv;
	public UDPThread(String ip, int port) {
		
		try {
			uSrv = new UDPServer(ip, port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	public void bindManagerServer(ServerInterface srv) {
		this.srv = srv;
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(status) {
			try {
				message = uSrv.Receive();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(message.equals("SHUTDOWN")) status = false;
			if(message.equals("COUNT"))
				try {
					uSrv.Send(srv.getRecordCounts("REMOTE SRV"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		
	}

}
