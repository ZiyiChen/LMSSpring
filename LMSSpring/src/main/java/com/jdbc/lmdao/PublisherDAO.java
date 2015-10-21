/**
 * PublisherDAO.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.jdbc.lmdao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.jdbc.lmdo.Publisher;

public class PublisherDAO extends BaseDAO implements ResultSetExtractor<List<Publisher>>{
	/**
	 * @param temp
	 */
	public PublisherDAO(JdbcTemplate temp) {
		super(temp);
	}

	public void insert(Publisher pub) throws SQLException {
		int pubId = template.update("insert into tbl_publisher (publisherName, publisherAddress, publisherPhone) values (?, ?, ?)",
				new Object[]{pub.getPublisherName(), pub.getAddress(), pub.getPhone()});
		pub.setPublisherId(pubId);
	}
	
	public void update(Publisher pub) throws SQLException {
		template.update("update tbl_publisher set publisherName = ?, publisherAddress = ?, publisherPhone = ? where publisherId = ?",
				new Object[]{pub.getPublisherName(), pub.getAddress(), pub.getPhone(), pub.getPublisherId()});
	}
	
	public void delete (Publisher pub) throws SQLException {
		template.update("update tbl_book set pubId = NULL where pubId = ?",
				new Object[]{pub.getPublisherId()});
		template.update("delete from tbl_publisher where publisherId = ?",
				new Object[]{pub.getPublisherId()});
	}
	
	public Publisher readOne (int pubId) throws SQLException {
		List<Publisher> pubs =  template.query("select * from tbl_publisher where publisherId = ?",
				new Object[]{pubId}, this);
		if (pubs != null && pubs.size() > 0) {
			return pubs.get(0);
		}else {
			return null;
		}
	}
	
	public List<Publisher> readAll () throws SQLException{
		return  template.query("select * from tbl_publisher", this);
	}

	@Override
	public List<Publisher> extractData(ResultSet rs) throws SQLException {
		List<Publisher> pubs = new ArrayList<Publisher>();
		while (rs.next()) {
			Publisher pub = new Publisher();
			pub.setPublisherId(rs.getInt("publisherId"));
			pub.setPublisherName(rs.getString("publisherName"));
			pub.setAddress(rs.getString("publisherAddress"));
			pub.setPhone(rs.getString("publisherPhone"));
			pubs.add(pub);
		}
		return pubs;
	}

	public List<Publisher> searchSizedPublishers(int pageNo, int pageSize,
			String search) throws SQLException {
		search = '%' + search + '%';
		return template.query (setPageLimits(pageNo, pageSize, "select * from tbl_publisher where publisherName like ?"),
				new Object[] {search}, this);
	}

	public int countPublishers(String search) throws SQLException {
		search = '%' + search + '%';
		return template.queryForObject("select count(*) from tbl_publisher where publisherName like ?", new Object[] {search}, Integer.class);
	}
	
	
}
