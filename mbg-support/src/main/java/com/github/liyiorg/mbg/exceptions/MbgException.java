package com.github.liyiorg.mbg.exceptions;

import org.apache.ibatis.exceptions.PersistenceException;

public class MbgException extends PersistenceException {

	private static final long serialVersionUID = -1087064438395590672L;

	public MbgException() {
		super();
	}

	public MbgException(String message) {
		super(message);
	}

	public MbgException(String message, Throwable cause) {
		super(message, cause);
	}

	public MbgException(Throwable cause) {
		super(cause);
	}

}
