package com.hqkang.DSMS.Server.ClinicSPackage;


/**
* com/hqkang/DSMS/Server/ClinicSPackage/createRes.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/kanghuaqiang/git/DSMS/DSMS/src/ClinicServer.idl
* Wednesday, June 29, 2016 11:08:18 PM EDT
*/

public final class createRes implements org.omg.CORBA.portable.IDLEntity
{
  public boolean res = false;
  public String RID = null;

  public createRes ()
  {
  } // ctor

  public createRes (boolean _res, String _RID)
  {
    res = _res;
    RID = _RID;
  } // ctor

} // class createRes