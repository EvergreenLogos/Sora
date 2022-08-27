package com.egl.sora.graphics;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.mapeditor.core.TileSet;

import com.egl.sora.graphics.*;

public class TextureManager {

	static HashMap<Integer, Texture> textures;
	static BlockingQueue<Integer> queue;
	
	static ConcurrentHashMap<String, BufferedImage> tilesetQueue;
	
	static HashMap<String, Texture> tilesets;
	
	public TextureManager() {
		textures = new HashMap<Integer, Texture>();
		tilesets = new HashMap<String, Texture>();
		
		tilesetQueue = new ConcurrentHashMap<String, BufferedImage>();
	}
	
	public synchronized static void queueTileset(BufferedImage bi, String name) {
		java.lang.System.out.println("puttingi nque");
		tilesetQueue.put(name, bi);
	}
	
	public synchronized static void processTilesetQueue() {
		if (!tilesetQueue.isEmpty()) {
			for (Map.Entry<String, BufferedImage> entry : tilesetQueue.entrySet()) {
				putTilesetIfNotExists(entry.getValue(), entry.getKey());
				java.lang.System.out.println("theres somethingi n queue");
			}
			tilesetQueue.clear();
		}
	}
	
	public synchronized static void putIfNotExists(int id) {
		if (!textures.containsKey(id)) {
			Texture tex = new Texture("assets/"+id+".png");
			textures.put(id, tex);
		}
	}
	
	public synchronized static Texture getTexture(int id) {
		if (textures.containsKey(id)) {
			return textures.get(id);
		} else {
			putIfNotExists(id);
			return getTexture(id);
		}
	}
	
	public synchronized static Texture getTexture(String path) {
		Texture tex = new Texture(path);
		return tex;
	}
	
	public synchronized static void putTilesetIfNotExists(BufferedImage bi, String name) {
		if (!tilesets.containsKey(name)) {
			Texture tex = new Texture(bi);
			tilesets.put(name, tex);
		}
	}
	
	public synchronized static Texture getTileset(String name) {
		if (tilesets.containsKey(name)) {
			return tilesets.get(name);
		}
		return null;
	}
}
