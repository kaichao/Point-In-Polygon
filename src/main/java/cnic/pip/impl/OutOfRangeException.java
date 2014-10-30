package cnic.pip.impl;

public class OutOfRangeException extends RuntimeException {
	private static final long serialVersionUID = 1786628269832811487L;

	public OutOfRangeException() {
	}

	public OutOfRangeException(String message) {
		super(message);
	}

	public OutOfRangeException(Throwable cause) {
		super(cause);
	}

	public OutOfRangeException(String message, Throwable cause) {
		super(message, cause);
	}

	public OutOfRangeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
