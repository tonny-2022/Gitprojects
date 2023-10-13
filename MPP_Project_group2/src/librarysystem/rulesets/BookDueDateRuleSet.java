package librarysystem.rulesets;

import business.Group2Exception;
import librarysystem.BookDueDateWindow;

import java.awt.*;

/**
 * Rules:
 * 1. Fields cannot be empty!
 *
 */
public class BookDueDateRuleSet implements RuleSet {
	private BookDueDateWindow bookWindow;
	@Override
	public void applyRules(Component ob) throws Group2Exception {
		bookWindow = (BookDueDateWindow) ob;
		nonemptyRule();
	}

	private void nonemptyRule() throws Group2Exception {
		if(bookWindow.getISBNString().trim().isEmpty()) {
			throw new Group2Exception("Fields cannot be empty!");
		}
	}
}
