/**
 * 
 */
package com.searchpicto.ws.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 
 * Test class for {@link MediaService}.
 *
 * @author carol
 *
 */

@ExtendWith(MockitoExtension.class)
class MediaServiceTest {

	/**
	 * The service to test.
	 */
	@InjectMocks
	private PictoService pictoService = new PictoServiceImpl();

	/**
	 * Tests the method getPictoById with a working Picto.
	 */
	@Test
	void sometest() {
		assertTrue(true);
	}
}
