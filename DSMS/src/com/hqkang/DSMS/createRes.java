package com.hqkang.DSMS;

public class createRes {
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
