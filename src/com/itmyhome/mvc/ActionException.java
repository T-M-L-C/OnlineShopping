package com.itmyhome.mvc;

@SuppressWarnings("serial")
public class ActionException extends RuntimeException {

	public ActionException(Exception e) {
		super(e);
	}
	
	public ActionException(String msg) {
		super(msg);
	}
}
