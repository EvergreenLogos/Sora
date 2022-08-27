package com.egl.sora.server;

public class Instruction {
	
	public static final int LOW = 0;
	public static final int MEDIUM = 1;
	public static final int HIGH = 2;
	
	Object payload;
	int priority;
	int code;
	Channel sender = null;
	
	public Instruction(int priority, int code, Object payload, Channel sender) {
		this.priority = priority;
		this.code = code;
		this.payload = payload;
		this.sender = sender;
	}
	
	public Instruction(int priority, int code, Object payload) {
		this.priority = priority;
		this.code = code;
		this.payload = payload;
	}
	
	public Object getPayload() {
		return payload;
	}
	
	public int getCode() {
		return code;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public Channel getSender() {
		return sender;
	}
}
