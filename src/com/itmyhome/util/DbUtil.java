package com.itmyhome.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 尽量在同一个线程中能共享Connection对象
 * */
public class DbUtil {

	private static String url;
	private static String username;
	private static String password;
	// 创建一个线程局部变量：每个线程都有一个该变量，它们是不一样的对象
	private static ThreadLocal<Connection> conns = new ThreadLocal<Connection>();
	
	static {
		try {
			Properties prop = new Properties();
			prop.load(DbUtil.class.getResourceAsStream("/jdbc.properties"));
			
			String driver = prop.getProperty("jdbc.driverClass");
			Class.forName(driver);
			
			url = prop.getProperty("jdbc.jdbcUrl");
			username = prop.getProperty("jdbc.user");
			password = prop.getProperty("jdbc.password");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得Connection对象
	 * 线程共享
	 * */
	public static Connection getConn() {
		Connection conn = conns.get();
		try {
			if(conn == null) {
				conn = DriverManager.getConnection(url, username, password);
				conns.set(conn);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 关闭当前线程中的Connection
	 * */
	public static void colseConn() {
		try {
			Connection conn = conns.get();
			conns.set(null);
			if(conn!=null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		Connection conn = DbUtil.getConn();
		Connection conn2 = DbUtil.getConn();
		System.out.println(conn);
		System.out.println(conn2);
		// 创建一个线程
		new Thread(){
			@Override
			public void run() {
				Connection conn3 = DbUtil.getConn();
				System.out.println(conn3);
			}
		}.start();
		
		DbUtil.colseConn();
		System.out.println(conn2.isClosed());
		
	}
}
