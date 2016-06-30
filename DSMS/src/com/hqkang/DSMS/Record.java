package com.hqkang.DSMS;

import java.io.Serializable;


public abstract class Record implements Serializable,RecordInterface{
	public String recordID;
	public String firstName;
	public String lastName;
	public abstract String getType();
	//public abstract Record CorbaRecord2Record(com.hqkang.DSMS.Server.ClinicSPackage.Record s);
	public abstract com.hqkang.DSMS.Server.ClinicSPackage.Record record2CorbaRecord();

}
