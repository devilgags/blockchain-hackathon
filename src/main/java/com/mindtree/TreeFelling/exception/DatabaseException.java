/**
 * 
 */
package com.mindtree.TreeFelling.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mindtree.TreeFelling.errorcodes.ErrorMessages;



public class DatabaseException extends ServiceException {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseException.class);

	private static final long serialVersionUID = 1L;

	public DatabaseException(String message) {

		super(ErrorMessages.Exeption_In_Database);
		logger.error(message);

	}

}