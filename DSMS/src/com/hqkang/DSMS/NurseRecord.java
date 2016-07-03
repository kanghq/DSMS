package com.hqkang.DSMS;
import java.util.Date;


public class NurseRecord extends Record {
	public enum Designation {junior, senior}
	public enum Status {active, terminated}
	Designation designation;
	Status status;
	Date statusDate = new Date();
	/**
	 * 
	 * @param _recordID
	 * @param _firstName
	 * @param _lastName
	 * @param _designation
	 * @param _status
	 * @param _statusDate
	 */
	public NurseRecord(String _recordID, String _firstName, String _lastName, Designation _designation, Status _status, Date _statusDate) {
		this.recordID = _recordID == null?"":_recordID;
		this.firstName = _firstName;
		this.lastName = _lastName;
		this.designation = _designation;
		this.status = _status;
		this.statusDate = _statusDate;
	}
	
	public String toString() {
		return this.recordID + " "+ this.firstName + " "+ this.lastName + " ";
	}
	
	public String getType() {
		return "NR";
	}

	@Override
	public com.hqkang.DSMS.Server.ClinicSPackage.Record record2CorbaRecord() {
		// TODO Auto-generated method stub
		return new com.hqkang.DSMS.Server.ClinicSPackage.Record(recordID, firstName, lastName, designation.toString(), status.toString(), (int)statusDate.getTime(), "", "", "", "", this.getClass().getName());
	}



}
