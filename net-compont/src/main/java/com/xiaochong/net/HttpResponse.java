package com.xiaochong.net;

import java.io.Serializable;

/**
 * Created by anylife.zlb@gmail.com on 2016/7/11.
 */
public class HttpResponse<T> implements Serializable {
	private String code;
	private String msg;
	private T data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("{");
		sb.append("\"code\":\"")
				.append(code).append('\"');
		sb.append(",\"msg\":\"")
				.append(msg).append('\"');
		sb.append(",\"data\":")
				.append(data);
		sb.append('}');
		return sb.toString();
	}
}
