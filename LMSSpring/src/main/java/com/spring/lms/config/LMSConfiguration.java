/**
 * LMSConfiguration.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.spring.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
		return new BookDAO();
	}
	@Bean
	public AuthorDAO authorDAO() {
		return new AuthorDAO();
	}
	@Bean
	public PublisherDAO pubDAO() {
		return new PublisherDAO();
	}
	
	@Bean
	public GenreDAO genDAO() {
		return new GenreDAO();
	}
	
	@Bean
	public BookLoansDAO blDAO() {
		return new BookLoansDAO();
	}
	
	@Bean
	public BranchDAO bhDAO () {
		return new BranchDAO();
	}
	
	@Bean
	public BorrowerDAO brDAO () {
		return new BorrowerDAO();
	}
	
}
