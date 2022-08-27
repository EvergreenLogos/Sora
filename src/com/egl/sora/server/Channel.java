package com.egl.sora.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class Channel {
	
	Connection con;
	Thread thread;
	
	String ip;
	int id;
	
	String route;
	ServerPlayer player;

	public Channel(Connection con) {
		this.con = con;
		this.ip = con.getRemoteAddressTCP().toString();
		this.id = con.getID();
		
		open();
	}
	
	public void open() {
		System.log("Opening new channel with IP "+ip);
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				//System.log("Started the thread");
				con.addListener(new Listener() {
					public void connected(Connection connection) {
						System.log(connection.getRemoteAddressTCP().toString());
						System.log(con.getRemoteAddressTCP().toString());
					}
					
					@Override
					public void received(Connection connection, Object object) {
						//System.log("Received something.");
						if (object instanceof Packet) {
							//System.log("got a packet from "+ip);
							processPacket((Packet) object);
						}
					}
				});
			}
		});
		
		thread.run();
	}
	
	public void close() {
		con.close();
	}
	
	public Connection getConnection() {
		return con;
	}
	
	void processPacket(Packet packet) {
		//System.log("channel processing packet.");
		System.putInstruction(new Instruction(Instruction.LOW, packet.getCode(), packet.getObject(), this));
		
		/*switch (packet.getType()) {
		case Packet.OBJECT:
			break;
		case Packet.QUERY:
			//System.log("its a query");
			System.putInstruction(new Instruction(Instruction.LOW, packet.getCode(), packet.getObject(), this));
			break;
		case Packet.ROUTE:
			route = (String) packet.getObject();
			break;
		default:
			break;
		}*/
	}
	
	public void send(Packet packet) {
		con.sendTCP(packet);
	}
	
	public void setPlayer(ServerPlayer player) {
		this.player = player;
	}
	
	public ServerPlayer getPlayer() {
		return player;
	}
}
