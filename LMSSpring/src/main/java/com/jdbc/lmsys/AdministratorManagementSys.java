/**
 * AdministratorManagementSys.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.jdbc.lmsys;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdbc.lmdao.AuthorDAO;
import com.jdbc.lmdao.BookDAO;
import com.jdbc.lmdao.BookLoansDAO;
import com.jdbc.lmdao.BorrowerDAO;
import com.jdbc.lmdao.BranchDAO;
import com.jdbc.lmdao.GenreDAO;
import com.jdbc.lmdao.PublisherDAO;
import com.jdbc.lmdo.Author;
import com.jdbc.lmdo.Book;
import com.jdbc.lmdo.BookLoans;
import com.jdbc.lmdo.Borrower;
import com.jdbc.lmdo.Branch;
import com.jdbc.lmdo.Genre;
import com.jdbc.lmdo.Publisher;

@Service
public class AdministratorManagementSys {
	@Autowired
	BookDAO bkDAO;
	
	@Autowired
	AuthorDAO auDAO;
	
	@Autowired
	GenreDAO genDAO;
	
	@Autowired
	BookLoansDAO blDAO;
	
	@Autowired
	PublisherDAO pubDAO;
	
	@Autowired
	BranchDAO bhDAO;
	
	@Autowired
	BorrowerDAO brDAO;

	public List<Book> getAllBooks () throws SQLException {
		return bkDAO.readAll();
	}
	
	public List<Book> getAllFullLoadBooks () throws SQLException {
		List<Book> bks = bkDAO.readAll();
		for (Book bk : bks) {
			bk.setAuthors(auDAO.readAllByBook(bk));
			bk.setGenres(genDAO.readAllByBook(bk));
		}
		return bks;
	}
	
	public Book getBookById (int id) throws SQLException {
		return bkDAO.readOne(id);
	}
	
	public Book getFullLoadBookById (int id) throws SQLException {
		Book bk = bkDAO.readOne(id);
		bk.setAuthors(auDAO.readAllByBook(bk));
		bk.setGenres(genDAO.readAllByBook(bk));
		return bk;
	}
	
	public List<Author> getAllValidAuthorsByBook(Book bk) throws SQLException {
		List<Author> res = new ArrayList<Author>();
		List<Author> addedAths = auDAO.readAllByBook(bk);
		bk.setAuthors(addedAths);
		res = auDAO.readAll();
		res.removeAll(addedAths);
		return res;
	}
	
	public List<Genre> getAllValidGenresByBook(Book bk) throws SQLException {
		List<Genre> res = new ArrayList<Genre>();
		List<Genre> addedGens = genDAO.readAllByBook(bk);
		bk.setGenres(addedGens);
		res = genDAO.readAll();
		res.removeAll(addedGens);
		return res;
	}
	
	public List<Publisher> getAllValidPublishersByBook(Book bk) throws SQLException {
		List<Publisher> res = pubDAO.readAll();
		res.remove(bk.getPublisher());
		return res;
	}
	
	@Transactional
	public void updateBook (Book bk) throws SQLException {
		bkDAO.update(bk);
	}
	
	@Transactional
	public void deleteBook (Book bk) throws SQLException {
		bkDAO.delete(bk);
	}
	
	@Transactional
	public void insertBook (Book bk) throws SQLException {
		bkDAO.insert(bk);
	}
	
	public List<Publisher> getAllPublishers () throws SQLException {
		return pubDAO.readAll();
	}
	
	public Publisher getPublisherById (int id) throws SQLException {
		return pubDAO.readOne(id);
	}
	
	@Transactional
	public void updatePublisher (Publisher pub) throws SQLException {
		pubDAO.update(pub);
	}
	
	@Transactional
	public void deletePublisher (Publisher pub) throws SQLException {
		pubDAO.delete(pub);
	}
	
	@Transactional
	public void insertPublisher (Publisher pub) throws SQLException {
		pubDAO.insert(pub);
	}
	
	public List<Author> getAllAuthors () throws SQLException {
		return auDAO.readAll();
	}
	
	public Author getAuthorById (int id) throws SQLException {
		return auDAO.readOne(id);
	}
	
	@Transactional
	public void updateAuthor (Author auth) throws SQLException {
		auDAO.update(auth);
	}
	
	@Transactional
	public void deleteAuthor (Author auth) throws SQLException {
		auDAO.delete(auth);
	}
	
	@Transactional
	public void insertAuthor (Author auth) throws SQLException {
		auDAO.insert(auth);
	}
	
	public List<Branch> getAllBranch () throws SQLException {
		return bhDAO.readAll();
	}
	
	@Transactional
	public void updateBranch (Branch bh) throws SQLException {
		bhDAO.update(bh);
	}
	
	@Transactional
	public void deleteBranch (Branch bh) throws SQLException {
		bhDAO.delete(bh);
	}
	
	@Transactional
	public void insertBranch (Branch bh) throws SQLException {
		bhDAO.insert(bh);
	}
	
	public List<Borrower> getAllBorrower () throws SQLException {
		return brDAO.readAll();
	}
	
	@Transactional
	public void updateBorrower (Borrower br) throws SQLException {
		brDAO.update(br);
	}
	
	@Transactional
	public void deleteBorrower (Borrower br) throws SQLException {
		brDAO.delete(br);
	}
	
	@Transactional
	public void insertBorrower (Borrower br) throws SQLException {
		brDAO.insert(br);
	}
	
	public List<BookLoans> getAllBookLoans () throws SQLException {
		return blDAO.readAll();
	}
	
	public List<Genre> getAllGenres () throws SQLException {
		return genDAO.readAll();
	}
	
	public Genre getGenreById (int id) throws SQLException {
		return genDAO.readOne(id);
	}

	public List<Book> searchSizedFullLoadBooks(int pageNo, int pageSize, String searchText) throws SQLException {
		List<Book> bks = bkDAO.searchSizedBooks(pageNo, pageSize, searchText);
		for (Book bk : bks) {
			bk.setAuthors(auDAO.readAllByBook(bk));
			bk.setGenres(genDAO.readAllByBook(bk));
		}
		return bks;
	}
	
	public List<Book> getSizedFullLoadBooks() throws SQLException {
		List<Book> bks = bkDAO.readAll();
		for (Book bk : bks) {
			bk.setAuthors(auDAO.readAllByBook(bk));
			bk.setGenres(genDAO.readAllByBook(bk));
		}
		return bks;
	}
	
	public int countBook (String searchText) throws SQLException {
		return bkDAO.countBooks(searchText);
	}
	
	public List<Author> searchAuthors(int pageNo, int pageSize, String search) throws SQLException {
		return auDAO.searchSizedAuthors(pageNo, pageSize, search);
	}
	public int countAuthor(String search) throws SQLException {
		return auDAO.countAuthors(search);
	}

	public List<Publisher> searchPublishers(int pageNo, int pageSize,
			String search) throws SQLException {
		return pubDAO.searchSizedPublishers(pageNo, pageSize, search);
	}

	public int countPublisher(String search) throws SQLException {
		return pubDAO.countPublishers(search);
	}

	public List<BookLoans> pageBookLoans(int pageNo, int pageSize) throws SQLException {
		return blDAO.sizedBookLoans(pageNo, pageSize);
	}

	public int countBookLoans() throws SQLException {
		return blDAO.countBookLoans();
	}

	@Transactional
	public void overrideDueDate(BookLoans bl) throws SQLException {
		blDAO.overrideDueDate(bl);
	}
}
