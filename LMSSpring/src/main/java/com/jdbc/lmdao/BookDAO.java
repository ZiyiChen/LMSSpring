/**
 * BookDAO.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.jdbc.lmdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.jdbc.lmdo.Author;
import com.jdbc.lmdo.Book;
import com.jdbc.lmdo.Genre;
import com.jdbc.lmdo.Publisher;

public class BookDAO extends BaseDAO implements ResultSetExtractor<List<Book>>{

	@Autowired
	PublisherDAO pubDAO;

	public BookDAO (JdbcTemplate temp) {
		super(temp);
	}

	public void insert(final Book book) throws SQLException {
		final Integer pubId = book.getPublisher()==null?null:book.getPublisher().getPublisherId();
		KeyHolder holder = new GeneratedKeyHolder();
		template.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = null;
				if (pubId != null) {
					ps = connection.prepareStatement(
							"insert into tbl_book (title, pubId) values (?,?)",
							Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, book.getTitle());
					ps.setInt(2, pubId);
				} else {
					ps = connection.prepareStatement(
							"insert into tbl_book (title) values (?)",
							Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, book.getTitle());
				}
				return ps;
			}
		}, holder);

		int bookId = holder.getKey().intValue();

		if (book.getAuthors() != null){
			for (Author auth : book.getAuthors()) {
				template.update("insert into tbl_book_authors (bookId, authorId) values (?,?)",
						new Object[] { bookId, auth.getAuthorId() });
			}
		}

		if (book.getGenres() != null){
			for (Genre genre : book.getGenres()) {
				template.update("insert into tbl_book_genres (bookId, genre_id) values (?,?)",
						new Object[] { bookId, genre.getGenreId() });
			}
		}
		book.setBookId(bookId);
	}

	public void update(Book book) throws SQLException {
		Integer pubId = book.getPublisher()==null?null:book.getPublisher().getPublisherId();
		template.update("update tbl_book  set title = ?, pubId = ? where bookId = ?",
				new Object[] { book.getTitle(), pubId, book.getBookId() });
		//update tbl_book_authors table
		template.update("delete from tbl_book_authors where bookId = ?",
				new Object[] { book.getBookId() });
		if (book.getAuthors() != null){
			for (Author auth : book.getAuthors()) {
				template.update("insert into tbl_book_authors (bookId, authorId) values (?,?)",
						new Object[] { book.getBookId(), auth.getAuthorId() });
			}
		}
		//update tbl_book_genres table
		template.update("delete from tbl_book_genres where bookId = ?",
				new Object[] { book.getBookId() });
		if (book.getGenres() != null){
			for (Genre genre : book.getGenres()) {
				//System.out.println("in book save: "+ book.getBookId() + genre.getGenreId());
				template.update("insert into tbl_book_genres (bookId, genre_id) values (?,?)",
						new Object[] { book.getBookId(), genre.getGenreId() });
			}
		}
	}

	public void delete(Book book) throws SQLException {
		template.update("delete from tbl_book_authors where bookId = ?",
				new Object[] {book.getBookId()});
		template.update("delete from tbl_book_genres where bookId = ?",
				new Object[] {book.getBookId()});
		template.update("delete from tbl_book_loans where bookId = ?",
				new Object[] {book.getBookId()});
		template.update("delete from tbl_book_copies where bookId = ?",
				new Object[] {book.getBookId()});
		template.update("delete from tbl_book where bookId = ?",
				new Object[] {book.getBookId()});
	}

	public Book readOne(int bookId) throws SQLException {
		List<Book> books = template.query("select * from tbl_book where bookId = ?",
				new Object[] {bookId}, this);
		if (books != null && books.size() > 0) {
			return books.get(0);
		}else {
			return null;
		}
	}

	public List<Book> readAll () throws SQLException {
		return template.query ("select * from tbl_book", this);
	}

	public List<Book> readAllByAuthor (Author auth) throws SQLException {
		return template.query ("select * from tbl_book where bookId in (select bookId from tbl_book_authors where authorId = ?)",
				new Object[] {auth.getAuthorId()}, this);
	}
	public List<Book> readAllByGenre (Genre genre) throws SQLException {
		return template.query ("select * from tbl_book where bookId in (select bookId from tbl_book_genres where genre_id = ?)",
				new Object[] {genre.getGenreId()}, this);
	}

	public List<Book> readAllByPublisher (Publisher pub) throws SQLException {
		return template.query ("select * from tbl_book where pubId = ?)",
				new Object[] {pub.getPublisherId()}, this);
	}

	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException,
	DataAccessException {
		List<Book> books = new ArrayList<Book> ();
		while (rs.next()) {
			Book bk = new Book();
			bk.setBookId(rs.getInt("bookId"));
			bk.setTitle(rs.getString("title"));
			bk.setPublisher(pubDAO.readOne(rs.getInt("pubId")));
			books.add(bk);
		}
		return books;
	}

	public int countBooks(String searchText) throws SQLException {
		searchText = '%' + searchText + '%';
		return template.queryForObject("select count(*) from tbl_book where title like ?", new Object[] {searchText}, Integer.class);
	}
	
	public List<Map<String, Object>> countBooksByPublishers () throws SQLException {
		return template.queryForList("select publisherName, count(bookId) as count from tbl_publisher left join tbl_book on tbl_book.pubId = tbl_publisher.publisherId group by publisherId order by publisherName asc");
	}

	public List<Book> searchSizedBooks(int pageNo, int pageSize, String searchText) throws SQLException {
		searchText = '%' + searchText + '%';
		return template.query (setPageLimits(pageNo, pageSize, "select * from tbl_book where title like ?"),
				new Object[] {searchText}, this);
	}
}
