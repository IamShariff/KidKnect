package com.kidknect.responsedto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for generating custom ResponseEntity objects with response
 * messages and HTTP status codes.
 */
public class ResponseHandler {

	/**
	 * Generate a ResponseEntity with a custom message, HTTP status, and a response
	 * object.
	 *
	 * @param <T>         The type of the response object.
	 * @param message     The custom message to include in the response.
	 * @param status      The HTTP status to set for the response.
	 * @param responseObj The response object to include in the response.
	 * @return A ResponseEntity containing the custom message, HTTP status, and
	 *         response object.
	 */
	@SuppressWarnings("unchecked")
	public static <T> ResponseEntity<T> generateResponse(String message, HttpStatus status, T responseObj) {
		Map<String, Object> map = new HashMap<>();
		map.put("message", message);
		map.put("status", status);
		map.put("data", responseObj);
		return new ResponseEntity<>((T) map, status);
	}

	/**
	 * Generate a ResponseEntity with a custom message and HTTP status.
	 *
	 * @param <T>     The type of the response object.
	 * @param message The custom message to include in the response.
	 * @param status  The HTTP status to set for the response.
	 * @return A ResponseEntity containing the custom message and HTTP status.
	 */
	@SuppressWarnings("unchecked")
	public static <T> ResponseEntity<T> generateResponse(String message, HttpStatus status) {
		Map<String, Object> map = new HashMap<>();
		map.put("message", message);
		map.put("status", status);
		return new ResponseEntity<>((T) map, status);
	}

}