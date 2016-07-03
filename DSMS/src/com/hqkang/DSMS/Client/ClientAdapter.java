package com.hqkang.DSMS.Client;

import java.rmi.RemoteException;
import java.util.HashMap;

import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import com.hqkang.DSMS.RecordInterface;
import com.hqkang.DSMS.createRes;
import com.hqkang.DSMS.Server.ClinicS;
import com.hqkang.DSMS.Server.ClinicSHelper;
import com.hqkang.DSMS.Server.ServerIntConverter;
import com.hqkang.DSMS.Server.ServerInterface;


public class ClientAdapter implements ServerInterface {
	
	ClinicS srv = null;
	NamingContext ncRef = null;
	
	
	public ClientAdapter(ClinicS s) {
		srv = s;
	}
	
	public boolean setNCref(NamingContext n) {
		boolean res = false;
		ncRef = n;
		res = true;
		return res;
	}
	
	public ClientAdapter(String url, NamingContext n) {
		
		ncRef = n;
		NameComponent nc = new NameComponent(url, "");
		NameComponent path[] = {nc};
		try {
			srv = ClinicSHelper.narrow(ncRef.resolve(path));
			
		} catch (NotFound | CannotProceed | InvalidName e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public HashMap<String, String> getSrvURLList() {
		// TODO Auto-generated method stub
		return ServerIntConverter.Array2HashMap(srv.getSrvURLList());
	}

	@Override
	public HashMap<String, String> getSrvUDPList() {
		// TODO Auto-generated method stub
		return ServerIntConverter.Array2HashMap(srv.getSrvUDPList());
	}

	@Override
	public String getRecordCounts(String AID) {
		// TODO Auto-generated method stub
		return srv.getRecordCounts(AID);
	}

	@Override
	public createRes createRecord(com.hqkang.DSMS.Record r, String AID) {
		// TODO Auto-generated method stub
		return ServerIntConverter.CcreateRes2JcreateRes(
				srv.createRecord(
						r.record2CorbaRecord(), AID));
	}

	@Override
	public long getSeq() {
		// TODO Auto-generated method stub
		return srv.getSeq();
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
	public boolean setSeq(long s) {
		// TODO Auto-generated method stub
		return srv.setSeq((int) s);
		
	}

	@Override
	public com.hqkang.DSMS.Record select(String RID) {
		// TODO Auto-generated method stub
		return RecordInterface.CorbaRecord2Record(srv.select(RID));
	}

	@Override
	public boolean remove(String RID) {
		// TODO Auto-generated method stub
		return srv.remove(RID);
	}

	@Override
	public boolean transferRecord(String rID, String remoteAdd, String aID) {
		// TODO Auto-generated method stub
		return srv.transferRecord(rID, remoteAdd, aID);
	}

	

}
