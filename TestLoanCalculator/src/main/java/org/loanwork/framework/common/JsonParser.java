package org.loanwork.framework.common;

import com.google.gson.Gson;

/**
 * Wrapper class for serializing and deserializing operations with gson library
 * 
 * @author Sabahattin Kasapoglu
 *
 */
public class JsonParser {

	private Gson gson = new Gson();;

	/**
	 * Generic parser method to convert the response json format to object model
	 * 
	 * @param response
	 * @param clazz    object class to map
	 * @return the bind object
	 */
	public Object deserializeResponse(String response, Class<?> clazz) {
		return gson.fromJson(response, clazz);
	}

	/**
	 * Generic parser method to convert the request from object to json model
	 * 
	 * @param clazz generic object
	 * @return json representation for the request
	 */
	public String serializeRequest(Object clazz) {
		return gson.toJson(clazz);
	}

}
