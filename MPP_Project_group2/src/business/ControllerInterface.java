package business;

import business.*;
import dataaccess.Auth;

import java.util.HashMap;
import java.util.List;

public interface ControllerInterface {
	public static Auth currentAuth = null;

	/**
	 * Login handling
	 */
	public abstract void login(String id, String password) throws Group2Exception;


	/**
	 * Message handling: Error messages, information, etc
	 *
	 */
	void showError(String string);
	void showInfo(String info);


	/**
	 * Handling Library member
	 */
	public List<LibraryMember> getAllLibraryMember();
	public LibraryMember createLibraryMember(String memberID,
											 String firstName,
											 String lastName,
											 String street,
											 String city,
											 String zip,
											 String state,
											 String tel) throws Group2Exception;

	/**
	 * Handling books:
	 * @param memberID
	 * @return
	 * @throws Group2Exception
	 */

	public CheckoutRecord getRecord(String memberID) throws Group2Exception;

	public List<Book> getAllBook();

	public Book getBookById(String isbn) throws Group2Exception;

	public Book addBook(String ISBN,
						String title,
						int maxCheckoutLength,
						List<Author> authors,
						List<BookCopy> copies) throws Group2Exception;

	public BookCopy addBookCopy(Book book) throws Group2Exception;

	public void updateBookCopyStatus(Book book, BookCopy bookCopy, boolean newStatus) throws Group2Exception;

	public Book checkout(String memberID, String isbn) throws Group2Exception;

	Book addBookCopyByISBN(String isbn) throws Group2Exception;

	public HashMap<BookCopy, LibraryMember> find(String isbn) throws Group2Exception;


	/**
	 * Handling Authors
	 */
	public List<Author> getAllAuthor();

	public List<Author> getAllAuthorByBook(String ISBN);


} // End of ControllerInterface class
