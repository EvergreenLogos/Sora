package com.egl.sora.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class World {
	
	int id;
	String name;
	
	HashMap<Integer,Room> rooms;

	public World() {
		rooms = new HashMap<Integer, Room>();
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void update() {
		if (!rooms.isEmpty()) {
			for (Map.Entry entry : rooms.entrySet()) {
				Room r = (Room) entry.getValue();
				r.update();
			}
		}
		
	}
	
	public void putRoom(Room room) {
		int id = 0;
		rooms.put(id, room);
	}
	
	/*public void save() {
		System.log("Saving all active rooms in world "+name+".");
		
		String wd = java.lang.System.getProperty("user.dir");
		String directory = wd+"/"+name;
		File dir = new File(directory);
		
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		int iteration = 1;
		
		for (Map.Entry entry : rooms.entrySet()) {
			Room r = (Room) entry.getValue();
			int total = rooms.size();
			
			try {
				String x = ""+r.getX();
				if (r.getX()<10) {
					x = "0"+x;
				}
				
				String y = ""+r.getY();
				if (r.getY()<10) {
					y = "0"+y;
				}
				
				String fileName = x+"a"+y+".sr";
				
				FileOutputStream fileOutput = new FileOutputStream(directory+"/"+fileName);
				ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
				objectOutput.writeObject(r);
				objectOutput.close();
				System.log("Room "+fileName+" of world "+name+" successfully written. ("+iteration+"/"+total+")");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			iteration++;
		}
		
		System.log("Successfully saved rooms.");
	}*/
	
	public void clearRooms() {
		rooms.clear();
	}
	
	public Room getRoom(int id) {
		return rooms.get(id);
	}
}
