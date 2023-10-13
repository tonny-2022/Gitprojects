package librarysystem.rulesets;

import business.Group2Exception;

import java.awt.*;

public interface RuleSet {
	public void applyRules(Component ob) throws Group2Exception;
}
