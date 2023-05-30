/**
 * 
 */
package com.searchpicto.ws.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.searchpicto.ws.exception.PictoIndexingException;

/**
 * 
 * The advice when a {@link PictoIndexingException} is thrown.
 * 
 * @author carol
 *
 */
@ControllerAdvice
public class PictoIndexingAdvice {

	/**
	 * The handler when a {@link PictoIndexingException} is thrown.
	 * @param ex The exception thrown.
	 * @return The message.
	 */
	@ResponseBody
	@ExceptionHandler(PictoIndexingException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String PictoIndexingHandler(PictoIndexingException ex) {
		return ex.getMessage();
	}
}
