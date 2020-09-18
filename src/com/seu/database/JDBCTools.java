package com.seu.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.mchange.v2.c3p0.ComboPooledDataSource;


/**
 * JDBC 的工具类
 * 
 * 其中包含: 获取数据库连接, 关闭数据库资源等方法.
 */
public class JDBCTools {

	// public static Connection getConnection() throws Exception {
	// Properties properties = new Properties();
	// InputStream inStream = JDBCTools.class.getClassLoader()
	// .getResourceAsStream("jdbc.properties");
	// properties.load(inStream);
	//
	// // 1. 准备获取连接的 4 个字符串: user, password, jdbcUrl, driverClass
	// String user = properties.getProperty("user");
	// String password = properties.getProperty("password");
	// String jdbcUrl = properties.getProperty("jdbcUrl");
	// String driverClass = properties.getProperty("driverClass");
	//
	// // 2. 加载驱动: Class.forName(driverClass)
	// Class.forName(driverClass);
	//
	// // 3. 调用
	// // DriverManager.getConnection(jdbcUrl, user, password)
	// // 获取数据库连接
	// Connection connection = DriverManager.getConnection(jdbcUrl, user,
	// password);
	// return connection;
	// }
	private static ComboPooledDataSource ds = null;

	// 在静态代码块中创建数据库连接池
	static {
		try {
			// 通过代码创建C3P0数据库连接池
			/*
			 * ds = new ComboPooledDataSource();
			 * ds.setDriverClass("com.mysql.jdbc.Driver");
			 * ds.setJdbcUrl("jdbc:mysql://localhost:3306/jdbcstudy");
			 * ds.setUser("root"); ds.setPassword("XDP");
			 * ds.setInitialPoolSize(10); ds.setMinPoolSize(5);
			 * ds.setMaxPoolSize(20);
			 */

			// 通过读取C3P0的xml配置文件创建数据源，C3P0的xml配置文件c3p0-config.xml必须放在src目录下
			// ds = new ComboPooledDataSource();//使用C3P0的默认配置来创建数据源
			ds = new ComboPooledDataSource("MySQL");// 使用C3P0的命名配置来创建数据源

		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * @Method: getConnection
	 * @Description: 从数据源中获取数据库连接
	 * @Anthor:zk
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		// 从数据源中获取数据库连接
		return ds.getConnection();
	}

	public static void releaseDB(ResultSet resultSet, PreparedStatement statement, Connection connection) {

		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// name 表名
	public static boolean HasTable(String name) {
		// 判断某一个表是否存在
		Connection conn = null;
		ResultSet set = null;
		boolean result = false;
		try {
			conn = getConnection();
			DatabaseMetaData meta = (DatabaseMetaData) conn.getMetaData();// sqlConn
																			// 数据库连接
			set = meta.getTables(null, null, name, null);
			while (set.next()) {
				result = true;
			}
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		} finally {
			if (set != null) {
				try {
					set.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	// 创建数据库表
	public static void createTable(String sql) {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		try {
			conn = getConnection();
			preparedStatement = conn.prepareStatement(sql);
			System.out.println("要创建数据库了");
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("无法创建数据库");
		} finally {
			releaseDB(null, preparedStatement, conn);
		}
	}
	
	
	 public static void free(ResultSet rs,Statement st,Connection conn){
			try{
				if(rs != null){
					rs.close();
				}
			}catch(SQLException e){
				e.printStackTrace();
			}finally{
				try{
					if(st != null){
						st.close();
					}
				}catch(SQLException e){
					e.printStackTrace();
				}finally{
					if(conn != null){
						try {
							conn.close();
							
						} catch (Exception e) {
							
							e.printStackTrace();
						}
					}
				}
				
			}
		}
}
