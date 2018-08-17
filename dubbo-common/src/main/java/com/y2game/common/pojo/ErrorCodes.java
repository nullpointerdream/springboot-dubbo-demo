package com.y2game.common.pojo;

import java.util.Objects;

public enum ErrorCodes {


    PARAM_ERROR(1005,"参数不能为空"),
    JSON_ERROR(2,"JSON转换失败"),
    USER_NOT_FOUND_ERROR(2,"用户不存在"),
    USER_ERROR(2,"账号或密码错误"),
	REMAIN_ERROR(2,"鸡蛋份数不足"),
    OPENID_ERROR(0,"获取openid失败"),
    SERVICE_ERROR(0,"服务端内部错误"),
    THIRD_PARTY_ERROR(0,"远程服务错误:"),
	TEL_ERROR(2,"手机号不能为空:"),
	LOGIN_ERROR(2,"用户登录已过期:"),
	DECRYPT_ERROR(1006,"解密失败:"),
	VERIFY_MOBILE_CODE_ERROR(1001,"验证码错误");


	private Integer code;
	private String errorInfo;

	ErrorCodes(Integer code, String errorInfo){
		this.code = code ;
		this.errorInfo = errorInfo;
	}

	public int getErrorCode() {
		return code;
	}

	public String getInfo() {
		return toString();
	}

	@Override
	public String toString() {
		return errorInfo;
	}

	public static ErrorCodes fromErrorCode(Integer code){
		for (ErrorCodes error : ErrorCodes.values()) {
			if (Objects.equals(code,error.getErrorCode())) {
				return error;
			}
		}
		return null;
	}

}
