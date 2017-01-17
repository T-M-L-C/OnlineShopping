package com.itmyhome.db;




import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.ArrayUtils;

/**
 * 数据库查询助手
 * */
@SuppressWarnings({ "unchecked", "serial" })
public class QueryHelper {

	private final static QueryRunner _g_runner = new QueryRunner(
			DBManager.getDataSource());
	private final static ColumnListHandler _g_columnListHandler = new ColumnListHandler() {
		@Override
		protected Object handleRow(ResultSet rs) throws SQLException {
			Object obj = super.handleRow(rs);
			if (obj instanceof BigInteger)
				return ((BigInteger) obj).longValue();
			return obj;
		}
	};
	private final static ScalarHandler _g_scaleHandler = new ScalarHandler() {
		@Override
		public Object handle(ResultSet rs) throws SQLException {
			Object obj = super.handle(rs);
			if (obj instanceof BigInteger)
				return ((BigInteger) obj).longValue();
			return obj;
		}
	};

	private final static List<Class<?>> PrimitiveClasses = new ArrayList<Class<?>>() {
		{
			add(Long.class);
			add(Integer.class);
			add(String.class);
			add(java.util.Date.class);
			add(java.sql.Date.class);
			add(java.sql.Timestamp.class);
		}
	};

	private final static boolean _IsPrimitive(Class<?> cls) {
		return cls.isPrimitive() || PrimitiveClasses.contains(cls);
	}

	/**
	 * 获取数据库连接
	 */
	public static Connection getConnection() {
		try {
			return DBManager.getConnection();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * 读取某个对象
	 */
	public static <T> T queryUnique(Class<T> beanClass, String sql,
			Object... params) {
		try {
			return (T) _g_runner.query(sql,
					_IsPrimitive(beanClass) ? _g_scaleHandler
							: new BeanHandler<T>(beanClass), params);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * 对象查询
	 */
	public static <T> List<T> query(Class<T> beanClass, String sql,
			Object... params) {
		try {
			return (List<T>) _g_runner.query(sql,
					_IsPrimitive(beanClass) ? _g_columnListHandler
							: new BeanListHandler<T>(beanClass), params);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * 分页查询
	 */
	public static <T> List<T> queryPage(Class<T> beanClass, String sql,
			int page, int count, Object... params) {
		if (page < 0 || count < 0)
			throw new IllegalArgumentException(
					"Illegal parameter of 'page' or 'count', Must be positive.");
		int from = (page - 1) * count;
		count = (count > 0) ? count : Integer.MAX_VALUE;
		return query(beanClass, sql + " LIMIT ?,?",
				ArrayUtils.addAll(params, new Integer[] { from, count }));
	}

	/**
	 * 执行统计查询语句，语句的执行结果必须只返回一个数值
	 */
	public static long stat(String sql, Object... params) {
		try {
			Number num = (Number) _g_runner.query(sql, _g_scaleHandler, params);
			return (num != null) ? num.longValue() : -1;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * 执行INSERT/UPDATE/DELETE语句
	 */
	public static int update(String sql, Object... params) {
		try {
			return _g_runner.update(sql, params);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	/**
	 * 批量执行指定的SQL语句
	 */
	public static int[] batch(String sql, Object[][] params) {
		try {
			return _g_runner.batch(sql, params);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
