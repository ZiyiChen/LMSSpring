/**
 * AuthorDAO.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.jdbc.lmdao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.jdbc.lmdo.Author;
import com.jdbc.lmdo.Book;

public class AuthorDAO extends BaseDAO implements
ResultSetExtractor<List<Author>>{

	public AuthorDAO(JdbcTemplate temp) {
		super(temp);
	}

	public void insert(Author auth) throws SQLException {
		template.update("insert into tbl_author (authorName) values (?)",
				new Object[] {auth.getAuthorName()});
	}

	public void update(Author auth) throws SQLException {
		template.update("update tbl_author set authorName = ? where authorId = ?",
				new Object[] { auth.getAuthorName(), auth.getAuthorId() });
	}

	public void delete(Author auth) throws SQLException {
		template.update("delete from tbl_book_authors where authorId = ?",
				new Object[] { auth.getAuthorId() });
		template.update("delete from tbl_author where authorId = ?",
				new Object[] { auth.getAuthorId() });
	}

	public Author readOne(int authorId) throws SQLException {
		List<Author> authors = template.query(
				"select * from tbl_author where authorId = ?",
				new Object[] { authorId }, this);
		if (authors != null && authors.size() > 0) {
			return authors.get(0);
		} else {
			return null;
		}
	}

	public List<Author> readAll() throws SQLException {
		return template.query("select * from tbl_author", this);
	}

	public List<Author> readAllByBook(Book bk) throws SQLException {
		return template.query(
				"select * from tbl_author where authorId in (select authorId from tbl_book_authors where bookId = ?)",
				new Object[] { bk.getBookId() }, this);
	}

	public List<Author> searchSizedAuthors(int pageNo, int pageSize, String search) throws SQLException {
		search = '%' + search + '%';
		return template.query (setPageLimits(pageNo, pageSize, "select * from tbl_author where authorName like ?"),
				new Object[] {search}, this);
	}

	public int countAuthors(String searchText) throws SQLException {
		searchText = '%' + searchText + '%';
		return template.queryForObject("select count(*) from tbl_author where authorName like ?", new Object[] {searchText}, Integer.class);
	}

	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Author> authors = new ArrayList<Author>();
		while (rs.next()) {
			Author auth = new Author();
			auth.setAuthorId(rs.getInt("authorId"));
			auth.setAuthorName(rs.getString("authorName"));
			authors.add(auth);
		}

		return authors;
	}
}
