package com.itmyhome.dao.impl;


import java.util.List;


import com.itmyhome.dao.UserDao;
import com.itmyhome.db.QueryHelper;
import com.itmyhome.entity.User;

public class UserDaoImpl implements UserDao {

	public User find(Integer id) {
		String sql = "select * from t_user where id=?";
		return QueryHelper.queryUnique(User.class, sql, id);
	}

	public List<User> findAll() {
		String sql = "select * from t_user";
		return QueryHelper.query(User.class, sql);
	}

	public List<User> findByProperty(String propName, Object value) {
		String sql = "select * from t_user where " + propName + "=?";
		return QueryHelper.query(User.class, sql, value);
	}

	public void remove(Integer id) {
		String sql = "delete from t_user where id=?";
		QueryHelper.update(sql, id);
	}

	public void saveOrUpdate(User user) {
		if(user.getId() == null) {
			//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String sql = "insert into t_user(username,password,sex) values(?,?,?)";
			QueryHelper.update(sql,user.getUsername(), user.getPassword());
		} else {
			String sql = "update t_user set username=?, password=? ,sex=? where id=?";
			QueryHelper.update(sql, user.getUsername(), user.getPassword(),user.getSex(), user.getId());
		}
	}
	
	public List<User> listPage(int pageNo, int pageSize) {
		String sql = "select * from t_user";
		//String sql="select * from (select rownum rn,t_user.* from t_user) where rn between ? and ?";
		return QueryHelper.queryPage(User.class, sql, pageNo, pageSize);
	}
	
	
}
