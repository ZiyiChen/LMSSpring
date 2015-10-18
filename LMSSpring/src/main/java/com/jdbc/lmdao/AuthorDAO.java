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

import com.jdbc.lmdo.Author;
import com.jdbc.lmdo.Book;

public class AuthorDAO extends BaseDAO {
	public void insert(Author auth) throws SQLException {
		int id = saveWithId("insert into tbl_author (authorName) values (?)",
				new Object[] {auth.getAuthorName()});
		auth.setAuthorId(id);
	}

	public void update(Author auth) throws SQLException {
		save("update tbl_author set authorName = ? where authorId = ?",
				new Object[] { auth.getAuthorName(), auth.getAuthorId() });
	}

	public void delete(Author auth) throws SQLException {
		save("delete from tbl_book_authors where authorId = ?",
				new Object[] { auth.getAuthorId() });
		save("delete from tbl_author where authorId = ?",
				new Object[] { auth.getAuthorId() });
	}

	public Author readOne(int authorId) throws SQLException {
		List<Author> authors = (List<Author>) read(
				"select * from tbl_author where authorId = ?",
				new Object[] { authorId });
		if (authors != null && authors.size() > 0) {
			return authors.get(0);
		} else {
			return null;
		}
	}

	public List<Author> readAll() throws SQLException {
		return (List<Author>) read("select * from tbl_author", null);
	}

	public List<Author> readAllByBook(Book bk) throws SQLException {
		return (List<Author>) read(
				"select * from tbl_author where authorId in (select authorId from tbl_book_authors where bookId = ?)",
				new Object[] { bk.getBookId() });
	}

	public List<Author> searchSizedAuthors(int pageNo, int pageSize, String search) throws SQLException {
		search = '%' + search + '%';
		return (List<Author>) read (setPageLimits(pageNo, pageSize, "select * from tbl_author where authorName like ?"),
				new Object[] {search});
	}
	
	public int countAuthors(String searchText) throws SQLException {
		searchText = '%' + searchText + '%';
		return count ("select count(*) from tbl_author where authorName like ?", searchText);
	}
	
	@Override
	protected List<Author> convertResult(ResultSet rs) throws SQLException {
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
