package com.hqkang.DSMS.Server;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.hqkang.DSMS.NurseRecord.Status;
import com.hqkang.DSMS.*;

public class Database {
	
	
	HashMap<Character, CopyOnWriteArrayList<Record>> db = new HashMap<Character, CopyOnWriteArrayList<Record>>();
	Hashtable<String, Character> index = new Hashtable<String, Character>();
	Hashtable<String, ReentrantReadWriteLock> lockTbl = new Hashtable<String, ReentrantReadWriteLock>();
	private Semaphore LkTblSmp = new Semaphore(1);
	

	
	boolean update(String key, String field, String value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (!index.containsKey(key)) return false;	//check existence again
		ReentrantReadWriteLock lck = null;
		char schName = (char) index.get(key).charValue();
		CopyOnWriteArrayList<Record> schema = (CopyOnWriteArrayList<Record>) db.get(schName); //get list of last name
		Iterator<Record> ite = schema.iterator();
		while (ite.hasNext()) {
			Record rec = ite.next();
			if (rec.recordID.equals(key)) {
				lck = lockTbl.get(rec.recordID);
				try{
				lck.writeLock().lock();
				schema.remove(rec);
				Class<?> current = rec.getClass();
				Field nameField = null;
				do {
					try{
			      
			    	  nameField = current.getDeclaredField(field);	//get field
					} catch (NoSuchFieldException e) {}
			      
				} while((current = current.getSuperclass()) != null);
				if(null == nameField) return false;
				nameField.setAccessible(true);
				if(nameField.getType().isEnum()) {
					nameField.set(rec, Enum.valueOf((Class<Enum>) nameField.getType(), value));
				} else {
				
				nameField.set(rec, value);	//reflection set back
				}
				insert(rec);	//put back record
				} catch(Exception e) { return false;}
				finally {lck.writeLock().unlock();}
				return true;
			} 

		}
		return false;
		
		
	} 
	
	boolean insert(Record rec) {
		String lastName = rec.lastName;
		char lNC = lastName.charAt(0); //last name char
		CopyOnWriteArrayList<Record> schema = null;
		try {
			LkTblSmp.acquire();
			if(lockTbl.containsKey(rec.recordID)) return false;
			lockTbl.put(rec.recordID, new ReentrantReadWriteLock()); //make sure only one key inserted. concurrency
		} catch (Exception e){}
		finally {LkTblSmp.release();}
		
		try {
			lockTbl.get(rec.recordID).writeLock().lock();
			if (index.containsKey(rec.recordID)) return false;
		if (!db.containsKey(lNC)) {
			db.put(lNC, new CopyOnWriteArrayList<Record>()); 
		}
		schema = (CopyOnWriteArrayList<Record>) db.get(lNC);
		schema.add(rec);
		db.put(lNC, schema);
		index.put(rec.recordID, rec.lastName.substring(0, 1).toCharArray()[0]); //index for ID search		
		} catch (Exception e) {}
		finally {lockTbl.get(rec.recordID).writeLock().unlock();}
		return true;		
		
	}
	
	public boolean check(String key) {
		boolean exists = false;
		if(null!=lockTbl.get(key)) {
		
			try {
				lockTbl.get(key).readLock().lock();
				exists = index.containsKey(key);
			
			} catch(Exception e) {} 
			finally {
				
				lockTbl.get(key).readLock().unlock();
				
			}
		}
		return exists;
	}
	
	Record select(String key) {
		if(!index.containsKey(key)) return null;
		char schName = (char) index.get(key);
		CopyOnWriteArrayList<Record> schema = (CopyOnWriteArrayList<Record>) db.get(schName);
		Iterator<Record> ite = schema.iterator();
		Record rec = null;
		while (ite.hasNext()) {
			rec = ite.next();
			if (rec.recordID.equals(key)) {
				ReentrantReadWriteLock lck = lockTbl.get(key);
				try {
				lck.readLock().lock();
				break;
				} 
				finally {lck.readLock().unlock();}
			}
		}
		return rec;
		
		
	}
	
		
	
	
	boolean delete(String key) {
		if(!index.containsKey(key)) return false;
		char schName = (char) index.get(key);
		CopyOnWriteArrayList<Record> schema = (CopyOnWriteArrayList<Record>) db.get(schName);
		Iterator<Record> ite = schema.iterator();
		while (ite.hasNext()) {
			Record rec = ite.next();
			if (rec.recordID.equals(key)) {
				ReentrantReadWriteLock lck = lockTbl.get(key);
				try {
				lck.writeLock().lock();
				schema.remove(rec);		//remove element
				index.remove(key);		//remove index record
				lockTbl.remove(key);	//remove lock
				} catch(Exception e) {}
				finally {lck.writeLock().unlock();}
			return true;
			} 

		}
		return false;
	}

	
	
	


}
