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

import com.jdbc.lmdo.Branch;

public class BranchDAO extends BaseDAO{
	public void insert(Branch bh) throws SQLException {
		int branchId = saveWithId("insert into tbl_library_branch (branchName, branchAddress) values (?, ?)",
				new Object[]{bh.getName(), bh.getAddress()});
		bh.setBranchId(branchId);
	}
	
	public void update (Branch branch) throws SQLException {
		save("update tbl_library_branch set branchName = ?, branchAddress = ? where branchId = ?",
				new Object[] {branch.getName(), branch.getAddress(), branch.getBranchId()});
	}
	
	public void delete (Branch branch) throws SQLException {
		save("delete from tbl_book_copies where branchId = ?",
				new Object[]{branch.getBranchId()});
		save("delete from tbl_book_loans where branchId = ?",
				new Object[]{branch.getBranchId()});
		save("delete from tbl_library_branch where branchId = ?",
				new Object[]{branch.getBranchId()});
	}
	
	public Branch readOne (int branchId) throws SQLException {
		List<Branch> bhs = (List<Branch>) read("select * from tbl_library_branch where branchId = ?",
				new Object[]{branchId});
		if (bhs != null && bhs.size() > 0)
			return bhs.get(0);
		else
			return null;
	}
	
	public List<Branch> readAll() throws SQLException {
		return (List<Branch>) read("select * from tbl_library_branch",
				null);
	}
	
	@Override
	protected Object convertResult(ResultSet rs) throws SQLException {
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
