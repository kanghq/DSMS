package com.hqkang.DSMS;

import com.hqkang.DSMS.*;
import com.hqkang.DSMS.DoctorRecord.Location;
import com.hqkang.DSMS.NurseRecord.Designation;
import com.hqkang.DSMS.NurseRecord.Status;
public interface RecordInterface {
	/**
	 *  convert corba record structure to java record obj
	 * @param s
	 * @return
	 */
	public static Record CorbaRecord2Record(com.hqkang.DSMS.Server.ClinicSPackage.Record s) {
		Record r = null;
		if(s._class.equals(NurseRecord.class.getName())) {
			r = new NurseRecord("", s.firstName, s.lastName, Designation.valueOf(s.designation), Status.valueOf(s.status), (java.util.Date)new java.util.Date(s.statusdate));
		}
		if(s._class.equals(DoctorRecord.class.getName())) {
			r = new DoctorRecord("", s.firstName, s.lastName, s.address, s.phone, s.specialization, Location.valueOf(s.location));
		}
		return r;
	}

}
