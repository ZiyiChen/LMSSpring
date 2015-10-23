/**
 * AdminController.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.spring.lms;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdbc.lmdo.Author;
import com.jdbc.lmdo.Book;
import com.jdbc.lmdo.BookLoans;
import com.jdbc.lmdo.Genre;
import com.jdbc.lmdo.Publisher;
import com.jdbc.lmsys.AdministratorManagementSys;

@RestController
public class AdminController {

	@Autowired
	private AdministratorManagementSys adminService;
	
	@RequestMapping(value = "/addBook", method = RequestMethod.POST, consumes = "application/json", produces="text/plain")
	public @ResponseBody String addBook(@RequestBody Book bk, Locale locale, Model model) {
		try {
			adminService.insertBook(bk);
			return "added succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Book add failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/updateBook", method = RequestMethod.POST, consumes = "application/json", produces="text/plain")
	public @ResponseBody String updateBook(@RequestBody Book bk, Locale locale, Model model) {
		try {
			adminService.updateBook(bk);
			return "Book update succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Book update failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/deleteBook", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String deleteBook(@RequestBody Book bk, Locale locale, Model model) {
		try {
			adminService.deleteBook(bk);
			return "Book delete succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Book delete failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/listBooksPage/{pageNo}/{pageSize}", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
	public @ResponseBody String listBooksPage(@PathVariable(value = "pageNo") Integer pageNo, @PathVariable(value = "pageSize") Integer pageSize, @RequestParam(value = "searchText", required = false) String searchText) {
		try {
			List<Book> bks = adminService.searchSizedFullLoadBooks(pageNo, pageSize, searchText);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(bks);
		}catch (Exception e) {
			e.printStackTrace();
			return "List Book Page failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/listBooks", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String listBooks () {
		try {
			List<Book> bks = adminService.getAllBooks();
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(bks);
		}catch (Exception e) {
			e.printStackTrace();
			return "List Book failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/listAuthors", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String listAuthors () {
		try {
			List<Author> aus = adminService.getAllAuthors();
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(aus);
		}catch (Exception e) {
			e.printStackTrace();
			return "List Author failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/countBook", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String countBook(@RequestParam(value = "searchText", required = false) String searchText) {
		try {
			return Integer.toString(adminService.countBook(searchText));
		}catch (Exception e) {
			e.printStackTrace();
			return "Count Book failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/addAuthor", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String addAuthor(@RequestBody Author author, Locale locale, Model model) {
		try {
			adminService.insertAuthor(author);
			return "Author added succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Author add failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/updateAuthor", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String updateAuthor(@RequestBody Author author, Locale locale, Model model) {
		try {
			adminService.updateAuthor(author);
			return "Author update succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Author update failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/deleteAuthor", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String deleteAuthor(@RequestBody Author author, Locale locale, Model model) {
		try {
			adminService.deleteAuthor(author);
			return "Author delete succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Author delete failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/listAuthorsPage/{pageNo}/{pageSize}/", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String listAuthorsPage(@PathVariable(value = "pageNo") Integer pageNo, @PathVariable(value = "pageSize") Integer pageSize, @RequestParam(value = "searchText", required = false) String searchText) {
		try {
			List<Author> aus = adminService.searchAuthors(pageNo, pageSize, searchText);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(aus);
		}catch (Exception e) {
			e.printStackTrace();
			return "List Author Page failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/countAuthor", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String countAuthor(@RequestParam(value = "searchText", required = false) String searchText) {
		try {
			return Integer.toString(adminService.countAuthor(searchText));
		}catch (Exception e) {
			e.printStackTrace();
			return "Count Author failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/listGenres", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String listGenres () {
		try {
			List<Genre> gens = adminService.getAllGenres();
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(gens);
		}catch (Exception e) {
			e.printStackTrace();
			return "List Genre failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/addPublisher", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String addPublisher(@RequestBody Publisher pub, Locale locale, Model model) {
		try {
			adminService.insertPublisher(pub);
			return "Publisher added succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Publisher add failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/updatePublisher", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String updateAuthor(@RequestBody Publisher pub, Locale locale, Model model) {
		try {
			adminService.updatePublisher(pub);
			return "Publisher update succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Publisher update failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/deletePublisher", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String deletePublisher(@RequestBody Publisher pub, Locale locale, Model model) {
		try {
			adminService.deletePublisher(pub);
			return "Publisher delete succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Publisher delete failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/listPublishers", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String listPublishers () {
		try {
			List<Publisher> pubs = adminService.getAllPublishers();
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(pubs);
		}catch (Exception e) {
			e.printStackTrace();
			return "List Publisher failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/listPublishersPage/{pageNo}/{pageSize}/", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String listPublishersPage(@PathVariable(value = "pageNo") Integer pageNo, @PathVariable(value = "pageSize") Integer pageSize, @RequestParam(value = "searchText", required = false) String searchText) {
		try {
			List<Publisher> pubs = adminService.searchPublishers(pageNo, pageSize, searchText);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(pubs);
		}catch (Exception e) {
			e.printStackTrace();
			return "List Publisher Page failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/countPublisher", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String countPublisher(@RequestParam(value = "searchText", required = false) String searchText) {
		try {
			return Integer.toString(adminService.countPublisher(searchText));
		}catch (Exception e) {
			e.printStackTrace();
			return "Count Publisher failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/listBookLoansPage/{pageNo}/{pageSize}/", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String listBookLoansPage(@PathVariable(value = "pageNo") Integer pageNo, @PathVariable(value = "pageSize") Integer pageSize) {
		try {
			List<BookLoans> bls = adminService.pageBookLoans(pageNo, pageSize);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(bls);
		}catch (Exception e) {
			e.printStackTrace();
			return "List BookLoans Page failed. Reason: " + e.getMessage();
		}
	}
	
	@RequestMapping(value = "/updateBookLoan", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody String updateBookLoan(@RequestBody BookLoans bl, Locale locale, Model model) {
		try {
			adminService.overrideDueDate(bl);
			return "Override due date succesfully";
		} catch (Exception e) {
			e.printStackTrace();
			return "Override due date failed. Reason: " + e.getMessage();
		}
	}
}
