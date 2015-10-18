/**
 * BaseDAO.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.jdbc.lmdao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseDAO {
	protected abstract Object convertResult(ResultSet rs) throws SQLException;
	
	protected Connection getConnection() throws SQLException {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/library", 
				"root", "password");
		return conn;
	}
	
//	protected int save(String query, Object[] values, String key) throws SQLException {
//		PreparedStatement stmt = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
//		
//		int count = 1;
//		for(Object obj : values) {
//			stmt.setObject(count, obj);
//			count++;
//		}
//		
//		stmt.executeUpdate();
//		ResultSet rs = stmt.getGeneratedKeys();
//		if (key != null && rs.next()) 
//			return rs.getInt(key);
//		else
//			return -1;
//	}
	
	protected int saveWithId(String query, Object[] values) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		
		int count = 1;
		for(Object obj : values) {
			stmt.setObject(count, obj);
			count++;
		}
		
		stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if(rs.next()){
			int id = rs.getInt(1);
			conn.close();
			return id;
		}
		else {
			conn.close();
			return -1;
		}
	}

	protected void save(String query, Object[] values) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement stmt = conn.prepareStatement(query);
		
		int count = 1;
		for(Object obj : values) {
			stmt.setObject(count, obj);
			count++;
		}
		
		stmt.executeUpdate();
		conn.close();
	}
	
	protected Object read(String query, Object[] values) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement stmt = conn.prepareStatement(query);

		if(values != null) {
			int count = 1;
			for(Object obj : values) {
				stmt.setObject(count, obj);
				count++;
			}
		}
		
		ResultSet rs = stmt.executeQuery();
		Object obj = this.convertResult(rs);
		conn.close();
		return obj;

	}
	
	protected int count (String query, String searchText) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement stmt = conn.prepareStatement(query);
		if (searchText != null)
			stmt.setString(1, searchText);
		ResultSet rs = stmt.executeQuery();
		int result = 0;
		if (rs.next()) {
			result = rs.getInt(1);
		}
		conn.close();
		return result;
	}
	
	protected String setPageLimits(int pageNo, int pageSize, String query) {
		StringBuilder sb = new StringBuilder(query);
		sb.append(" LIMIT " + (pageNo - 1)*pageSize + "," + pageSize);
		return sb.toString();
	}
}
