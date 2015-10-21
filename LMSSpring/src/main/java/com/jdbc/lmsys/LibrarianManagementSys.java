/**
 * LibrarianManagementSys.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.jdbc.lmsys;



import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jdbc.lmdao.BookCopiesDAO;
import com.jdbc.lmdao.BookDAO;
import com.jdbc.lmdao.BranchDAO;
import com.jdbc.lmdo.Book;
import com.jdbc.lmdo.BookCopies;
import com.jdbc.lmdo.Branch;

public class LibrarianManagementSys {

	@Autowired
	BranchDAO bhDAO;
	
	@Autowired
	BookDAO bkDAO;
	
	@Autowired
	BookCopiesDAO bcDAO;
	
	public List<Branch> getAllBranches() throws SQLException {
		return bhDAO.readAll();
	}

	public void updateBranch(Branch branch) throws SQLException {
		bhDAO.update(branch);
	}

	public List<Book> getAllBooks () throws SQLException {
		return bkDAO.readAll();
	}

	public BookCopies getBookCopies (Book bk, Branch bh) throws SQLException {
		return bcDAO.readOne(bk, bh);
	}

	public void updateBookCopies (BookCopies bc) throws SQLException {
		bcDAO.delete(bc);
		bcDAO.insert(bc);
	}
}
