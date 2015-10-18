/**
 * BookLoansDAO.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.jdbc.lmdao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jdbc.lmdo.Book;
import com.jdbc.lmdo.BookLoans;
import com.jdbc.lmdo.Borrower;
import com.jdbc.lmdo.Branch;

public class BookLoansDAO extends BaseDAO{
	public void insert (BookLoans bl) throws SQLException {
		save("insert into tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate, dateIn) values (?, ?, ?, ?, ?, ?)",
				new Object[]{bl.getBook().getBookId(), bl.getBranch().getBranchId(), bl.getBorrower().getCardNo(), bl.getDateOut(), bl.getDueDate(), bl.getDateIn()});
	}
	
	public void update (BookLoans bl) throws SQLException {
		save("update tbl_book_loans set dateOut = ?, dueDate = ?, dateIn = ? where bookId = ? and branchId = ? and cardNo = ?",
				new Object[]{bl.getDateOut(), bl.getDueDate(), bl.getDateIn(), bl.getBook().getBookId(), bl.getBranch().getBranchId(), bl.getBorrower().getCardNo()});
	}
	
	public void delete (BookLoans bl) throws SQLException {
		save("delete from tbl_book_loans where bookId = ? and branchId = ? and cardNo = ?",
				new Object[]{bl.getBook().getBookId(), bl.getBranch().getBranchId(), bl.getBorrower().getCardNo()});
	}
	
	public BookLoans readOne (Book bk, Branch bh, Borrower br) throws SQLException {
		List<BookLoans> lns = (List<BookLoans>) read (
				"select * from tbl_book_loans where bookId = ? and branchId = ? and cardNo = ?",
				new Object[]{bk.getBookId(), bh.getBranchId(), br.getCardNo()});
		if (lns != null && lns.size() > 0)
			return lns.get(0);
		else
			return null;
	}
	
	public List<BookLoans> readAll () throws SQLException {
		return (List<BookLoans>) read (
				"select * from tbl_book_loans",
				null);
	}
	
	public List<BookLoans> readAllByBook (Book bk) throws SQLException {
		return (List<BookLoans>) read (
				"select * from tbl_book_loans where bookId = ?",
				new Object[]{bk.getBookId()});
	}
	
	public List<BookLoans> readAllByBranch (Branch bh) throws SQLException {
		return (List<BookLoans>) read (
				"select * from tbl_book_loans where branchId = ?",
				new Object[]{bh.getBranchId()});
	}
	
	public List<BookLoans> readAllByCard (Borrower br) throws SQLException {
		return (List<BookLoans>) read (
				"select * from tbl_book_loans where cardNo = ?",
				new Object[]{br.getCardNo()});
	}

	@Override
	protected Object convertResult(ResultSet rs) throws SQLException {
		List<BookLoans> bls = new ArrayList<BookLoans> ();
		while (rs.next()) {
			BookLoans bl = new BookLoans();
			BookDAO bkDAO = new BookDAO();
			bl.setBook(bkDAO.readOne(rs.getInt("bookId")));
			BranchDAO bhDAO = new BranchDAO();
			bl.setBranch(bhDAO.readOne(rs.getInt("branchId")));
			BorrowerDAO brDAO = new BorrowerDAO();
			bl.setBorrower(brDAO.readOne(rs.getInt("cardNo")));
			bl.setDateOut(rs.getDate("dateOut"));
			bl.setDueDate(rs.getDate("dueDate"));
			bl.setDateIn(rs.getDate("dateIn"));
			bls.add(bl);
		}
		return bls;
	}

	public List<BookLoans> sizedBookLoans(int pageNo, int pageSize) throws SQLException {
		return (List<BookLoans>) read (setPageLimits(pageNo, pageSize, "select * from tbl_book_loans"),
				null);
	}

	public int countBookLoans() throws SQLException {
		return count ("select count(*) from tbl_book_loans", null);
	}

	public void overrideDueDate(BookLoans bl) throws SQLException {
		save("update tbl_book_loans set dueDate = ? where bookId = ? and branchId = ? and cardNo = ?",
				new Object[]{bl.getDueDate(), bl.getBook().getBookId(), bl.getBranch().getBranchId(), bl.getBorrower().getCardNo()});
	} 
	
	
}
