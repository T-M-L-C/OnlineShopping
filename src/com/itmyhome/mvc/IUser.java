package com.itmyhome.mvc;

/**
 * 用户接口类
 * */
public interface IUser {
	
	/**
	 * 用户是否锁定
	 * */
	public boolean IsLocked();

	/**
	 * 获得用户ID
	 * */
	public Integer getId();

	/**
	 * 获得用户编号
	 * */
	public String getPasswd();
}
