package com.egl.sora.server;

import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_ProfileRGB;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.mapeditor.core.Compression;
import org.mapeditor.core.Data;
import org.mapeditor.core.Encoding;
import org.mapeditor.core.ImageData;
import org.mapeditor.core.MapLayer;
import org.mapeditor.core.Orientation;
import org.mapeditor.core.RenderOrder;
import org.mapeditor.core.StaggerAxis;
import org.mapeditor.core.StaggerIndex;
import org.mapeditor.core.TileLayer;
import org.mapeditor.core.TileSet;
import org.mapeditor.util.BasicTileCutter;
import org.mapeditor.util.TileCutter;

import com.egl.sora.graphics.TileSprite;
import com.egl.sora.graphics.Tilemap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.KryoSerialization;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import sun.awt.image.IntegerInterleavedRaster;
import sun.java2d.StateTrackable;
import sun.java2d.StateTrackableDelegate;

public class System {
	
	boolean mainLoopRunning;
	
	Server server;
	
	static List<Channel> channels;
	static List<ServerPlayer> players;
	static HashMap<String, World> worlds;
	
	static ConcurrentLinkedQueue<Instruction> instructions;
	
	InstructionParser ip;
	
	int port;

	public System() {
		port = 7000;
		mainLoopRunning = true;
	}
	
	void init() {
		log("Starting Sora server on port "+port);
		
		server = new Server(10000000, 1000000);
		
		Kryo kryo = server.getKryo();
		
		
		server.start();
		try {
			server.bind(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log("ERROR: Unable to bind to port");
			e.printStackTrace();
		}
		
		kryo.register(Packet.class);
		kryo.register(LinkedList.class);
		kryo.register(ArrayList.class);
		kryo.register(HashMap.class);
		kryo.register(int[][].class);
		kryo.register(int[].class);
		kryo.register(String[].class);
		kryo.register(float[].class);
		kryo.register(short[].class);
		kryo.register(byte[].class);
		
		kryo.register(org.mapeditor.core.Map.class);
		kryo.register(MapLayer.class);
		kryo.register(MapLayer[].class);
		kryo.register(TileLayer.class);
		kryo.register(TileLayer[].class);
		kryo.register(TileSet.class);
		kryo.register(TileSet[].class);
		kryo.register(TileSprite.class);
		kryo.register(TileSprite[].class);
		kryo.register(Data.class);
		kryo.register(Properties.class);
		kryo.register(Compression.class);
		kryo.register(Encoding.class);
		kryo.register(Orientation.class);
		kryo.register(RenderOrder.class);
		kryo.register(StaggerAxis.class);
		kryo.register(StaggerIndex.class);
		kryo.register(ImageData.class);
		kryo.register(TreeMap.class);
		
		
		instructions = new ConcurrentLinkedQueue<Instruction>();
		
		channels = new LinkedList<Channel>();
		players = new LinkedList<ServerPlayer>();
		worlds = new HashMap<String, World>();
		
		ip = new InstructionParser(this);
		
		createListener();
		
		mainLoop();
	}
	
	void createListener() {
		server.addListener(new Listener() {
			public void connected(Connection connection) {
				newConnection(connection);
			}
			
			/*public void received (Connection connection, Object object) {
				
				if (object instanceof Packet) {
					Packet request = (Packet) object;
				}
			}*/
			
			public void disconnected(Connection connection) {
				log("a client disconnected");
				
			}
		});
	}
	
	void mainLoop() {
		int x = 0;
		
		World newWorld = new World(); // WorldGenerator.generate("test", 1, 10, 10);
		worlds.put("test", newWorld);
		
		Room newRoom = new Room(0);
		newRoom.setMap("untitled.tmx");  // change to real file
		newWorld.putRoom(newRoom);
		
		// DONT PUT ANYTHING BELOW THIS LINE
		
		/*long time = java.lang.System.currentTimeMillis();
		long now = time;
		
		if (now >= time+16) {
			time = java.lang.System.currentTimeMillis();
			now = time;
		}*/

		while(mainLoopRunning) {
			
			//if (now == time+16) {
				//log("I am running");
				
				while (!instructions.isEmpty()) {
					//log("instructions is not empty");
					Instruction current = instructions.remove();
					
					ip.parseInstruction(current);
				}
				
				if (!worlds.isEmpty()) {
					for (Map.Entry w : worlds.entrySet()) {
						World world = (World) w.getValue();
						world.update();
					}
				}
			
				if (!players.isEmpty()) {
					for (ServerPlayer p : players) {
						p.update();
						
						//Packet pack = new Packet();
						//pack.setType(Packet.QUERY);
						//pack.setObject("pos "+p.getX()+" "+p.getY());
						//p.getChannel().send(pack);
					}
				}
				
				//time = java.lang.System.currentTimeMillis();
				//now = time;
			//}
			
			//now = java.lang.System.currentTimeMillis();
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	void newConnection(Connection con) {
		log("Incoming new connection from IP "+con.getRemoteAddressTCP().toString());
		channels.add(new Channel(con));
	}
	
	synchronized static void login(String username, Channel chan) {
		log("Player "+username+" has logged in.");
		Room room = worlds.get("test").getRoom(0);
		ServerPlayer sp = new ServerPlayer(chan, username, room);
		sp.setWorld(worlds.get(0));
		players.add(sp);
		chan.setPlayer(sp);
	}
	
	synchronized static void putInstruction(Instruction ins) {
		//log("putting instruction");
		instructions.add(ins);
		//log("Queue is now sized "+instructions.size());
		//log("is empty? "+instructions.isEmpty());
	}
	
	public static void log(String text) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		
		java.lang.System.out.print("["+dtf.format(now)+"] "+text+"\n");
	}
	
	public static int cantorPair(int a, int b) {
		int x = (int) (((0.5)*(a+b)*(a+b+1))+b);
		System.log(a+", "+b+" -> "+x);
		return x;
	}
}
