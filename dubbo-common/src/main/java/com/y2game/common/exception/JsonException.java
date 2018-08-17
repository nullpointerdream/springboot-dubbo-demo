package com.y2game.common.exception;


import com.y2game.common.pojo.ErrorCodes;

public class JsonException extends BaseException{
	
	private static final long serialVersionUID = 1L;
	
	public JsonException(ErrorCodes code) {
		super(code);
	}

	public static JsonException newInstance(){
		return newInstance(ErrorCodes.JSON_ERROR);
	}
	
	public static JsonException newInstance(ErrorCodes code){
		return new JsonException(code);
	}

}
