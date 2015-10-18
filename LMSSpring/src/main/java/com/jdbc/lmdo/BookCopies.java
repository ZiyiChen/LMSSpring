/**
 * BookCopies.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.jdbc.lmdo;

public class BookCopies {
	private Branch branch;
	private Book book;
	private int onOfCopies;
	/**
	 * @return the branch
	 */
	public Branch getBranch() {
		return branch;
	}
	/**
	 * @param branch the branch to set
	 */
	public void setBranch(Branch branch) {
		this.branch = branch;
	}
	/**
	 * @return the book
	 */
	public Book getBook() {
		return book;
	}
	/**
	 * @param book the book to set
	 */
	public void setBook(Book book) {
		this.book = book;
	}
	/**
	 * @return the onOfCopies
	 */
	public int getOnOfCopies() {
		return onOfCopies;
	}
	/**
	 * @param onOfCopies the onOfCopies to set
	 */
	public void setOnOfCopies(int onOfCopies) {
		this.onOfCopies = onOfCopies;
	}
	
	
}
