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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.jdbc.lmdo.Book;
import com.jdbc.lmdo.BookCopies;
import com.jdbc.lmdo.Branch;

public class BookCopiesDAO extends BaseDAO implements ResultSetExtractor<List<BookCopies>>{
	/**
	 * @param temp
	 */
	
	@Autowired
	BookDAO bkDAO;
	
	@Autowired
	BranchDAO bhDAO;

	public BookCopiesDAO(JdbcTemplate temp) {
		super(temp);
	}

	public void insert (BookCopies cpy) throws SQLException {
		template.update("insert into tbl_book_copies (bookId, branchId, noOfCopies) values (?, ?, ?)",
				new Object[]{cpy.getBook().getBookId(), cpy.getBranch().getBranchId(), cpy.getOnOfCopies()});
	}

	public void update (BookCopies cpy) throws SQLException {
		template.update("update tbl_book_copies set noOfCopies = ? where bookId = ? and branchId = ?",
				new Object[]{cpy.getOnOfCopies(), cpy.getBook().getBookId(), cpy.getBranch().getBranchId()});
	}

	public void delete (BookCopies cpy) throws SQLException {
		template.update("delete from tbl_book_copies where bookId = ? and branchId = ?",
				new Object[]{cpy.getBook().getBookId(), cpy.getBranch().getBranchId()});
	}

	public BookCopies readOne (Book bk, Branch bh) throws SQLException {
		List<BookCopies> cpys = template.query(
				"select * from tbl_book_copies where bookId = ? and branchId = ?",
				new Object[]{bk.getBookId(), bh.getBranchId()}, this);
		if (cpys != null && cpys.size() > 0)
			return cpys.get(0);
		else
			return null;
	}

	public List<BookCopies> readAll () throws SQLException {
		return template.query ("select * from tbl_book_copies", this);
	}

	public List<BookCopies> readAllByBook (Book bk) throws SQLException {
		return template.query (
				"select * from tbl_book_copies where bookId = ?", 
				new Object[]{bk.getBookId()}, this);
	}

	public List<BookCopies> readAllByBranch (Branch bh) throws SQLException {
		return template.query (
				"select * from tbl_book_copies where branchId = ?", 
				new Object[]{bh.getBranchId()}, this);
	}


	@Override
	public List<BookCopies> extractData(ResultSet rs) throws SQLException,
	DataAccessException{
		List<BookCopies> cpys = new ArrayList<BookCopies>();
		while(rs.next()) {
			BookCopies cpy = new BookCopies();
			cpy.setBook(bkDAO.readOne(rs.getInt("bookId")));
			cpy.setBranch(bhDAO.readOne(rs.getInt("branchId")));
			cpy.setOnOfCopies(rs.getInt("noOfCopies"));
			cpys.add(cpy);
		}
		return cpys;
	}
}
