package com.egl.sora.server;

public class Packet {
	
	int code;
	Object object = null;

	public Packet() {
		
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public void setObject(Object object) {
		this.object = object;
	}
	
	public Object getObject() {
		return object;
	}
}
