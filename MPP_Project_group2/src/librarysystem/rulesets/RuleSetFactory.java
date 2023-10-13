package librarysystem.rulesets;

import librarysystem.*;

import java.awt.*;
import java.util.HashMap;

final public class RuleSetFactory {
	private RuleSetFactory(){}
	static HashMap<Class<? extends Component>, RuleSet> map = new HashMap<>();

	static {
		map.put(BookCheckoutWindow.class, new BookCheckoutRuleSet());
		map.put(AddMemberWindow.class, new AddMemberRuleSet());
		map.put(LoginWindow.class, new LoginRuleSet());
		map.put(AddBookCopyWindow.class, new AddBookCopyRuleSet());
		map.put(AddBookWindow.class, new AddBookRuleSet());
		map.put(MemberCheckoutRecordWindow.class, new MemberCheckoutRuleSet());
		map.put(BookDueDateWindow.class, new BookDueDateRuleSet());
	}
	public static RuleSet getRuleSet(Component c) {
		Class<? extends Component> cl = c.getClass();
		if(!map.containsKey(cl)) {
			throw new IllegalArgumentException(
					"No RuleSet found for this Component");
		}
		return map.get(cl);
	}
}
