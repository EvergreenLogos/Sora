package com.egl.sora.client;

import com.egl.sora.graphics.Sprite;
import com.egl.sora.graphics.Texture;
import com.egl.sora.graphics.TextureManager;

public class Player {

	Sprite sprite;
	
	float x;
	float y;
	
	float lastX;
	float lastY;
	
	float dx;
	float dy;
	
	public Player() {
		sprite = new Sprite(0f, 0f, 64, 64, 0, 0, 1, 1, false);
		
		sprite.setTexture(TextureManager.getTexture("assets/tux.png"));
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
		ClientSystem.getCamera().setFocus(x, y);
	}
	
	public void setDX(float dx) {
		this.dx = dx;
	}
	
	public void setDY(float dy) {
		this.dy = dy;
	}
	
	public float getDX() {
		return dx;
	}
	
	public float getDY() {
		return dy;
	}
	
	public void draw() {
		x+=dx;
		y+=dy;
		
		if (x != lastX || y != lastY) {
			//java.lang.System.out.println("players position changed");
			
			sprite.setPos(x, y);
			ClientSystem.getCamera().setFocus(x, y);
			
			dx=0;
			dy=0;
		}
		
		sprite.draw();
		
		lastX = x;
		lastY = y;
	}
}
