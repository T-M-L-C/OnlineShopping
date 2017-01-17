package com.itmyhome.dao;

import java.util.List;

import com.itmyhome.entity.User;

public interface UserDao {

	/**
	 * 增加�?��用户
	 * */
	public void saveOrUpdate(User user);
	
	/**
	 * 删除�?��用户
	 * */
	public void remove(Integer id);
	
	/**
	 * 查询�?��用户
	 * */
	public List<User> findAll();
	
	/**
	 * 根据id查找用户
	 * */
	public User find(Integer id);
	
	/**
	 * 根据某属性进行查�?
	 * */
	public List<User> findByProperty(String propName, Object value);

	public List<User> listPage(int pageNo, int pageSize);
}
