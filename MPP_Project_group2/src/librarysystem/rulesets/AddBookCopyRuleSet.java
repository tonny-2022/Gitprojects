package librarysystem.rulesets;

import business.Group2Exception;
import librarysystem.AddBookCopyWindow;

import java.awt.*;

/**
 * Rules:
 * 1. Fields cannot be empty!
 */
public class AddBookCopyRuleSet implements RuleSet {
	private AddBookCopyWindow bookWindow;
	@Override
	public void applyRules(Component ob) throws Group2Exception {
		bookWindow = (AddBookCopyWindow) ob;
		nonemptyRule();
	}

	private void nonemptyRule() throws Group2Exception {
		if(bookWindow.getISBN().trim().isEmpty()) {
			throw new Group2Exception("Fields cannot be empty!");
		}
	}
}
