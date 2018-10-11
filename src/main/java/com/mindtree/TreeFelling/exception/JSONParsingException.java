/**
 * 
 */
package com.mindtree.TreeFelling.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mindtree.TreeFelling.errorcodes.ErrorMessages;

public class JSONParsingException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(JSONParsingException.class);

	/**
	 * @param message
	 */
	public JSONParsingException(String message) {
		super(ErrorMessages.Exception_IN_JSONPARSING);
		logger.error(message);
	}

}
