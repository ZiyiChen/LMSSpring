/**
 * LMSConfiguration.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.spring.lms.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jdbc.lmdao.AuthorDAO;
import com.jdbc.lmdao.BookDAO;
import com.jdbc.lmdao.BookLoansDAO;
import com.jdbc.lmdao.BorrowerDAO;
import com.jdbc.lmdao.BranchDAO;
import com.jdbc.lmdao.GenreDAO;
import com.jdbc.lmdao.PublisherDAO;
import com.jdbc.lmsys.AdministratorManagementSys;

@Configuration
public class LMSConfiguration {

	@Bean
	public AdministratorManagementSys admin() {
		return new AdministratorManagementSys();
	}

	@Bean
	public BookDAO bookDAO() {
		return new BookDAO(template());
	}
	@Bean
	public AuthorDAO authorDAO() {
		return new AuthorDAO(template());
	}
	@Bean
	public PublisherDAO pubDAO() {
		return new PublisherDAO(template());
	}
	
	@Bean
	public GenreDAO genDAO() {
		return new GenreDAO(template());
	}
	
	@Bean
	public BookLoansDAO blDAO() {
		return new BookLoansDAO(template());
	}
	
	@Bean
	public BranchDAO bhDAO () {
		return new BranchDAO(template());
	}
	
	@Bean
	public BorrowerDAO brDAO () {
		return new BorrowerDAO(template());
	}
	
	@Bean
	public JdbcTemplate template() {
		JdbcTemplate template = new JdbcTemplate();
		template.setDataSource(datasource());
		return template;
	}
	
	@Bean
	public BasicDataSource datasource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/library");
		ds.setUsername("root");
		ds.setPassword("password");
		
		return ds;
	}
}
