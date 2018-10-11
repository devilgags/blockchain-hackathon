/**
 * 
 */
package com.mindtree.TreeFelling.exception;

import com.mindtree.TreeFelling.errorcodes.ErrorMessages;



public class ControllerException extends BaseException {


	private static final long serialVersionUID = -1877185212504939047L;

	public ControllerException(String message) {
		super(ErrorMessages.Exception_Data_Not_Present);
		
	}

}
