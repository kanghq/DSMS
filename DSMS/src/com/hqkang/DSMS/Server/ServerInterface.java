package com.hqkang.DSMS.Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


import com.hqkang.DSMS.Record;
import com.hqkang.DSMS.createRes;

public interface ServerInterface extends Remote{
	public HashMap<String, String> getSrvURLList() throws RemoteException;
	public HashMap<String, String> getSrvUDPList() throws RemoteException;
	public String getRecordCounts(String AID) throws RemoteException;
	public createRes createRecord(Record r, String AID) throws RemoteException;
	public long getSeq() throws RemoteException;
	public boolean check(String key) throws RemoteException;
	public boolean editRecord(String RID, String field, String value, String AID) throws RemoteException;
	public boolean setSeq(long s) throws RemoteException;
	public Record select(String RID) throws RemoteException;
	public boolean remove(String RID) throws RemoteException;
	public boolean transferRecord(String rID, String remoteAdd, String aID) throws RemoteException;

	
}