package com.hqkang.DSMS.Server.ClinicSPackage;


/**
* com/hqkang/DSMS/Server/ClinicSPackage/createRes.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/kanghuaqiang/git/DSMS/DSMS/src/ClinicServer.idl
* Saturday, July 2, 2016 1:03:51 AM EDT
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
