/**
 * BranchDAO.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.jdbc.lmdao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.jdbc.lmdo.Branch;

public class BranchDAO extends BaseDAO implements
ResultSetExtractor<List<Branch>>{
	/**
	 * @param temp
	 */
	public BranchDAO(JdbcTemplate temp) {
		super(temp);
	}

	public void insert(Branch bh) throws SQLException {
		template.update("insert into tbl_library_branch (branchName, branchAddress) values (?, ?)",
				new Object[]{bh.getName(), bh.getAddress()});
	}
	
	public void update (Branch branch) throws SQLException {
		template.update("update tbl_library_branch set branchName = ?, branchAddress = ? where branchId = ?",
				new Object[] {branch.getName(), branch.getAddress(), branch.getBranchId()});
	}
	
	public void delete (Branch branch) throws SQLException {
		template.update("delete from tbl_book_copies where branchId = ?",
				new Object[]{branch.getBranchId()});
		template.update("delete from tbl_book_loans where branchId = ?",
				new Object[]{branch.getBranchId()});
		template.update("delete from tbl_library_branch where branchId = ?",
				new Object[]{branch.getBranchId()});
	}
	
	public Branch readOne (int branchId) throws SQLException {
		List<Branch> bhs = template.query("select * from tbl_library_branch where branchId = ?",
				new Object[]{branchId}, this);
		if (bhs != null && bhs.size() > 0)
			return bhs.get(0);
		else
			return null;
	}
	
	public List<Branch> readAll() throws SQLException {
		return template.query("select * from tbl_library_branch",
				this);
	}
	
	@Override
	public List<Branch> extractData(ResultSet rs) throws SQLException, DataAccessException{
		List<Branch> bhs = new ArrayList<Branch>();
		while(rs.next()) {
			Branch bh = new Branch();
			bh.setBranchId(rs.getInt("branchId"));
			bh.setName(rs.getString("branchName"));
			bh.setAddress(rs.getString("branchAddress"));
			bhs.add(bh);
		}
		return bhs;
	}
}
