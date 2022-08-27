package com.egl.sora.graphics;

import java.io.Serializable;

public class TileSprite extends Sprite {
	
	boolean passable;
	
	String tileset;
	int id;
	
	public TileSprite(int x, int y, String tileset, int id, float textureOriginX, float textureOriginY, float texFactorX, float texFactorY, boolean stat) {
		super(x*32, y*32, 32, 32, textureOriginX, textureOriginY, texFactorX, texFactorY, true);
		this.x = x;
		this.y = y;
		this.tileset = tileset;
		this.id = id;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

	public void applyTexture() {
		//sprite = new Sprite(x*32, y*32, 32, 32, true);
		//sprite.setTexture(TextureManager.getTexture(id));
	}
}
