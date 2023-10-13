package librarysystem.rulesets;

import business.Group2Exception;
import librarysystem.LoginWindow;

import java.awt.*;

/**
 * Rules:
 *  1. Fields cannot be empty!
 */

public class LoginRuleSet implements RuleSet {
		private LoginWindow window;
	@Override
	public void applyRules(Component ob) throws Group2Exception {
		window = (LoginWindow) ob;
		nonEmptyRule();
	}

	private void nonEmptyRule() throws Group2Exception {
		if(window.getUsername().trim().isEmpty() ||
				window.getPassword().trim().isEmpty()
		) {
			throw new Group2Exception("Fields cannot be empty!");
		}
	}


	
}
