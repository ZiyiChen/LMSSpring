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

import com.jdbc.lmdo.Author;
import com.jdbc.lmdo.Publisher;

public class PublisherDAO extends BaseDAO{
	public void insert(Publisher pub) throws SQLException {
		int pubId = saveWithId("insert into tbl_publisher (publisherName, publisherAddress, publisherPhone) values (?, ?, ?)",
				new Object[]{pub.getPublisherName(), pub.getAddress(), pub.getPhone()});
		pub.setPublisherId(pubId);
	}
	
	public void update(Publisher pub) throws SQLException {
		save("update tbl_publisher set publisherName = ?, publisherAddress = ?, publisherPhone = ? where publisherId = ?",
				new Object[]{pub.getPublisherName(), pub.getAddress(), pub.getPhone(), pub.getPublisherId()});
	}
	
	public void delete (Publisher pub) throws SQLException {
		save("update tbl_book set pubId = NULL where pubId = ?",
				new Object[]{pub.getPublisherId()});
		save("delete from tbl_publisher where publisherId = ?",
				new Object[]{pub.getPublisherId()});
	}
	
	public Publisher readOne (int pubId) throws SQLException {
		List<Publisher> pubs = (List<Publisher>) read("select * from tbl_publisher where publisherId = ?",
				new Object[]{pubId});
		if (pubs != null && pubs.size() > 0) {
			return pubs.get(0);
		}else {
			return null;
		}
	}
	
	public List<Publisher> readAll () throws SQLException{
		return (List<Publisher>) read("select * from tbl_publisher", null);
	}

	@Override
	protected Object convertResult(ResultSet rs) throws SQLException {
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
		return (List<Publisher>) read (setPageLimits(pageNo, pageSize, "select * from tbl_publisher where publisherName like ?"),
				new Object[] {search});
	}

	public int countPublishers(String search) throws SQLException {
		search = '%' + search + '%';
		return count ("select count(*) from tbl_publisher where publisherName like ?", search);
	}
	
	
}
