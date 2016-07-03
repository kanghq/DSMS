package com.hqkang.DSMS;

import java.io.Serializable;

public class createRes implements Serializable {
	boolean res;
	String RID;
	public createRes(boolean _res, String _RID) {
		res = _res;
		RID = _RID;
	}
	public boolean getBoolean() {
		return res;
	}
	public String getRID() {
		return RID;
	}
	
}
