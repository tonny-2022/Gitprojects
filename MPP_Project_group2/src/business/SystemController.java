package business;

import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SystemController implements ControllerInterface {
	public static Auth currentAuth = null;


	/**
	 *
	 * @param id
	 * @param password
	 * @throws Group2Exception
	 */
	@Override
	public void login(String id, String password) throws Group2Exception {
		DataAccess da = new DataAccessFacade();
		HashMap<String, User> map = da.readUserMap();
		if(!map.containsKey(id)) {
			throw new Group2Exception("ID " + id + " not found");
		}
		String passwordFound = map.get(id).getPassword();
		if(!passwordFound.equals(password)) {
			throw new Group2Exception("Password incorrect");
		}
		SystemController.currentAuth = map.get(id).getAuthorization();

	}

	/**
	 *
	 * @param string
	 */
	@Override
	public void showError(String string) {

	}

	/**
	 *
	 * @param info
	 */
	@Override
	public void showInfo(String info) {

	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<LibraryMember> getAllLibraryMember() {
		DataAccess da = new DataAccessFacade();
		return da.readMemberMap().values().stream().toList();
	}

	/**
	 *
	 * @param memberID
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param zip
	 * @param state
	 * @param tel
	 * @return
	 * @throws Group2Exception
	 */
	@Override
	public LibraryMember createLibraryMember(String memberID,
											 String firstName,
											 String lastName,
											 String street,
											 String city,
											 String zip,
											 String state,
											 String tel) throws Group2Exception {

		DataAccess da = new DataAccessFacade();
		HashMap<String, LibraryMember> map = da.readMemberMap();
		if (map.containsKey(memberID)) {
			throw new Group2Exception("This memberID exist in the system");
		}
		Address address = new Address(street,city,state,zip);
		LibraryMember member = new LibraryMember(memberID,firstName,lastName,tel,address);
		da.saveNewMember(member);
		return member;
	}

	/**
	 *
	 * @param memberID
	 * @return
	 * @throws Group2Exception
	 */
	@Override
	public CheckoutRecord getRecord(String memberID) throws Group2Exception {
		DataAccess da = new DataAccessFacade();
		HashMap<String, LibraryMember> map = da.readMemberMap();
		if (!map.containsKey(memberID)) {
			throw new Group2Exception("This memberID does not exist in the system");
		}
		return map.get(memberID).getCheckoutRecord();
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<Book> getAllBook() {
		DataAccess da = new DataAccessFacade();
		return da.readBooksMap().values().stream().toList();
	}

	/**
	 *
	 * @param isbn
	 * @return
	 * @throws Group2Exception
	 */
	@Override
	public Book getBookById(String isbn) throws Group2Exception {
		DataAccess da = new DataAccessFacade();
		HashMap<String, Book> map = da.readBooksMap();
		Book book = map.get(isbn);
		if (book == null) {
			throw new Group2Exception("This ISBN does not exist in the system");
		}
		return book;
	}

	/**
	 *
	 * @param ISBN
	 * @param title
	 * @param maxCheckoutLength
	 * @param authors
	 * @param copies
	 * @return
	 * @throws Group2Exception
	 */
	@Override
	public Book addBook(String ISBN,
						String title,
						int maxCheckoutLength,
						List<Author> authors,
						List<BookCopy> copies) throws Group2Exception {
		DataAccess da = new DataAccessFacade();
		HashMap<String, Book> map = da.readBooksMap();
		if (map.containsKey(ISBN)) {
			throw new Group2Exception("This ISBN existed in the system");
		}
		Book book = new Book(ISBN,title,maxCheckoutLength,authors, copies.toArray(new BookCopy[0]));
		map.put(ISBN,book);
		da.updateBook(book);
		return book;
	}

	/**
	 *
	 * @param book
	 * @return
	 * @throws Group2Exception
	 */
	@Override
	public BookCopy addBookCopy(Book book) throws Group2Exception {
		DataAccess da = new DataAccessFacade();
		HashMap<String, Book> map = da.readBooksMap();
		if (!map.containsKey(book.getIsbn())) {
			throw new Group2Exception("This ISBN does not exist in the system");
		}
		BookCopy bookCopy = book.addCopy();
		da.updateBook(book);
		return bookCopy;
	}

	/**
	 *
	 * @param book
	 * @param bookCopy
	 * @param newStatus
	 * @throws Group2Exception
	 */
	@Override
	public void updateBookCopyStatus(Book book, BookCopy bookCopy, boolean newStatus) throws Group2Exception {
		DataAccess da = new DataAccessFacade();
		HashMap<String, Book> map = da.readBooksMap();
		if (!map.containsKey(book.getIsbn())) {
			throw new Group2Exception("This ISBN does not exist in the system");
		}
		Book refBook = map.get(book.getIsbn());
		BookCopy refBookCopy = refBook.getCopies()[bookCopy.getCopyNum()];
		refBookCopy.setAvailable(newStatus);

		da.updateBook(refBook);
	}

	/**
	 *
	 * @param memberID
	 * @param isbn
	 * @return
	 * @throws Group2Exception
	 */
	@Override
	public Book checkout(String memberID, String isbn) throws Group2Exception {
		DataAccess da = new DataAccessFacade();
		HashMap<String, LibraryMember> memberMap = da.readMemberMap();
		if (!memberMap.containsKey(memberID)) {
			throw new Group2Exception("MemberId not found");
		}


		HashMap<String, Book> bookHashMap = da.readBooksMap();
		if (!bookHashMap.containsKey(isbn)) {
			throw new Group2Exception("Book not found");
		}
		Book book = bookHashMap.get(isbn);
		if (!book.isAvailable()) {
			throw new Group2Exception("Book copy not available");
		}
		BookCopy copy = book.getNextAvailableCopy();
		LibraryMember member = memberMap.get(memberID);

		if (member.getCheckoutRecord() == null) {
			member.setCheckoutRecord(new CheckoutRecord());
		}

		copy.changeAvailability();
		member.getCheckoutRecord()
				.addRecordEntry(new CheckoutRecordEntry(copy));
		da.saveNewRecord(member.getCheckoutRecord());
		da.updateBook(book);
		da.saveNewMember(member);
		return book;
	}

	/**
	 *
	 * @param isbn
	 * @return
	 * @throws Group2Exception
	 */
	@Override
	public Book addBookCopyByISBN(String isbn) throws Group2Exception {
		DataAccess da = new DataAccessFacade();
		HashMap<String, Book> map = da.readBooksMap();
		Book book = map.get(isbn);
		if (book == null) {
			throw new Group2Exception("This ISBN does not exist in the system");
		}
		book.addCopy();
		da.updateBook(book);
		return book;
	}

	/**
	 *
	 * @param isbn
	 * @return
	 * @throws Group2Exception
	 */
	@Override
	public HashMap<BookCopy, LibraryMember> find(String isbn) throws Group2Exception {
		DataAccess da = new DataAccessFacade();
		HashMap<String, Book> map = da.readBooksMap();
		if (!map.containsKey(isbn)) {
			throw new Group2Exception("This ISBN does not exist in the system");
		}

		HashMap<BookCopy,LibraryMember> bookCopyLibraryMemberHashMap = new HashMap<>();
		Book book = map.get(isbn);
		List<BookCopy> bookCopyList = Arrays.asList(book.getCopies());

		HashMap<String, LibraryMember> memberMap = da.readMemberMap();
		for (LibraryMember member: memberMap.values()) {
			List<CheckoutRecordEntry> entries = member.getCheckoutRecord().getRecordEntries();
			for (int i = 0; i < entries.size(); i++) {
				BookCopy copy = entries.get(i).getBookCopy();
				if (bookCopyList.contains(copy)) {
					bookCopyLibraryMemberHashMap.put(copy,member);
				}
			}
		}
		if (bookCopyLibraryMemberHashMap.isEmpty()) {
			bookCopyLibraryMemberHashMap.put(bookCopyList.get(0),null);
		}
		return bookCopyLibraryMemberHashMap;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public List<Author> getAllAuthor() {
		DataAccess da = new DataAccessFacade();
		return da.readAuthorsMap().values().stream().toList();
	}

	/**
	 *
	 * @param ISBN
	 * @return
	 */
	@Override
	public List<Author> getAllAuthorByBook(String ISBN) {
		return null;
	}

} // End of SystemController class
