/**
 * Book.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.jdbc.lmdo;

import java.sql.SQLException;
import java.util.List;

//import com.jdbc.lmdao.AuthorDAO;
//import com.jdbc.lmdao.BookCopiesDAO;
//import com.jdbc.lmdao.BookLoansDAO;
//import com.jdbc.lmdao.GenreDAO;

public class Book {
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bookId;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (bookId != other.bookId)
			return false;
		return true;
	}
	private int bookId;
	private String title;
	private Publisher publisher;
	private List<Genre> genres;
	private List<Author> authors;
	private List<BookCopies> copies;
	private List<BookLoans> loans;
	/**
	 * @return the bookId
	 */
	public int getBookId() {
		return bookId;
	}
	/**
	 * @param bookId the bookId to set
	 */
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the publisher
	 */
	public Publisher getPublisher() {
		return publisher;
	}
	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}
	/**
	 * @return the genres
	 * @throws SQLException 
	 */
	public List<Genre> getGenres() throws SQLException {
//		if (genres == null) {
//			GenreDAO gDAO = new GenreDAO();
//			genres = gDAO.readAllByBook(this);
//		}
		return genres;
	}
	/**
	 * @param genres the genres to set
	 */
	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}
	/**
	 * @return the authors
	 * @throws SQLException 
	 */
	public List<Author> getAuthors() throws SQLException {
//		if (authors == null) {
//			AuthorDAO aDAO = new AuthorDAO();
//			authors = aDAO.readAllByBook(this);
//		}
		return authors;
	}
	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	/**
	 * @return the copies
	 * @throws SQLException 
	 */
	public List<BookCopies> getCopies() throws SQLException {
//		if (copies == null) {
//			BookCopiesDAO bcDAO = new BookCopiesDAO();
//			copies = bcDAO.readAllByBook(this);
//		}
		return copies;
	}
	/**
	 * @param copies the copies to set
	 */
	public void setCopies(List<BookCopies> copies) {
		this.copies = copies;
	}
	/**
	 * @return the loans
	 * @throws SQLException 
	 */
	public List<BookLoans> getLoans() throws SQLException {
//		if (loans == null) {
//			BookLoansDAO blDAO = new BookLoansDAO();
//			loans = blDAO.readAllByBook(this);
//		}
		return loans;
	}
	/**
	 * @param loans the loans to set
	 */
	public void setLoans(List<BookLoans> loans) {
		this.loans = loans;
	}
	
	
}
