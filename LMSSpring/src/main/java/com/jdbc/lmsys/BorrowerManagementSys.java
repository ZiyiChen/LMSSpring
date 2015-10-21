/**
 * BorrowerManagementSys.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.jdbc.lmsys;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.jdbc.lmdao.BookCopiesDAO;
import com.jdbc.lmdao.BookDAO;
import com.jdbc.lmdao.BookLoansDAO;
import com.jdbc.lmdao.BorrowerDAO;
import com.jdbc.lmdao.BranchDAO;
import com.jdbc.lmdo.Book;
import com.jdbc.lmdo.BookCopies;
import com.jdbc.lmdo.BookLoans;
import com.jdbc.lmdo.Borrower;
import com.jdbc.lmdo.Branch;

public class BorrowerManagementSys {
	
	@Autowired
	BorrowerDAO brDAO;
	
	@Autowired
	BookDAO bkDAO;
	
	@Autowired
	BranchDAO bhDAO;
	
	@Autowired
	BookLoansDAO blDAO;
	
	@Autowired
	BookCopiesDAO bcDAO;

	public List<Branch> getAllBranches() throws SQLException {
		return bhDAO.readAll();
	}

	public List<BookCopies> getAllAvailableBookCopies (Branch bh, Borrower br) throws SQLException {
		List<BookCopies> bcs = bcDAO.readAllByBranch(bh);
		List<BookCopies> res = new ArrayList<BookCopies>();

		List<BookLoans> bls = blDAO.readAllByCard(br);
		List<Book> bks = new ArrayList<Book>();
		for (BookLoans bl : bls) {
			if (bl.getDateIn() != null) {
				bks.add(bl.getBook());
			}
		}
		for (BookCopies bc : bcs) {
			if (bc.getOnOfCopies() > 0 && !bks.contains(bc.getBook())) {
				res.add(bc);
			}
		}
		return res;
	}

	@Transactional
	public void checkOut (BookCopies bc, Borrower br) throws SQLException {
		BookLoans bl = new BookLoans();
		bl.setBook(bc.getBook());
		bl.setBranch(bc.getBranch());
		bl.setBorrower(br);
		Calendar cal = Calendar.getInstance();
		bl.setDateOut(cal.getTime());
		cal.add(Calendar.DATE, 7);
		bl.setDueDate(cal.getTime());
		blDAO.delete(bl);
		blDAO.insert(bl);

		bc.setOnOfCopies(bc.getOnOfCopies() - 1);
		bcDAO.update(bc);
	}

	public List<BookLoans> getAllAvailableBookLoans (Borrower br, Branch bh) throws SQLException {
		List<BookLoans> bls = blDAO.readAllByCard(br);
		List<BookLoans> res = new ArrayList<BookLoans>();
		for (BookLoans bl : bls) {
			if(bl.getBranch().getBranchId() == bh.getBranchId() && bl.getDateIn() == null) {
				res.add(bl);
			}
		}
		return res;
	}

	@Transactional
	public void returnBook (BookLoans bl) throws SQLException {
		BookCopies bc = bcDAO.readOne(bl.getBook(), bl.getBranch());
		bc.setOnOfCopies(bc.getOnOfCopies() + 1);
		bcDAO.update(bc);

		bl.setDateIn(new Date());
		blDAO.delete(bl);
		blDAO.insert(bl);
	}

	public Borrower getBorrower (int id) throws SQLException {
		return brDAO.readOne(id);
	}
}
