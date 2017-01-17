package com.itmyhome.service;

import java.util.List;

import com.itmyhome.dao.UserDao;
import com.itmyhome.dao.impl.UserDaoImpl;
import com.itmyhome.entity.User;

public class UserService {

	private UserDao userDao = new UserDaoImpl();
	
	public void register(User user) {
		userDao.saveOrUpdate(user);
	}
	
	public User validate(String uname, String passwd) {
		List<User> users = userDao.findByProperty("username", uname);
		if(users.size() > 0) {
			User user = users.get(0);
			if(user.getPassword().equals(passwd)) {
				return user;
			}
		}
		return null;
	}
	
	public List<User> list() {
		return userDao.findAll();
	}
	
	public List<User> listPage(int pageNo, int pageSize) {
		return userDao.listPage(pageNo, pageSize);
	}
	
	public User get(Integer id) {
		return userDao.find(id);
	}
	
	public void remove(Integer id){
		userDao.remove(id);
	}
	
	public void saveOrupdate(User u){
		userDao.saveOrUpdate(u);
	}
}
