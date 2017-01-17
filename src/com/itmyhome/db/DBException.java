package com.itmyhome.db;

@SuppressWarnings("serial")
public class DBException extends RuntimeException {

	public DBException(String message, Throwable cause) {
		super(message, cause);
	}

	public DBException(String message) {
		super(message);
	}
	
	public DBException(Exception e) {
		e.printStackTrace();
	}
}
