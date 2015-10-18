/**
 * LibrarianManagementSys.java
 * @author Ziyi Chen 
 * <br> Email: <a href="mailto:ziyi_chen@gcitsolutions.com">ziyi_chen@gcitsolutions.com</a>
 * <br> Created on Sep 29, 2015
 */
package com.jdbc.lmsys;



import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.jdbc.lmdao.BookCopiesDAO;
import com.jdbc.lmdao.BookDAO;
import com.jdbc.lmdao.BranchDAO;
import com.jdbc.lmdo.Book;
import com.jdbc.lmdo.BookCopies;
import com.jdbc.lmdo.Branch;

public class LibrarianManagementSys {

	private Branch currBranch;
	private BufferedReader br;

	public LibrarianManagementSys (BufferedReader br) {
		this.br = br;
	}

	public void start () throws IOException, SQLException {
		//LIB1
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean lib1 = true;
		while(lib1) {
			System.out.println("1)\tEnter Branch you manage");
			System.out.println("2)\tQuite to previous ");

			int in1 = Integer.parseInt(br.readLine());
			switch (in1) {
			case 1:
				boolean lib2 = true;
				//LIB2
				while (lib2) {
					List<Branch> bhs = getAllBranches();
					int j = 1;
					for (Branch bh : bhs) {
						System.out.println(j + ")\t" + bh.getName() + ", " + bh.getAddress());
						j++;
					}
					System.out.println(j + ")\tQuit to previous");
					int in2 = Integer.parseInt(br.readLine());
					if (in2 < j && in2 > 0) {
						//LIB3
						currBranch = bhs.get(in2-1);
						boolean lib3 = true;
						while (lib3) {
							System.out.println("1)\tUpdate the details of the Library ");
							System.out.println("2)\tAdd copies of Book to the Branch");
							System.out.println("3)\tQuit to previous ");
							int in3 = Integer.parseInt(br.readLine());
							switch(in3) {
							case 1:
								//LIB4
								boolean lib4 = true;
								while(lib4) {
									System.out.println("You have chosen to update the Branch with Branch Id: "
											+currBranch.getBranchId()
											+" and Branch Name: "
											+currBranch.getName()
											+". \nEnter ¡®quit¡¯ at any prompt to cancel operation.");
									System.out.println("Please enter new branch name or enter N/A for no change:");
									String in41 = br.readLine();
									if (in41.equals("quit")) {
										lib4 = false;
										continue;
									} else if (!in41.equals("N/A")) {
										currBranch.setName(in41);
									}
									System.out.println("Please enter new branch address or enter N/A for no change:");
									String in42 = br.readLine();
									if (in42.equals("quit")) {
										lib4 = false;
									} else if (!in42.equals("N/A")) {
										currBranch.setAddress(in42);
									}
									updateBranch(currBranch);
									System.out.println("Successfully updated");
									lib4 = false;
								}
								break;
							case 2:
								//LIB5
								boolean lib5 = true;
								while(lib5) {
									System.out.println("Pick the Book you want to add copies of, to your branch:");
									List<Book> bks = getAllBooks();
									int k = 1;
									for (Book bk : bks) {
										System.out.println("\t"+k+") "+bk.getTitle());
										k++;
									}
									System.out.println("\t"+k+") Quit to cancel operation");
									int in5 = Integer.parseInt(br.readLine());
									if(in5 > 0 && in5 < k) {
										Book currBook = bks.get(in5-1);
										boolean lib6 = true;
										//LIB6
										while (lib6) {
											BookCopies bc = getBookCopies(currBook, currBranch);
											if (bc == null) {
												bc = new BookCopies();
												bc.setBook(currBook);
												bc.setBranch(currBranch);
												bc.setOnOfCopies(0);
											}
											System.out.println("Existing number of copies: " + bc.getOnOfCopies());
											System.out.println("Enter new number of copies:");
											int in6 = Integer.parseInt(br.readLine());
											if(in6 > -1) {
												bc.setOnOfCopies(in6);
												updateBookCopies(bc);
												lib6 = false;
												lib5 = false;
											} else {
												System.out.println("Invalid input, Please try again");
											}
										}
									} else if (in5 == k) {
										lib5 = false;
									} else {
										System.out.println("Invalid input, Please try again");
									}
								}
								break;
							case 3: lib3 = false;
							break;
							default: System.out.println("Invalid input, Please try again");
							break;
							}
						}
					}
					else if (in2 == j) {
						lib2 = false;
					} else {
						System.out.println("Invalid input, Please try again");
					}
				}
				break;
			case 2: lib1 = false;
			break;
			default: System.out.println("Invalid input, Please try again");
			break;
			}
		}
	}

	private List<Branch> getAllBranches() throws SQLException {
		BranchDAO bhDAO = new BranchDAO();
		return bhDAO.readAll();
	}

	private void updateBranch(Branch branch) throws SQLException {
		BranchDAO bhDAO = new BranchDAO();
		bhDAO.update(branch);
	}

	private List<Book> getAllBooks () throws SQLException {
		BookDAO bkDAO = new BookDAO();
		return bkDAO.readAll();
	}

	private BookCopies getBookCopies (Book bk, Branch bh) throws SQLException {
		BookCopiesDAO bcDAO = new BookCopiesDAO();
		return bcDAO.readOne(bk, bh);
	}

	private void updateBookCopies (BookCopies bc) throws SQLException {
		BookCopiesDAO bcDAO = new BookCopiesDAO();
		bcDAO.delete(bc);
		bcDAO.insert(bc);
	}
}
