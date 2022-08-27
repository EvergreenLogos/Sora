package com.egl.sora.graphics;

public class Camera {
	
	float focusX;
	float focusY;
	
	float originX;
	float originY;
	
	int windowWidth;
	int windowHeight;
	
	float zoomFactor;
	
	public Camera(float focusX, float focusY, int windowWidth, int windowHeight) {
		this.focusX = focusX;
		this.focusY = focusY;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		
		this.zoomFactor = 1;
		
		calculateOrigin();
	}
	
	public float getFocusX() {
		return focusX;
	}
	
	public float getFocusY() {
		return focusY;
	}
	
	public float getZoom() {
		return zoomFactor;
	}
	
	public void calculateOrigin() {
		originX = focusX;
		originY = focusY;
		System.out.println("Focus: "+focusX+" "+focusY);
		System.out.println("Calculated origin: "+originX+" "+originY);
	}

	public void move(float dx, float dy) {
		focusX += dx;
		focusY += dy;
		
		calculateOrigin();
	}
	
	public void zoom(float zoom) {
		zoomFactor += zoom;
		calculateOrigin();
	}
	
	public void setFocus(float x, float y) {
		focusX = x;
		focusY = y;
		//calculateOrigin();
	}
}
