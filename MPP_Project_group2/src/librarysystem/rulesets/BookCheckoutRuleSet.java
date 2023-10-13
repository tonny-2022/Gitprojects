package librarysystem.rulesets;

import business.Group2Exception;
import librarysystem.BookCheckoutWindow;

import java.awt.*;

/**
 * Rules:
 * 1. Fields cannot be empty!
 *
 */
public class BookCheckoutRuleSet implements RuleSet {
	private BookCheckoutWindow bookWindow;
	@Override
	public void applyRules(Component ob) throws Group2Exception {
		bookWindow = (BookCheckoutWindow) ob;
		nonemptyRule();


	}

	private void nonemptyRule() throws Group2Exception {
		if(bookWindow.getMemberID().trim().isEmpty() ||
				bookWindow.getISBN().trim().isEmpty()) {
			throw new Group2Exception("Fields cannot be empty!");
		}
	}
}
