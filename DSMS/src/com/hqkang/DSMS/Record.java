package com.hqkang.DSMS;

import java.io.Serializable;

public abstract class Record implements Serializable{
	public String recordID;
	public String firstName;
	public String lastName;
	public abstract String getType();
	

}
