package com.cheetahload.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Operator {
	private static ConnectionPool connectionPool;
	private static Operator operator;

	public static Operator getOperator(String dbClassName, String url, int maxQueueSize) {
		if (dbClassName == null || dbClassName.isEmpty()) {
			System.out.println(
					"Error: Operator - getOperator(String dbClassName, String url, int maxQueueSize) - Parameter dbClassName is null or empty. Connection pool creation failed.");
		} else if (url == null || url.isEmpty()) {
			System.out.println(
					"Error: Operator - getOperator(String dbClassName, String url, int maxQueueSize) - Parameter url is null or empty. Connection pool creation failed.");
		} else {
			connectionPool = new ConnectionPool(dbClassName, url, maxQueueSize);
		}
		if (operator == null) {
			operator = new Operator();
		}
		return operator;
	}

	public boolean execute(List<String> sqlList) {
		if (null == sqlList || sqlList.isEmpty()) {
			System.out.println("ERROR: Operator - execute(List<String> sqlList) - SQL list is null or empty.");
			return false;
		}
		Connection connection = connectionPool.get();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Iterator<String> iter = sqlList.iterator();
			String sql;
			while (iter.hasNext()) {
				sql = iter.next();
				try {
					statement.addBatch(sql);
				} catch (SQLException e) {
					System.out.println(
							"ERROR: Operator - execute(List<String> sqlList) - Failed to add batch to create tables. SQL: '"
									+ sql + "'");
					e.printStackTrace();
					return false;
				}
			}
			statement.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("ERROR: Operator - execute(List<String> sqlList) - execute SQL statements failed.");
			e.printStackTrace();
			return false;
		} finally {
			if (null != statement) {
				try {
					statement.close();
				} catch (SQLException e) {
					System.out.println("ERROR: Operator - execute(List<String> sqlList) - Failed to close statement.");
					e.printStackTrace();
				}
			}
			connectionPool.release(connection);
		}
		return true;
	}

	public boolean execute(String sql) {
		if (null == sql || sql.isEmpty()) {
			System.out.println("ERROR: Operator - execute(String sql) - SQL string is null or empty.");
			return false;
		}
		Connection connection = connectionPool.get();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.execute(sql);
			connection.commit();
		} catch (SQLException e) {
			System.out.println(
					"ERROR: Operator - execute(String sql) - execute SQL statement failed. SQL: '" + sql + "'");
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				System.out.println("ERROR: Operator - execute(String sql) - Roll back failed.");
				e1.printStackTrace();
				return false;
			}
			return false;
		} finally {
			if (null != statement) {
				try {
					statement.close();
				} catch (SQLException e) {
					System.out.println("ERROR: Operator - execute(String sql) - Failed to close statement.");
					e.printStackTrace();
				}
			}
			connectionPool.release(connection);
		}
		return true;
	}

	public List<LinkedList<Object>> get(String sql) {
		List<LinkedList<Object>> list = new LinkedList<LinkedList<Object>>();
		if (null == sql || sql.isEmpty()) {
			System.out.println("ERROR: Operator - get(String sql) - SQL string is null or empty.");
			return list;
		}
		Connection connection = connectionPool.get();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			if (resultSet != null) {
				int columnCount = resultSet.getMetaData().getColumnCount();
				if (columnCount > 0) {
					while (resultSet.next()) {
						LinkedList<Object> sublist = new LinkedList<Object>();
						for (int i = 0; i < columnCount; i++) {
							Object object = resultSet.getObject(0);
							sublist = new LinkedList<Object>();
							sublist.add(object);
						}
						list.add(sublist);
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("ERROR: Operator - get(String sql) - execute SQL statement failed. SQL: '" + sql + "'");
			e.printStackTrace();
			return list;
		} finally {
			if (null != statement) {
				try {
					statement.close();
				} catch (SQLException e) {
					System.out.println("ERROR: Operator - get(String sql) - Failed to close statement.");
					e.printStackTrace();
				}
			}
			connectionPool.release(connection);
		}
		return list;
	}

	public LinkedList<Object> getFirst(String sql) {
		LinkedList<Object> list = new LinkedList<Object>();
		if (null == sql || sql.isEmpty()) {
			System.out.println("ERROR: Operator - getFirst(String sql) - SQL string is null or empty.");
			return list;
		}
		Connection connection = connectionPool.get();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			if (resultSet != null) {
				int columnCount = resultSet.getMetaData().getColumnCount();
				if (columnCount > 0) {
					resultSet.next();
					for (int i = 0; i < columnCount; i++) {
						Object object = resultSet.getObject(0);
						list.add(object);
					}
				}
			}
		} catch (SQLException e) {
			System.out.println(
					"ERROR: Operator - getFirst(String sql) - execute SQL statement failed. SQL: '" + sql + "'");
			e.printStackTrace();
			return list;
		} finally {
			if (null != statement) {
				try {
					statement.close();
				} catch (SQLException e) {
					System.out.println("ERROR: Operator - getFirst(String sql) - Failed to close statement.");
					e.printStackTrace();
				}
			}
			connectionPool.release(connection);
		}
		return list;
	}
	
	
}
