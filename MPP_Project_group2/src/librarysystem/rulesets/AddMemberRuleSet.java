package librarysystem.rulesets;

import business.Group2Exception;
import librarysystem.AddMemberWindow;

import java.awt.*;

/**
 * Rules:
 *  1. Fields cannot be empty
 */

public class AddMemberRuleSet implements RuleSet {
		private AddMemberWindow window;
	@Override
	public void applyRules(Component ob) throws Group2Exception {
		window = (AddMemberWindow) ob;
		nonEmptyRule();
		zipNumeric();
	}

	private void nonEmptyRule() throws Group2Exception {
		if(window.getMemberID().trim().isEmpty() ||
				window.getFirstName().trim().isEmpty() ||
				window.getLastName().trim().isEmpty() ||
				window.getPhone().trim().isEmpty() ||
				window.getStreet().trim().isEmpty() ||
				window.getCity().trim().isEmpty() ||
				window.getZipCode().trim().isEmpty() ||
				window.getStateString().trim().isEmpty()
		) {
			throw new Group2Exception("Fields cannot be empty!");
		}
	}

	private void zipNumeric() throws Group2Exception {
		try {
			Integer.parseInt(window.getZipCode().trim());
		}
		catch (NumberFormatException e) {
			throw new Group2Exception("Zip code have to be numeric!");
		}
	}
	
}
