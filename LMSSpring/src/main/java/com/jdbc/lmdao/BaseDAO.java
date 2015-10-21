/**
 * BaseDAO.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.jdbc.lmdao;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class BaseDAO {
	protected JdbcTemplate template;
	
	public BaseDAO(JdbcTemplate temp) {
		this.template = temp;
	}
	
	protected String setPageLimits(int pageNo, int pageSize, String query) {
		StringBuilder sb = new StringBuilder(query);
		sb.append(" LIMIT " + (pageNo - 1)*pageSize + "," + pageSize);
		return sb.toString();
	}
}
