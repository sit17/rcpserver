package com.i5i58.apis.constants;

/**
 * Define common result code
 * 
 * @author frank
 *
 */
public enum ResultCode {
	SUCCESS("success"), 
	SERVICE_ERROR("service_error"), 
	PARAM_INVALID("param_invalid"), 
	GUARD_RIGHT("guard_right"),
	VIP_RIGHT("vip_right"),
	UPDATE_NOW_GIFT_CONFIG("update_now_gift_config"), 
	IGOLD_NOT_ENOUGH("igold_not_enough"), 
	TOKEN_INVALID("token_invalid"), 
	AUTH("no_auth"), 
	DIFF_SPACE_LOGIN("diff_space_login"),
	REQUEST_TRANSPOND_FAILED("request_transpond_failed"),
	FOUND_LIVE_ERROR("find_live_error");

	private String code;

	private ResultCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
