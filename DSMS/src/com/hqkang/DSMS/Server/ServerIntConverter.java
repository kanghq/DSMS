package com.hqkang.DSMS.Server;

import java.util.HashMap;

import com.hqkang.DSMS.Server.ClinicSPackage.MapItem;
import com.hqkang.DSMS.Server.ClinicSPackage.createRes;

public final class ServerIntConverter {
	 public static MapItem[] HashMap2Array(HashMap<String, String> h) {
		 String[] keyA = null;
		 keyA = h.keySet().toArray(new String[h.keySet().size()]);
		 String[] VA = null;
		 VA = h.values().toArray(new String[h.values().size()]);
		 MapItem[] res = new MapItem[h.size()];
		 for(int i = 0; i < h.size(); i++) {
			 res[i] = new MapItem(keyA[i],VA[i]);
		 }
		 return res;
	 }
	 
	 public static HashMap<String, String> Array2HashMap(MapItem[] m) {
		 HashMap<String, String> res = new HashMap<String, String>();
		 for(int i = 0; i < m.length; i++) {
			 res.put(m[i].key, m[i].value);
		 }
		 return res;
	 }
	 
	 public static createRes JcreateRes2CcreateRes(com.hqkang.DSMS.createRes r) {
		 return new com.hqkang.DSMS.Server.ClinicSPackage.createRes(r.getBoolean(), r.getRID());
	 }
	 
	 public static com.hqkang.DSMS.createRes CcreateRes2JcreateRes(createRes r) {
		 return new com.hqkang.DSMS.createRes(r.res, r.RID);
	 }

}
