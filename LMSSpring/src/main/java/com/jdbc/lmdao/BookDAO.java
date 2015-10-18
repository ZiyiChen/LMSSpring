/**
 * BookDAO.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.jdbc.lmdao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jdbc.lmdo.Author;
import com.jdbc.lmdo.Book;
import com.jdbc.lmdo.Genre;
import com.jdbc.lmdo.Publisher;

public class BookDAO extends BaseDAO{
	public void insert(Book book) throws SQLException {
		Integer pubId = book.getPublisher()==null?null:book.getPublisher().getPublisherId();
		int bookId = saveWithId(
				"insert into tbl_book (title, pubId) values (?,?)",
				new Object[] { book.getTitle(),
						pubId});
		if (book.getAuthors() != null){
			for (Author auth : book.getAuthors()) {
				save("insert into tbl_book_authors (bookId, authorId) values (?,?)",
						new Object[] { bookId, auth.getAuthorId() });
			}
		}
		
		if (book.getGenres() != null){
			for (Genre genre : book.getGenres()) {
				save("insert into tbl_book_genres (bookId, genre_id) values (?,?)",
						new Object[] { bookId, genre.getGenreId() });
			}
		}
		book.setBookId(bookId);
	}
	
	public void update(Book book) throws SQLException {
		Integer pubId = book.getPublisher()==null?null:book.getPublisher().getPublisherId();
		save("update tbl_book  set title = ?, pubId = ? where bookId = ?",
				new Object[] { book.getTitle(), pubId, book.getBookId() });
		//update tbl_book_authors table
		save("delete from tbl_book_authors where bookId = ?",
				new Object[] { book.getBookId() });
		if (book.getAuthors() != null){
			for (Author auth : book.getAuthors()) {
				save("insert into tbl_book_authors (bookId, authorId) values (?,?)",
						new Object[] { book.getBookId(), auth.getAuthorId() });
			}
		}
		//update tbl_book_genres table
		save("delete from tbl_book_genres where bookId = ?",
				new Object[] { book.getBookId() });
		if (book.getGenres() != null){
			for (Genre genre : book.getGenres()) {
				//System.out.println("in book save: "+ book.getBookId() + genre.getGenreId());
				save("insert into tbl_book_genres (bookId, genre_id) values (?,?)",
						new Object[] { book.getBookId(), genre.getGenreId() });
			}
		}
	}
	
	public void delete(Book book) throws SQLException {
		save("delete from tbl_book_authors where bookId = ?",
				new Object[] {book.getBookId()});
		save("delete from tbl_book_genres where bookId = ?",
				new Object[] {book.getBookId()});
		save("delete from tbl_book_loans where bookId = ?",
				new Object[] {book.getBookId()});
		save("delete from tbl_book_copies where bookId = ?",
				new Object[] {book.getBookId()});
		save("delete from tbl_book where bookId = ?",
				new Object[] {book.getBookId()});
	}
	
	public Book readOne(int bookId) throws SQLException {
		List<Book> books = (List<Book>) read("select * from tbl_book where bookId = ?",
				new Object[] {bookId});
		if (books != null && books.size() > 0) {
			return books.get(0);
		}else {
			return null;
		}
	}
	
	public List<Book> readAll () throws SQLException {
		return (List<Book>) read ("select * from tbl_book", null);
	}
	
	public List<Book> readAllByAuthor (Author auth) throws SQLException {
		return (List<Book>) read ("select * from tbl_book where bookId in (select bookId from tbl_book_authors where authorId = ?)",
				new Object[] {auth.getAuthorId()});
	}
	public List<Book> readAllByGenre (Genre genre) throws SQLException {
		return (List<Book>) read ("select * from tbl_book where bookId in (select bookId from tbl_book_genres where genre_id = ?)",
				new Object[] {genre.getGenreId()});
	}
	
	public List<Book> readAllByPublisher (Publisher pub) throws SQLException {
		return (List<Book>) read ("select * from tbl_book where pubId = ?)",
				new Object[] {pub.getPublisherId()});
	}
	
	@Override
	protected Object convertResult(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<Book> ();
		while (rs.next()) {
			Book bk = new Book();
			bk.setBookId(rs.getInt("bookId"));
			bk.setTitle(rs.getString("title"));
			PublisherDAO pubDAO = new PublisherDAO();
			bk.setPublisher(pubDAO.readOne(rs.getInt("pubId")));
			books.add(bk);
		}
		return books;
	}
	
	public int countBooks(String searchText) throws SQLException {
		searchText = '%' + searchText + '%';
		return count ("select count(*) from tbl_book where title like ?", searchText);
	}
	
	public List<Book> searchSizedBooks(int pageNo, int pageSize, String searchText) throws SQLException {
		searchText = '%' + searchText + '%';
		return (List<Book>) read (setPageLimits(pageNo, pageSize, "select * from tbl_book where title like ?"),
				new Object[] {searchText});
	}
}
