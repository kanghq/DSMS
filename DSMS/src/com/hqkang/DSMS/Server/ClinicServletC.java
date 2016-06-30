package com.hqkang.DSMS.Server;

import java.rmi.RemoteException;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import com.hqkang.DSMS.RecordInterface;
import com.hqkang.DSMS.createRes;
import com.hqkang.DSMS.Server.ClinicSPackage.MapItem;



public class ClinicServletC extends ClinicSPOA implements ClinicSOperations{
	
	private ClinicServer srv = null;


	public ClinicServletC(ClinicServer s) {
		srv = s;
		// TODO Auto-generated constructor stub
	}
	
	

	@Override
	public MapItem[] getSrvURLList() {
		// TODO Auto-generated method stub
		return ServerIntConverter.HashMap2Array(srv.getSrvURLList());
	}

	@Override
	public MapItem[] getSrvUDPList() {
		// TODO Auto-generated method stub
		return ServerIntConverter.HashMap2Array(srv.getSrvUDPList());
	}

	@Override
	public String getRecordCounts(String AID) {
		// TODO Auto-generated method stub
		return srv.getRecordCounts(AID);
	}

	@Override
	public com.hqkang.DSMS.Server.ClinicSPackage.createRes createRecord(com.hqkang.DSMS.Server.ClinicSPackage.Record r,
			String AID) {
		// TODO Auto-generated method stub
		createRes res = null;
		try {
			res = srv.createRecord(RecordInterface.CorbaRecord2Record(r), AID);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		com.hqkang.DSMS.Server.ClinicSPackage.createRes Cres = new com.hqkang.DSMS.Server.ClinicSPackage.createRes(res.getBoolean(), res.getRID());
		return Cres;
	}

	@Override
	public int getSeq() {
		// TODO Auto-generated method stub
		return (int)srv.getSeq();
	}

	@Override
	public boolean setSeq(int s) {
		// TODO Auto-generated method stub
		return srv.setSeq(s);
		
	}

	@Override
	public com.hqkang.DSMS.Server.ClinicSPackage.Record select(String RID) {
		// TODO Auto-generated method stub
		return srv.select(RID).record2CorbaRecord();
	}



	@Override
	public boolean check(String key) {
		// TODO Auto-generated method stub
		return srv.check(key);
	}



	@Override
	public boolean editRecord(String RID, String field, String value, String AID) {
		// TODO Auto-generated method stub
		return srv.editRecord(RID, field, value, AID);
	}



	@Override
	public boolean remove(String RID) {
		// TODO Auto-generated method stub
		return srv.remove(RID);
	}
	
	public void export(POA rootpoa,org.omg.CosNaming.NamingContextExt ncRef) {
		
		try {
	     rootpoa.the_POAManager().activate();
		
		
		org.omg.CORBA.Object srvRef = rootpoa.servant_to_reference(this);
		ClinicS csO = ClinicSHelper.narrow(srvRef);
		
		NameComponent path[];
		
			path = ncRef.to_name(srv.getMyURL());
		
		ncRef.rebind(path, csO);
		System.out.println("CORBA server exported");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
