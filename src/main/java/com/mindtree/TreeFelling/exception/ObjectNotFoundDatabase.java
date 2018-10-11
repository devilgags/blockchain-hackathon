/**
 * 
 */
package com.mindtree.TreeFelling.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mindtree.TreeFelling.errorcodes.ErrorMessages;

public class ObjectNotFoundDatabase extends ServiceException {

	/**
	 * 
	 */
	private static final Logger logger = LoggerFactory.getLogger(ObjectNotFoundDatabase.class);

	private static final long serialVersionUID = 1L;

	public ObjectNotFoundDatabase(String message) {

		super(ErrorMessages.Exception_Data_Not_Present);
		logger.error(message);
	}

}