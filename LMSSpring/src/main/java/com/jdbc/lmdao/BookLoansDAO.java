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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.jdbc.lmdo.Book;
import com.jdbc.lmdo.BookLoans;
import com.jdbc.lmdo.Borrower;
import com.jdbc.lmdo.Branch;

public class BookLoansDAO extends BaseDAO implements ResultSetExtractor<List<BookLoans>>{
	
	@Autowired
	BookDAO bkDAO;
	
	@Autowired
	BranchDAO bhDAO;
	
	@Autowired
	BorrowerDAO brDAO;
	
	
	/**
	 * @param temp
	 */
	public BookLoansDAO(JdbcTemplate temp) {
		super(temp);
	}

	public void insert (BookLoans bl) throws SQLException {
		template.update("insert into tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate, dateIn) values (?, ?, ?, ?, ?, ?)",
				new Object[]{bl.getBook().getBookId(), bl.getBranch().getBranchId(), bl.getBorrower().getCardNo(), bl.getDateOut(), bl.getDueDate(), bl.getDateIn()});
	}
	
	public void update (BookLoans bl) throws SQLException {
		template.update("update tbl_book_loans set dateOut = ?, dueDate = ?, dateIn = ? where bookId = ? and branchId = ? and cardNo = ?",
				new Object[]{bl.getDateOut(), bl.getDueDate(), bl.getDateIn(), bl.getBook().getBookId(), bl.getBranch().getBranchId(), bl.getBorrower().getCardNo()});
	}
	
	public void delete (BookLoans bl) throws SQLException {
		template.update("delete from tbl_book_loans where bookId = ? and branchId = ? and cardNo = ?",
				new Object[]{bl.getBook().getBookId(), bl.getBranch().getBranchId(), bl.getBorrower().getCardNo()});
	}
	
	public BookLoans readOne (Book bk, Branch bh, Borrower br) throws SQLException {
		List<BookLoans> lns = template.query (
				"select * from tbl_book_loans where bookId = ? and branchId = ? and cardNo = ?",
				new Object[]{bk.getBookId(), bh.getBranchId(), br.getCardNo()}, this);
		if (lns != null && lns.size() > 0)
			return lns.get(0);
		else
			return null;
	}
	
	public List<BookLoans> readAll () throws SQLException {
		return template.query (
				"select * from tbl_book_loans",
				this);
	}
	
	public List<BookLoans> readAllByBook (Book bk) throws SQLException {
		return template.query (
				"select * from tbl_book_loans where bookId = ?",
				new Object[]{bk.getBookId()}, this);
	}
	
	public List<BookLoans> readAllByBranch (Branch bh) throws SQLException {
		return template.query (
				"select * from tbl_book_loans where branchId = ?",
				new Object[]{bh.getBranchId()}, this);
	}
	
	public List<BookLoans> readAllByCard (Borrower br) throws SQLException {
		return template.query (
				"select * from tbl_book_loans where cardNo = ?",
				new Object[]{br.getCardNo()}, this);
	}

	@Override
	public List<BookLoans> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<BookLoans> bls = new ArrayList<BookLoans> ();
		while (rs.next()) {
			BookLoans bl = new BookLoans();
			bl.setBook(bkDAO.readOne(rs.getInt("bookId")));
			bl.setBranch(bhDAO.readOne(rs.getInt("branchId")));
			bl.setBorrower(brDAO.readOne(rs.getInt("cardNo")));
			bl.setDateOut(rs.getDate("dateOut"));
			bl.setDueDate(rs.getDate("dueDate"));
			bl.setDateIn(rs.getDate("dateIn"));
			bls.add(bl);
		}
		return bls;
	}

	public List<BookLoans> sizedBookLoans(int pageNo, int pageSize) throws SQLException {
		return template.query (setPageLimits(pageNo, pageSize, "select * from tbl_book_loans"),
				this);
	}

	public int countBookLoans() throws SQLException {
		return template.queryForObject("select count(*) from tbl_book_loans", Integer.class);
	}

	public void overrideDueDate(BookLoans bl) throws SQLException {
		template.update("update tbl_book_loans set dueDate = ? where bookId = ? and branchId = ? and cardNo = ?",
				new Object[]{bl.getDueDate(), bl.getBook().getBookId(), bl.getBranch().getBranchId(), bl.getBorrower().getCardNo()});
	} 
	
	
}
