/**
 * 
 */
package com.searchpicto.ws.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.searchpicto.ws.exception.PictoNotFoundException;

/**
 * 
 * The advice when a {@link PictoNotFoundException} is thrown.
 * 
 * @author carol
 *
 */
@ControllerAdvice
public class PictoNotFoundAdvice {

	/**
	 * The handler when a {@link PictoNotFoundException} is thrown.
	 * @param ex The exception thrown.
	 * @return The message.
	 */
	@ResponseBody
	@ExceptionHandler(PictoNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String pictoNotFoundHandler(PictoNotFoundException ex) {
		return ex.getMessage();
	}
}
