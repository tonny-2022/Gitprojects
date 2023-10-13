package business;

import java.io.Serializable;

public class Group2Exception extends Exception implements Serializable {

	public Group2Exception() {
		super();
	}
	public Group2Exception(String msg) {
		super(msg);
	}
	public Group2Exception(Throwable t) {
		super(t);
	}
	private static final long serialVersionUID = 8978723266036027364L;
	
}
