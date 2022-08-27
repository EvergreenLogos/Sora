package com.egl.sora.server;

public class ServerPlayer {

	Channel chan;
	
	String username;
	Room currentRoom;
	World currentWorld;
	float x;
	float y;
	float dx;
	float dy;
	
	int id;
	
	float speed;
	
	boolean[] pressedKeys;
	
	public ServerPlayer(Channel chan, String username, Room room) {
		this.chan = chan;
		this.username = username;
		this.currentRoom = room;
		this.id = chan.getConnection().getID();
		
		speed = 10;
		
		x=0;
		y=0;
		dx=0;
		dy=0;
		
		pressedKeys = new boolean[4];
		pressedKeys[0] = false; // W
		pressedKeys[1] = false; // A
		pressedKeys[2] = false; // S
		pressedKeys[3] = false; // F
	}
	
	public String getUsername() {
		return username;
	}
	
	public Room getRoom() {
		return currentRoom;
	}
	
	public Channel getChannel() {
		return chan;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setDX(float dx) {
		this.dx = dx;
	}
	
	public void setDY(float dy) {
		this.dy = dy;
	}
	
	public void update() {
		if (pressedKeys[0]) {
			dy+=speed;
		} else if (pressedKeys[1]) {
			dx-=speed;
		} else if (pressedKeys[2]) {
			dy-=speed;
		} else if (pressedKeys[3]) {
			dx+=speed;
		}
		
		x+=dx;
		y+=dy;
		
		dx=0;
		dy=0;
	}
	
	public void setWorld(World world) {
		this.currentWorld = world;
	}
	
	public void setRoom(Room room) {
		this.currentRoom = room;
	}
	
	public void keyDown(int id) {
		pressedKeys[id] = true;
	}
	
	public void keyUp(int id) {
		pressedKeys[id] = false;
	}
}
