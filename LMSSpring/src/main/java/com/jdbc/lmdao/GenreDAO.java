/**
 * GenreDAO.java
 * @genre Ziyi Chen 
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

import com.jdbc.lmdo.Genre;
import com.jdbc.lmdo.Book;

public class GenreDAO extends BaseDAO implements
ResultSetExtractor<List<Genre>>{
	/**
	 * @param temp
	 */
	public GenreDAO(JdbcTemplate temp) {
		super(temp);
	}

	public void insert(Genre genre) throws SQLException {
		template.update("insert into tbl_genre (genre_name) values (?)",
				new Object[] { genre.getGenreName() });
	}

	public void update(Genre genre) throws SQLException {
		template.update("update tbl_genre set genre_name = ? where genre_id = ?",
				new Object[] { genre.getGenreName(), genre.getGenreId() });
	}

	public void delete(Genre genre) throws SQLException {
		template.update("delete from tbl_book_genres where genre_id = ?",
				new Object[] { genre.getGenreId() });
		template.update("delete from tbl_genre where genre_id = ?",
				new Object[] { genre.getGenreId() });
	}

	public Genre readOne(int genre_id) throws SQLException {
		List<Genre> genres = template.query(
				"select * from tbl_genre where genre_id = ?",
				new Object[] { genre_id }, this);
		if (genres != null && genres.size() > 0) {
			return genres.get(0);
		} else {
			return null;
		}
	}

	public List<Genre> readAll() throws SQLException {
		return template.query("select * from tbl_genre", this);
	}

	public List<Genre> readAllByBook(Book bk) throws SQLException {
		return  template.query(
				"select * from tbl_genre where genre_id in (select genre_id from tbl_book_genres where bookId = ?)",
				new Object[] { bk.getBookId() }, this);
	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Genre> genres = new ArrayList<Genre>();
		while (rs.next()) {
			Genre genre = new Genre();
			genre.setGenreId(rs.getInt("genre_id"));
			genre.setGenreName(rs.getString("genre_name"));
			genres.add(genre);
		}

		return genres;
	}
}
