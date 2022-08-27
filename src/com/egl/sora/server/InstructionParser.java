package com.egl.sora.server;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.mapeditor.core.Map;
import org.mapeditor.core.TileSet;
import org.mapeditor.io.TMXMapWriter;

public class InstructionParser {
	System sys;

	public InstructionParser(System sys) {
		this.sys = sys;
	}
	
	public void parseInstruction(Instruction ins) {
		//System.log("parsing instruction");
		//if (ins.getPayload()==null) {
		//	return;
		//}
		/*String[] split = ins.getPayload().split("\\s");

        String cmd = split[0];
        String[] args = new String[split.length-1];
        if (args.length > 0) {
        	java.lang.System.arraycopy(split, 1, args, 0, args.length);
        }*/
		
		//System.log(""+ins.code);
        
        Packet response = new Packet();
        
        switch (ins.code) {
        case 0: {// ping
        	//response.setType(Packet.QUERY);
        	response.setCode(0);
        	ins.getSender().send(response);
        	break;
        }
        case 1: {// print
        	/*StringBuilder sb = new StringBuilder();
        	for (String s : args) {
        		sb.append(s);
        	}
        	System.log(sb.toString());*/
        	break;
        }
        case 3: {// login
        	String [] query = (String[]) ins.getPayload();
        	System.login(query[0], ins.getSender());
        	break;
        }
        case 4: {// keyboard input
        	int[] code = (int[]) ins.getPayload(); // {pressed/release, key code}
        	int presscode = code[0]; // 0 = pressed, 1 = release
        	int keycode = code[1];
        	ServerPlayer pl = ins.getSender().getPlayer();
        	switch(keycode) {
        	case 0: // W
        		//System.log("Player pressed w");
        		//ins.getSender().getPlayer().setDY(-30);
        		if (presscode == 0) {
        			pl.keyDown(0);
        		} else {
        			pl.keyUp(0);
        		}
        		break;
        	case 1: // A
        		//System.log("Player pressed a");
        		//ins.getSender().getPlayer().setDX(-30);
        		if (presscode == 0) {
        			pl.keyDown(1);
        		} else {
        			pl.keyUp(1);
        		}
        		break;
        	case 2: // S
        		//System.log("Player pressed s");
        		//ins.getSender().getPlayer().setDY(30);
        		if (presscode == 0) {
        			pl.keyDown(2);
        		} else {
        			pl.keyUp(2);
        		}
        		break;
        	case 3: // D
        		//System.log("Player pressed d");
        		//ins.getSender().getPlayer().setDX(30);
        		if (presscode == 0) {
        			pl.keyDown(3);
        		} else {
        			pl.keyUp(3);
        		}
        		break;
        	default:
        		break;
        	}
        	break;
        }
        case 5: { // player request position
        	float x = ins.getSender().getPlayer().getX();
        	float y = ins.getSender().getPlayer().getY();
        	//response.setType(Packet.QUERY);
        	response.setCode(5);
        	float [] pos = {x, y};
        	response.setObject(pos);
        	ins.getSender().send(response);
        	break;
        }
        case 6: { // player request room
    		System.log("getmyroom");
    		
    		Room room = ins.getSender().getPlayer().getRoom();
    		Map map = room.getMap();
    		//System.log("Sending "+room.getX()+" "+room.getY());
    		if (map==null) {
    			System.log("Its null on server");
    		}
    		
    		ArrayList<String> mapObject = new ArrayList<String>();
    		
    		// Write the TMX map into a string
    		
    		TMXMapWriter writer = new TMXMapWriter();
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		
    		try {
				writer.writeMap(room.getMap(), baos);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		String mapString = new String(baos.toByteArray());
    		
    		mapObject.add(mapString);
    		
    		
    		
    		//String data = SerializationUtils.serialize(room.getMap());
    		System.log(mapString);
    		
    		//response.setType(Packet.OBJECT);
    		response.setCode(6);
    		response.setObject(mapObject);
    		ins.getSender().send(response);
    		break;
        }
        case 7: {// request tileset
        	System.log("tileset requested "+ins.getPayload());
        	
        	Room room = ins.getSender().getPlayer().getRoom();
    		Map map = room.getMap();
    		TileSet target = null;
    		
    		/*List<TileSet> tss = map.getTileSets();
    		for (int i=0; i<tss.size(); i++) {
    			if (tss.get(i).getSource().equals((String) ins.getPayload())) {
    				target = tss.get(i);
    				System.log("Tileset found");
    			}
    		}*/
    		
    		String path = (String) ins.getPayload();
    		path = path.replace("file:", "");
    		System.log(path);
    		
    		File tileset = new File(path);
    		String targetString = "";
    		
    		try {
				BufferedReader br = new BufferedReader(new FileReader(tileset));
				
				StringBuilder sb = new StringBuilder();
				String x = "";
				
				while ((x = br.readLine()) != null) {
					sb.append(x);
				}
				
				targetString = sb.toString();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		
    		/*if (target == null) {
    			System.log("Tileset not found");
    			
    			response.setCode(7);
    			ins.getSender().send(response);
    			
    			break;
    		}
    		
    		// Write the Tileset into a string
    		
    		TMXMapWriter writer = new TMXMapWriter();
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		
    		try {
				writer.writeTileset(target, baos);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		String targetString = new String(baos.toByteArray());*/
    		
    		//System.log(targetString);
    		
    		Packet pack = new Packet();
    		pack.setCode(7);
    		pack.setObject(targetString);
    		//System.log((String) pack.getObject());
    		ins.getSender().send(pack);
    		break;
        }
        case 8: { // request tileset image
        	System.log("Image requested: "+ins.getPayload());
        	System.log(java.lang.System.getProperty("user.dir"));
        	
        	String imagePath = (String) ins.getPayload();
        	imagePath = imagePath.replace("../", "");
        	
        	try {
				BufferedImage bi = ImageIO.read(new File("assets/tileset/"+imagePath));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(bi, "png", baos);
				byte[] bytes = baos.toByteArray();
				
				Packet pack = new Packet();
				pack.setCode(8);
				pack.setObject(bytes);
				ins.getSender().send(pack);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	break;
        }
        default: {
        	break;
        }
        }
	}
}
