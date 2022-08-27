package com.egl.sora.server;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.mapeditor.core.Map;
import org.mapeditor.io.MapReader;
import org.mapeditor.io.TMXMapReader;

import com.egl.sora.graphics.Tilemap;

public class Room implements Serializable {
	
	int id;
	
	Map map;
	
	boolean shouldUpdate;
	
	//int x;
	//int y;
	
	public Room(int id) {
		this.id = id;
		shouldUpdate = false;
	}
	
	public Map getMap() {
		return this.map;
	}
	
	public void setMap(String tmxFile) {
		TMXMapReader mr;
		try {
			mr = new TMXMapReader();
			try {
				map = mr.readMap(tmxFile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.log("loaded map");
	}
	
	public void update() {
		
	}
}
