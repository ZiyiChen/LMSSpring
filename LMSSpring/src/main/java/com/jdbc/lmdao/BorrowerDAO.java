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

import com.jdbc.lmdo.Borrower;
import com.jdbc.lmdo.Book;

public class BorrowerDAO extends BaseDAO{
	public void insert(Borrower borr) throws SQLException {
		int id = saveWithId("insert into tbl_borrower (name, address, phone) values (?)",
				new Object[] {borr.getName(), borr.getAddress(), borr.getPhone()});
		borr.setCardNo(id);
	}

	public void update(Borrower borr) throws SQLException {
		save("update tbl_borrower set name = ?, address = ?, phone = ? where cardNo = ?",
				new Object[] {borr.getName(), borr.getAddress(), borr.getPhone(), borr.getCardNo()});
	}

	public void delete(Borrower borr) throws SQLException {
		save("delete from tbl_book_loans where cardNo = ?",
				new Object[]{borr.getCardNo()});
		save("delete from tbl_borrower where cardNo = ?",
				new Object[] {borr.getCardNo()});
	}

	public Borrower readOne(int cardNo) throws SQLException {
		List<Borrower> borrs = (List<Borrower>) read(
				"select * from tbl_borrower where cardNo = ?",
				new Object[] { cardNo });
		if (borrs != null && borrs.size() > 0) {
			return borrs.get(0);
		} else {
			return null;
		}
	}

	public List<Borrower> readAll() throws SQLException {
		return (List<Borrower>) read("select * from tbl_borrower", null);
	}

	@Override
	protected List<Borrower> convertResult(ResultSet rs) throws SQLException {
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
