/**
 * Branch.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.jdbc.lmdo;

import java.sql.SQLException;
import java.util.List;

//import com.jdbc.lmdao.BookCopiesDAO;
//import com.jdbc.lmdao.BookLoansDAO;

public class Branch {
	private int branchId;
	private String name;
	private String address;
	private List<BookLoans> loans;
	private List<BookCopies> copies;
	/**
	 * @return the branchId
	 */
	public int getBranchId() {
		return branchId;
	}
	/**
	 * @param branchId the branchId to set
	 */
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the loans
	 * @throws SQLException 
	 */
	public List<BookLoans> getLoans() throws SQLException {
//		if (loans == null) {
//			BookLoansDAO blDAO = new BookLoansDAO();
//			loans = blDAO.readAllByBranch(this);
//		}
		return loans;
	}
	/**
	 * @param loans the loans to set
	 */
	public void setLoans(List<BookLoans> loans) {
		this.loans = loans;
	}
	/**
	 * @return the copies
	 * @throws SQLException 
	 */
	public List<BookCopies> getCopies() throws SQLException {
//		if (copies == null) {
//			BookCopiesDAO bcDAO = new BookCopiesDAO();
//			copies = bcDAO.readAllByBranch(this);
//		}
		return copies;
	}
	/**
	 * @param copies the copies to set
	 */
	public void setCopies(List<BookCopies> copies) {
		this.copies = copies;
	}
	
	
}
