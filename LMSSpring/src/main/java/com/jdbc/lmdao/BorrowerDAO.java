/**
 * BorrowerDAO.java
 * @borror Ziyi Chen 
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

import com.jdbc.lmdo.Borrower;

public class BorrowerDAO extends BaseDAO implements ResultSetExtractor<List<Borrower>>{
	/**
	 * @param temp
	 */
	public BorrowerDAO(JdbcTemplate temp) {
		super(temp);
	}

	public void insert(Borrower borr) throws SQLException {
		template.update("insert into tbl_borrower (name, address, phone) values (?)",
				new Object[] {borr.getName(), borr.getAddress(), borr.getPhone()});
	}

	public void update(Borrower borr) throws SQLException {
		template.update("update tbl_borrower set name = ?, address = ?, phone = ? where cardNo = ?",
				new Object[] {borr.getName(), borr.getAddress(), borr.getPhone(), borr.getCardNo()});
	}

	public void delete(Borrower borr) throws SQLException {
		template.update("delete from tbl_book_loans where cardNo = ?",
				new Object[]{borr.getCardNo()});
		template.update("delete from tbl_borrower where cardNo = ?",
				new Object[] {borr.getCardNo()});
	}

	public Borrower readOne(int cardNo) throws SQLException {
		List<Borrower> borrs = template.query(
				"select * from tbl_borrower where cardNo = ?",
				new Object[] { cardNo }, this);
		if (borrs != null && borrs.size() > 0) {
			return borrs.get(0);
		} else {
			return null;
		}
	}

	public List<Borrower> readAll() throws SQLException {
		return template.query("select * from tbl_borrower", this);
	}

	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Borrower> borrs = new ArrayList<Borrower>();
		while (rs.next()) {
			Borrower borr = new Borrower();
			borr.setCardNo(rs.getInt("cardNo"));
			borr.setName(rs.getString("name"));
			borr.setAddress(rs.getString("address"));
			borr.setPhone(rs.getString("phone"));
			borrs.add(borr);
		}

		return borrs;
	}
}
