/**
 * BorrowerManagementSys.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.jdbc.lmsys;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jdbc.lmdao.BookCopiesDAO;
import com.jdbc.lmdao.BookLoansDAO;
import com.jdbc.lmdao.BorrowerDAO;
import com.jdbc.lmdao.BranchDAO;
import com.jdbc.lmdo.Book;
import com.jdbc.lmdo.BookCopies;
import com.jdbc.lmdo.BookLoans;
import com.jdbc.lmdo.Borrower;
import com.jdbc.lmdo.Branch;

public class BorrowerManagementSys {

	private BufferedReader brd;

	public BorrowerManagementSys (BufferedReader br) {
		this.brd = br;
	}

	public void start () throws IOException, SQLException {
		boolean bor0 = true;
		while (bor0) {
			System.out.println("Enter the your Card Number: (enter 'quit' to quit)");
			String s = brd.readLine();
			if (s.equals("quit")) {
				bor0 = false;
				continue;
			}
			int in0 = Integer.parseInt(s);
			Borrower br = getBorrower(in0);
			//System.out.println(br);
			if (br != null) {
				boolean bor1 = true;
				while (bor1) {
					System.out.println("1)\tCheck out a book");
					System.out.println("2)\tReturn a Book");
					System.out.println("3)\tQuit to Previous ");

					int in1 = Integer.parseInt(brd.readLine());
					switch (in1) {
					case 1:
						boolean bor2 = true;
						while (bor2) {
							List<Branch> bhs = getAllBranches();
							int i = 1;
							for (Branch bh : bhs) {
								System.out.println(i + ")\t" + bh.getName() + ", " + bh.getAddress());
								i++;
							}
							System.out.println(i + ")\tQuit to previous");
							int in2 = Integer.parseInt(brd.readLine());
							if (in2 < i && in2 > 0) {
								Branch currBh = bhs.get(in2-1);
								boolean bor3 = true;
								while(bor3) {
									List<BookCopies> bcs = getAllAvailableBookCopies(currBh, br);
									System.out.println("Pick the Book you want to check out ");
									int j = 1;
									for (BookCopies bc : bcs) {
										System.out.println("\t"+j+") "+bc.getBook().getTitle()
												+", by "+bc.getBook().getPublisher().getPublisherName()
												+" ("+bc.getOnOfCopies()+")");
										j++;
									}
									System.out.println("\t"+j+") Quit to cancel operation");
									int in3 = Integer.parseInt(brd.readLine());
									if(in3 > 0 && in3 < j) {
										BookCopies currBc = bcs.get(in3-1);
										checkOut(currBc, br);
										System.out.println("Successfully checked out");
									}else if (in3 == j) {
										bor3 = false;
									} else {
										System.out.println("Invalid input, Please try again");
									}
								}
							}else if (in2 == i) {
								bor2 = false;
							}else {
								System.out.println("Invalid input, Please try again");
							}
						}
						break;
					case 2:
						boolean bor4 = true;
						while (bor4) {
							List<Branch> bhs = getAllBranches();
							int i = 1;
							for (Branch bh : bhs) {
								System.out.println(i + ")\t" + bh.getName() + ", " + bh.getAddress());
								i++;
							}
							System.out.println(i + ")\tQuit to previous");
							int in4 = Integer.parseInt(brd.readLine());
							if (in4 < i && in4 > 0) {
								Branch currBh = bhs.get(in4-1);
								boolean bor5 = true;
								while(bor5) {
									List<BookLoans> bls = getAllAvailableBookLoans(br, currBh);
									DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
									System.out.println("Pick the Book you want to return");
									int k = 1;
									for (BookLoans bl : bls) {
										System.out.println("\t"+k+") "+bl.getBook().getTitle()
												+", date rent: ("+dateFormat.format(bl.getDateOut())
												+"), due date: ("+dateFormat.format(bl.getDueDate())+")");
										k++;
									}
									System.out.println("\t"+k+") Quit to cancel operation");
									int in5 = Integer.parseInt(brd.readLine());
									if(in5 > 0 && in5 < k) {
										BookLoans bl = bls.get(in5-1);
										returnBook(bl);
										System.out.println("Successfully returned the book");
									}else if (in5 == k) {
										bor5 = false;
									} else {
										System.out.println("Invalid input, Please try again");
									}
								}
							}else if (in4 == i) {
								bor4 = false;
							}else {
								System.out.println("Invalid input, Please try again");
							}
						}
						break;
					case 3: bor1 = false;
					break;
					default: System.out.println("Invalid input, Please try again");
					break;
					}
				}
			} else {
				System.out.println("Invalid key, Please try again");
			}
		}
	}


	private List<Branch> getAllBranches() throws SQLException {
		BranchDAO bhDAO = new BranchDAO();
		return bhDAO.readAll();
	}

	private List<BookCopies> getAllAvailableBookCopies (Branch bh, Borrower br) throws SQLException {
		BookCopiesDAO bcDAO = new BookCopiesDAO();
		List<BookCopies> bcs = bcDAO.readAllByBranch(bh);
		List<BookCopies> res = new ArrayList<BookCopies>();

		BookLoansDAO blDAO = new BookLoansDAO();
		List<BookLoans> bls = blDAO.readAllByCard(br);
		List<Book> bks = new ArrayList<Book>();
		for (BookLoans bl : bls) {
			if (bl.getDateIn() != null) {
				bks.add(bl.getBook());
			}
		}
		for (BookCopies bc : bcs) {
			if (bc.getOnOfCopies() > 0 && !bks.contains(bc.getBook())) {
				res.add(bc);
			}
		}
		return res;
	}

	private void checkOut (BookCopies bc, Borrower br) throws SQLException {
		BookLoans bl = new BookLoans();
		bl.setBook(bc.getBook());
		bl.setBranch(bc.getBranch());
		bl.setBorrower(br);
		Calendar cal = Calendar.getInstance();
		bl.setDateOut(cal.getTime());
		cal.add(Calendar.DATE, 7);
		bl.setDueDate(cal.getTime());
		BookLoansDAO blDAO = new BookLoansDAO();
		blDAO.delete(bl);
		blDAO.insert(bl);

		bc.setOnOfCopies(bc.getOnOfCopies() - 1);
		BookCopiesDAO bcDAO = new BookCopiesDAO();
		bcDAO.update(bc);
	}

	private List<BookLoans> getAllAvailableBookLoans (Borrower br, Branch bh) throws SQLException {
		BookLoansDAO blDAO = new BookLoansDAO();
		List<BookLoans> bls = blDAO.readAllByCard(br);
		List<BookLoans> res = new ArrayList<BookLoans>();
		for (BookLoans bl : bls) {
			if(bl.getBranch().getBranchId() == bh.getBranchId() && bl.getDateIn() == null) {
				res.add(bl);
			}
		}
		return res;
	}

	private void returnBook (BookLoans bl) throws SQLException {
		BookCopiesDAO bcDAO = new BookCopiesDAO();
		BookCopies bc = bcDAO.readOne(bl.getBook(), bl.getBranch());
		bc.setOnOfCopies(bc.getOnOfCopies() + 1);
		bcDAO.update(bc);

		BookLoansDAO blDAO = new BookLoansDAO();
		bl.setDateIn(new Date());
		blDAO.delete(bl);
		blDAO.insert(bl);
	}

	private Borrower getBorrower (int id) throws SQLException {
		BorrowerDAO brDAO = new BorrowerDAO();
		return brDAO.readOne(id);
	}
}
