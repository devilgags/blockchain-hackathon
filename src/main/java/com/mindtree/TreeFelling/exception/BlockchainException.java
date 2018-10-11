/**
 * 
 */
package com.mindtree.TreeFelling.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mindtree.TreeFelling.errorcodes.ErrorMessages;


public class BlockchainException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(BlockchainException.class);

	/**
	 * @param message
	 */
	public BlockchainException(String message) {
		super(ErrorMessages.Exception_IN_BLOCKCHAIN);
		logger.error(message);
	}

}
