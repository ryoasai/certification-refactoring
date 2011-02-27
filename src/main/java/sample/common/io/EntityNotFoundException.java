package sample.common.io;

import sample.common.SystemException;


public class EntityNotFoundException extends SystemException {
	private static final long serialVersionUID = 1L;
	
	public EntityNotFoundException() {
		super();
	}

	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityNotFoundException(String message) {
		super(message);
	}

	public EntityNotFoundException(Throwable cause) {
		super(cause);
	}

}
