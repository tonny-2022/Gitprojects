package business;

import business.BookCopy;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class CheckoutRecordEntry implements Serializable {
    @Serial
    private static final long serialVersionUID = -8195323504756671687L;

    private BookCopy bookCopy;
    private LocalDate checkoutDate;
    private LocalDate dueDate;

    public CheckoutRecordEntry(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
        this.checkoutDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(bookCopy.getBook().getMaxCheckoutLength());
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        return "Book:" + bookCopy.getBook().getTitle() + ", copy: " + bookCopy.getCopyNum() + ", CheckoutDate: " + checkoutDate.toString() + " DueDate: " + dueDate.toString();
    }
}
