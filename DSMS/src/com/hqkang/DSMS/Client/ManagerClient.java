package com.hqkang.DSMS.Client;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import com.hqkang.DSMS.DoctorRecord;
import com.hqkang.DSMS.NurseRecord;
import com.hqkang.DSMS.NurseRecord.Designation;
import com.hqkang.DSMS.NurseRecord.Status;
import com.hqkang.DSMS.Record;
import com.hqkang.DSMS.createRes;
import com.hqkang.DSMS.DoctorRecord.Location;
import com.hqkang.DSMS.Server.ServerInterface;

public class ManagerClient {
	String managerID;
	HashMap<String, String> srvURLMap ;
	HashMap<String, String> srvUDPMap ;
	Map<String, ServerInterface> srvMap = new HashMap<String, ServerInterface>();
	ServerInterface defSrv = null; //default server to get info
	
	public ManagerClient() {}
	
	public ManagerClient(String managerID) throws MalformedURLException, RemoteException, NotBoundException {
		System.setSecurityManager(new RMISecurityManager());
		
		writeLog(managerID, "Logged in");
		ServerInterface tempSrv =  (ServerInterface) Naming.lookup("rmi://localhost:2020/srv");
		srvURLMap = tempSrv.getSrvURLList();
		srvUDPMap = tempSrv.getSrvUDPList();
		Iterator<Entry<String,String>> ite = srvURLMap.entrySet().iterator();
		
		
		while(ite.hasNext()) {
			Entry<String, String> e = ite.next();
			String srvURL = e.getValue();
			ServerInterface srv = (ServerInterface) Naming.lookup(srvURL);
			srvMap.put(e.getKey(), srv);
		}
		login(managerID);
	}
	
	protected boolean writeLog(String managerID, String content) {
		File file = new File(managerID);
		FileOutputStream outStream = null;
		try {
			if (!file.exists()) {
				
					file.createNewFile();
				
			}
		outStream = new FileOutputStream(file, true);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		byte[] contentInBytes = (df.format(new Date()) + " "+ managerID + ":" + content+"\n\r").getBytes();
		 
		outStream.write(contentInBytes);
		outStream.flush();
		outStream.close();
		return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
	}
	
	void login(String mID) {
		
		managerID = mID;
		defSrv = srvMap.get(mID.substring(0, 3));
		//setManagerID
		
	}
	
	public String addDR(String firstName, String lastName, String address, String phone, String specialization, String location) throws RemoteException {
		
	
		Location loc = null;
		try {
		loc = Location.valueOf(location);
		} catch (IllegalArgumentException e) {
			System.out.println("DR location wrong type");
		}
		
		DoctorRecord dr = new DoctorRecord(null, firstName, lastName, address, phone, specialization, loc);
		dr.recordID = defSrv.createRecord(dr, managerID).getRID();
		writeLog(managerID, "added a DR:" + dr.toString());
		return dr.recordID;
		
		/**
		 * firstName
		 * LastName
		 */
		 
	}
	
	public void addNR(String firstName, String lastName, String des, String stat,String statDate) throws RemoteException {
		Status status = null;
		Designation designation = null;
		Date statusDate = null;
		try {
		
		status = Status.valueOf(stat);
		
		designation = Designation.valueOf(des);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		
			statusDate = sdf.parse(statDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		NurseRecord nr = new NurseRecord(null, firstName, lastName, designation, status, statusDate);
		nr.recordID = defSrv.createRecord(nr, managerID).getRID();
		
		
		writeLog(managerID, "added a NR:" + nr.toString());
		
		
	}
	
	
	public boolean editRecord(String RID, String fieldName, String newValue) {
		boolean exists = false;


		
		if(false == exists) {
			return false;
		}
		try {
			boolean result = defSrv.editRecord(RID, fieldName, newValue, managerID);
			writeLog(managerID, "edited a record :" + RID +" "+ fieldName +" " + newValue + " " + result);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
		
		
	}
	
	public String getRecordCounts() throws Exception {
		UDPClient clt = null;
		String output = "";
		try {
			clt = new UDPClient();
		
			Iterator<Entry<String, String>> iteRead = srvUDPMap.entrySet().iterator();
			
			while(iteRead.hasNext()) {
				String port =  (String) iteRead.next().getValue();
				
				clt.Send("127.0.0.1", Integer.valueOf(port), "COUNT");
				output += clt.Receive();
				
				
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {clt.close();}	
		return output;
		
	}
	
	public void transferRecord(String RID, String remoteAdd, String AID) {
		Record rec;
		try {
			rec = defSrv.select(RID);
		
			createRes res = null;
			do {
				res = srvMap.get(remoteAdd).createRecord(rec, AID);
			} while (res.getBoolean());
			defSrv.remove(RID);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	

}
