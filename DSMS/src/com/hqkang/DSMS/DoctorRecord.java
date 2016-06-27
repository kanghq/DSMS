package com.hqkang.DSMS;

import java.io.Serializable;

public class DoctorRecord extends Record {
	String address;
	String phone;
	String specialization;
	public enum Location {mtl, lvl, ddo}
	public Location location;
	
	public DoctorRecord(String _recordID, String _firstName, String _lastName, String _address, String _phone, String _specialization, Location _loc) {
			this.recordID = _recordID;
			this.firstName = _firstName;
			this.lastName = _lastName;
			this.address = _address;
			this.phone = _phone;
			this.specialization = _specialization;
			this.location = _loc;
		}
	public String toString() {
		return this.recordID + " "+ this.firstName + " "+ this.lastName + " ";
	}
	
	public String getType() {
		return "DR";
	}

}
