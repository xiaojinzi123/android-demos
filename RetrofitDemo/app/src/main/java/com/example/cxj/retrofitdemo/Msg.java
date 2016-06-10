package com.example.cxj.retrofitdemo;

public class Msg {

	public static final String OK = "ok";

	public static final String ERROR = "error";

	/**
	 * 只有两个值<br>
	 * 1."ok"<br>
	 * 2."error"
	 */
	private String msg = ERROR;

	/**
	 * 信息的文本
	 */
	private String msgText = ERROR;

	private Object data;

	public Msg() {
		super();
	}

	public Msg(String msg, Object data) {
		super();
		this.msg = msg;
		this.data = data;
	}

	public Msg(String msg, String msgText, Object data) {
		super();
		this.msg = msg;
		this.msgText = msgText;
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsgText() {
		return msgText;
	}

	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}

	@Override
	public String toString() {
		return "Msg [msg=" + msg + ", msgText=" + msgText + ", data=" + data + "]";
	}
}
