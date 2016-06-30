package com.hqkang.DSMS.Server;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

import com.hqkang.DSMS.*;
import com.hqkang.DSMS.DoctorRecord.Location;


public class ClinicServer implements ServerInterface {
	private HashMap<String, String> srvURLMap = new HashMap<String, String>();
	private HashMap<String, String> srvUDPMap = new HashMap<String, String>();
	private DoctorRecord.Location location;
	private int count = 0;
	private Database db;
	private static Semaphore semp = new Semaphore(1);
	private HashMap<String, ServerInterface> srvMap = new HashMap<String, ServerInterface>();
	

	
	public long seq = 1l;
	public ClinicServer(String loca) { //Singleton
		db = new Database();
		srvURLMap.put("mtl", ("rmi://localhost:2020/srv")); //put the server URL to list
		srvURLMap.put("lvl", "rmi://localhost:2021/srv");
		srvURLMap.put("ddo", "rmi://localhost:2022/srv");
		srvUDPMap.put("mtl", "3020"); //put the server URL to list
		srvUDPMap.put("lvl", "3021");
		srvUDPMap.put("ddo", "3022");
		location = Location.valueOf(loca);
	
		}
	
	public String getMyURL() {
		return srvURLMap.get(location.toString());
	}
		
		
	public void connect() { //connect to each server for data exchange
		Iterator<Entry<String,String>> ite = srvURLMap.entrySet().iterator();
		try {
			while(ite.hasNext()) {
				Entry<String, String> e = ite.next();
				String srvURL = e.getValue();
				ServerInterface srv = null;
				
				srv = (ServerInterface) Naming.lookup(srvURL);
				srvMap.put(e.getKey(), srv);
			}
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
			
		
		
	
	//polymorphic
	public createRes createRecord(Record r,String AID) throws RemoteException { //add sync for resolving dead lock
		try {
			semp.acquire();	
			syncSeq();
			if(r.recordID == null) {
			r.recordID = r.getType() + (new DecimalFormat("00000").format(seq));		
			seq++;
			}
			count++;
			//other server seq not synced	
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			semp.release();
		}
		
		boolean res = db.insert(r); //out of semaphore DB takes care of concurrency
		writeLog(location + " " + r.recordID + "has been written" + res + " by " + AID);
		return new createRes(res,r.recordID);
	}
	

	
	public String getRecordCounts(String AID)  {
		int cnt = -1;
		try {
			semp.acquire();		//not critical, read only
			cnt = this.count;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();   
		} finally {semp.release();}
		writeLog(location + " counts requests by " + AID);
		return location.toString() +" "+ cnt;
		
	}
	
	
	public HashMap<String, String> getSrvURLList() {
		return srvURLMap;
	}
	public HashMap<String, String> getSrvUDPList() {
		return srvUDPMap;
	}
	
	public long getSeq() {
		
		writeLog(location + " syncing sequence starts");
		return seq;
	}
	
	public boolean setSeq(long s) {
		boolean res = false;
		seq = s;
		res = true;
		writeLog(location + " syncing sequence completes");	
		return res;
	}
	
	
	public boolean writeLog( String content) {
		File file = new File(location.toString());
		FileOutputStream outStream = null;
		try {
			if (!file.exists()) {
				
					file.createNewFile();
				
			}
		outStream = new FileOutputStream(file, true);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
		byte[] contentInBytes = (df.format(new Date()) + " "+ (location.toString() + " : " + content)+"\n\r").getBytes();
		 
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
	
	public boolean editRecord(String RID, String fieldName, String newValue, String AID) {
		try {
			boolean result = db.update(RID, fieldName, newValue);
			writeLog(location + RID + " updating" + fieldName +" : "+ newValue + " " + result + " by " + AID);
			
			return result;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean check(String key) {
		return db.check(key);
	}
	
	public Record select(String RID) {
		return db.select(RID);
	}
	
	public boolean remove(String RID) {
		return db.delete(RID);
	}
	
	public void exportServer(int port) throws Exception {
		Remote obj = UnicastRemoteObject.exportObject(this, port);
		Registry r =  LocateRegistry.createRegistry(port);
		r.bind("srv", obj);
		
		System.out.println(location + " Server Exported");
	}

	 private boolean syncSeq() throws RemoteException {  //sync as an atomic operation
		try {
		semp.acquire();
		List<Long> seqList = new LinkedList<Long>();
		Iterator<Entry<String, ServerInterface>> iteRead = srvMap.entrySet().iterator();
		while(iteRead.hasNext()) {
			ServerInterface srv =  (ServerInterface) iteRead.next().getValue(); //get Val from each srv
			seqList.add(srv.getSeq());		
			
		}
		long max = (long) Collections.max(seqList); //find max
		Iterator<Entry<String, ServerInterface>> iteWrite = srvMap.entrySet().iterator();
		while(iteWrite.hasNext()) {
			ServerInterface srv =  (ServerInterface) iteWrite.next().getValue(); 
			boolean setRes = false;
			do{
			setRes = srv.setSeq(max); //set back
			} while(!setRes);
		}
		return true;
		} catch (Exception e) {
			return false;
		} finally {
		semp.release();
			
		}

	}
	 
	 public boolean transferRecord(String RID, String remoteAdd,String AID) {
			Record rec = null;
			int count = 0;
			createRes res = null;
			boolean remRes = false;
			boolean finalRes = false;
			try {
				rec = select(RID);
				
				do {
					count++;
					res = srvMap.get(remoteAdd).createRecord(rec, AID);
				} while (!res.getBoolean()&&count<4);
			
				if(true == res.getBoolean()) {
					remRes = remove(RID);
					if(false == remRes) {
						srvMap.get(remoteAdd).remove(RID);
					} else {
						finalRes = true; 
					}
				
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writeLog(rec.recordID + "has been transfered" + res + "by" + AID);
			
			return finalRes;
			
		}
	  
	
		
			
}
	



	


