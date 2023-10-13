package dataaccess;

import java.util.HashMap;

import business.Book;
import business.CheckoutRecord;
import business.LibraryMember;
import business.Author;

public interface DataAccess { 
	public HashMap<String,Book> readBooksMap();
	public HashMap<String, User> readUserMap();
	public HashMap<String, LibraryMember> readMemberMap();

	public HashMap<String,Author> readAuthorsMap();
	public void saveNewMember(LibraryMember member);

	public void updateBook(Book book);

	public HashMap<String, CheckoutRecord> readRecordsMap();

	public void saveNewRecord(CheckoutRecord record);

}
