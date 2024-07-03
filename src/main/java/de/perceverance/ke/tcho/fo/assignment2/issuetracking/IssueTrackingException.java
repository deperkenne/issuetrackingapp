package de.perceverance.ke.tcho.fo.assignment2.issuetracking;

public class IssueTrackingException extends Exception {

	private static final long serialVersionUID = 1L;

	public IssueTrackingException() {
		super();
	}

	public IssueTrackingException(String message) {
		super(message);
	}

	public IssueTrackingException(String message, Throwable cause) {
		super(message, cause);
	}

	public IssueTrackingException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IssueTrackingException(Throwable cause) {
		super(cause);
	}
}
