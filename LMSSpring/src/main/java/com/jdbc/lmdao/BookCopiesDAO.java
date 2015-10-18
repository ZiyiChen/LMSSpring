/**
 * BookCopiesDAO.java
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
import com.jdbc.lmdo.BookCopies;
import com.jdbc.lmdo.Branch;

public class BookCopiesDAO extends BaseDAO {
	public void insert (BookCopies cpy) throws SQLException {
		save("insert into tbl_book_copies (bookId, branchId, noOfCopies) values (?, ?, ?)",
				new Object[]{cpy.getBook().getBookId(), cpy.getBranch().getBranchId(), cpy.getOnOfCopies()});
	}
	
	public void update (BookCopies cpy) throws SQLException {
		save("update tbl_book_copies set noOfCopies = ? where bookId = ? and branchId = ?",
				new Object[]{cpy.getOnOfCopies(), cpy.getBook().getBookId(), cpy.getBranch().getBranchId()});
	}
	
	public void delete (BookCopies cpy) throws SQLException {
		save("delete from tbl_book_copies where bookId = ? and branchId = ?",
				new Object[]{cpy.getBook().getBookId(), cpy.getBranch().getBranchId()});
	}
	
	public BookCopies readOne (Book bk, Branch bh) throws SQLException {
		List<BookCopies> cpys = (List<BookCopies>) read(
				"select * from tbl_book_copies where bookId = ? and branchId = ?",
				new Object[]{bk.getBookId(), bh.getBranchId()});
		if (cpys != null && cpys.size() > 0)
			return cpys.get(0);
		else
			return null;
	}
	
	public List<BookCopies> readAll () throws SQLException {
		return (List<BookCopies>) read ("select * from tbl_book_copies", null);
	}
	
	public List<BookCopies> readAllByBook (Book bk) throws SQLException {
		return (List<BookCopies>) read (
				"select * from tbl_book_copies where bookId = ?", 
				new Object[]{bk.getBookId()});
	}
	
	public List<BookCopies> readAllByBranch (Branch bh) throws SQLException {
		return (List<BookCopies>) read (
				"select * from tbl_book_copies where branchId = ?", 
				new Object[]{bh.getBranchId()});
	}


	@Override
	protected Object convertResult(ResultSet rs) throws SQLException {
		List<BookCopies> cpys = new ArrayList<BookCopies>();
		while(rs.next()) {
			BookCopies cpy = new BookCopies();
			BookDAO bkDAO = new BookDAO();
			cpy.setBook(bkDAO.readOne(rs.getInt("bookId")));
			BranchDAO bhDAO = new BranchDAO();
			cpy.setBranch(bhDAO.readOne(rs.getInt("branchId")));
			cpy.setOnOfCopies(rs.getInt("noOfCopies"));
			cpys.add(cpy);
		}
		return cpys;
	}
}
