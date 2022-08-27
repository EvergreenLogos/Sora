package com.egl.sora.graphics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tilemap implements Serializable {
	
	int height;
	int width;
	
	TileSprite[][] lattice;

	public Tilemap() {
		
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void createLattice() {
		lattice = new TileSprite[width][height];
	}
	
	public void populate() {
		for (int x=0; x<width; x++) {
			
			for (int y=0; y<height; y++) {
				//lattice[x][y] = new TileSprite(1, false, x*32, y*32);
				lattice[x][y].applyTexture();
			}
			
			
		}
	}
	
	public void addTile(TileSprite tile) {
		//lattice[tile.getX()][tile.getY()] = tile;
	}
	
	public TileSprite getTile(int x, int y) {
		return lattice[x][y];
	}
	
	public void draw() {
		for (int x=0; x<width; x++) {
			
			for (int y=0; y<height; y++) {
				lattice[x][y].draw();
			}
		}
	}
}
