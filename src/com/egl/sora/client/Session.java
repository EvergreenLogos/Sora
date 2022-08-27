package com.egl.sora.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.mapeditor.core.Map;
import org.mapeditor.core.MapLayer;
import org.mapeditor.core.Tile;
import org.mapeditor.core.TileLayer;
import org.mapeditor.core.TileSet;

import com.egl.sora.graphics.Sprite;
import com.egl.sora.graphics.Texture;
import com.egl.sora.graphics.TextureManager;
import com.egl.sora.graphics.TileSprite;
import com.egl.sora.graphics.Tilemap;
import com.egl.sora.server.Instruction;
import com.egl.sora.server.Packet;
import com.egl.sora.server.Room;
import com.egl.sora.server.System;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class Session {

	ClientSystem cs;
	
	Client client;
	Thread networkThread;
	
	String username;
	
	//Tilemap currentFG;
	//Tilemap currentBG;
	
	List<TileSprite> passable;
	List<Sprite> impassable;
	
	//Room currentRoom;
	
	Map currentMap;
	
	List<Sprite> sprites;
	
	public static Player player;
	
	Packet getPosRequest;
	
	InstructionParser ip;
	TextureManager tm;
	
	Boolean route;
	String sRoute;
	
	boolean drawMap = false;
	
	Boolean roomChange;
	
	public Session(ClientSystem cs, Client client) {
		this.cs = cs;
		this.client = client;
		
		//currentFG=null;
		//currentBG=null;
		//currentRoom = null;
		
		currentMap = null;
		
		passable = new ArrayList<TileSprite>();
		
		sprites = new LinkedList<Sprite>();
		username = "admin";
		
		player = new Player();
		
		getPosRequest = new Packet();
		getPosRequest.setCode(5);
		
		ip = new InstructionParser(this);
		tm = new TextureManager();
		
		route = false;
		sRoute = "";
		
		roomChange=false;
		
		init();
	}
	
	void init() {
		createListener();
		
		Packet pack = new Packet();
		pack.setCode(3);
		String [] loginQuery = new String[2];
		loginQuery[0] = username; // username
		loginQuery[1] = "12345"; // password
		pack.setObject(loginQuery);
		java.lang.System.out.println("Sending login query");
		send(pack);
	}
	
	void createListener() {
		client.addListener(new Listener() {
			public void connected(Connection connection) {
				
			}
			
			public void received (Connection connection, Object object) {
				if (object instanceof Packet) {
					processPacket((Packet) object);
				}
			}
			
			public void disconnected(Connection connection) {
				
			}
		});
	}
	
	void processPacket(Packet packet) {
		//java.lang.System.out.println("got a packet");
		
		InstructionParser.parseInstruction(new Instruction(Instruction.LOW, packet.getCode(), packet.getObject()));
		
		/*switch (packet.getType()) {
		case Packet.OBJECT:
			//java.lang.System.out.println("alksdfj");
			if (packet.getObject() == null) {
				java.lang.System.out.println(" ITS NULL");
			}
			if (packet.getObject() instanceof Map) {
				java.lang.System.out.println("got a map");
				//openRoom((Room) packet.getObject());
				this.currentMap = (Map) packet.getObject();
				roomChange=true;
			}
			break;
		case Packet.QUERY:
			InstructionParser.parseInstruction(new Instruction(Instruction.LOW, packet.getCode(), packet.getObject()));
			break;
		case Packet.ROUTE:
			String routeS = (String) packet.getObject();
			if (routeS.contains("room")) {
				route = true;
				sRoute = "room";
			}
			break;
		default:
			break;
		}*/
	}
	
	public void loop() {
		
		send(getPosRequest);
		
		TextureManager.processTilesetQueue();
		
		/*if (currentBG!=null) {
			//currentBG.draw();
		} else {
			//Packet pack = new Packet();
			//pack.setType(Packet.QUERY);
			//pack.setObject("print this");
			//client.sendTCP(pack);
		}
		
		if (currentFG!=null) {
			//currentFG.draw();
		}*/
		
		if (currentMap != null) {
			if (roomChange) {
				//loadMap(currentMap);
				roomChange=false;
			}
			//currentMap.draw();
			if (drawMap) {
				for (int i=0; i<passable.size(); i++) {
					passable.get(i).draw();
					//java.lang.System.out.println(passable.get(i).getX()+", "+passable.get(i).getY());
				}
			}
			
		} else if (roomChange == false) {
			Packet pack = new Packet();
			java.lang.System.out.println("room request");
			//pack.setType(Packet.QUERY);
			pack.setCode(6);
			//pack.setObject("getmyroom");
			send(pack);
			roomChange = true;
		}
		
		if (!sprites.isEmpty()) {
			for (Sprite s : sprites) {
				//s.draw();
			}
		}
		
		
		ClientSystem.getCamera().setFocus(player.getX(), player.getY());
		//tilem.draw();
		player.draw();
	}
	
	public void send(Packet packet) {
		client.sendTCP(packet);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void loadMap(Map map) {
		java.lang.System.out.println("loading map");
		currentMap = map;
		
		//java.lang.System.out.println(map.toString());
		
		List<TileSet> tss = map.getTileSets();
		TileSet ts = tss.get(0);
		
		//List<TileLayer> tl = map.getLayers();
		MapLayer ml = map.getLayer(0);
		TileLayer tl = (TileLayer) map.getLayer(0);
		if (tl == null) {
			java.lang.System.out.println("is null");
		}
		//java.lang.System.out.println(tl.getWidth());
		
		int height = map.getHeight();
		int width = map.getWidth();
		
		int tsHeight = (ts.getMaxTileId()/ts.getColumns())+1;
		int tsWidth = ts.getColumns();
		
		float xFactor=(1f/(tsWidth)); // 0.033
		float yFactor=(1f/(tsHeight)); // 0.0625
		
		for (int x=0; x!=width-1; x++) {
			for (int y=0; y!=height-1; y++) {
				//room.getBG().getTile(x,y).applyTexture();
				//java.lang.System.out.println("("+x+" "+y+")");
				//if (tl.getTileAt(101,1) == null) {
					//java.lang.System.out.println("is null");
				//} else {
					//java.lang.System.out.print(tl.getTileAt(x, y).getId());
				//}
				int id = tl.getTileAt(x, y).getId();
				//Tile tile = ts.getTile(id);
				
				int row = (id / tsWidth);
				int col = (id % tsWidth);
				
				//tsprite.setTextureOriginX(col);
				//tsprite.setTextureOriginY(row);
				
				//java.lang.System.out.println("ID: "+id);
				//java.lang.System.out.println("Height/width: "+tsHeight+", "+tsWidth);
				//java.lang.System.out.println("Col and Row: "+col+", "+row);
				
				TileSprite tsprite = new TileSprite(x, y, "building", id, (float) (col)/(tsWidth), (float) (row)/(tsHeight), xFactor, yFactor, true);
				
				tsprite.setTexture(TextureManager.getTileset("building"));
				passable.add(tsprite);
				
				drawMap = true;
			}
		}
		
		route=false;
	}
}
