package com.egl.sora.client;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.mapeditor.core.ImageData;
import org.mapeditor.core.Map;
import org.mapeditor.core.TileSet;
import org.mapeditor.io.TMXMapReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.egl.sora.graphics.Texture;
import com.egl.sora.graphics.TextureManager;
import com.egl.sora.server.Instruction;
import com.egl.sora.server.Packet;

public class InstructionParser {
	static Session session;

	public InstructionParser(Session session) {
		this.session = session;
	}
	
	public static void parseInstruction(Instruction ins) {
		//System.log("parsing instruction");
		if (ins==null) {
			return;
		}
		/*String[] split = ins.split("\\s");

        String cmd = split[0];
        String[] args = new String[split.length-1];
        if (args.length > 0) {
        	java.lang.System.arraycopy(split, 1, args, 0, args.length);
        }*/
        
        Packet response = new Packet();
        
        switch (ins.getCode()) {
        case 5: {// update position, in response to packet sent with code 5
        	float[] pos = (float[]) ins.getPayload();
        	//session.getPlayer().setX(pos[0]); // x
        	//session.getPlayer().setY(pos[1]); // y
        	//java.lang.System.out.println("got position: "+pos[0]+", "+pos[1]);
        	session.getPlayer().setPos(pos[0], pos[1]);
        	break;
        }
        case 6: { // receive a map
        	ArrayList<String> mapObject = (ArrayList<String>) ins.getPayload();
        	
        	TMXMapReader reader;
        	Map map = new Map();
			try {
				reader = new TMXMapReader();
				
				InputStream is = new ByteArrayInputStream(mapObject.get(0).getBytes());
				
				try {
					map = reader.readMap(is);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			List<TileSet> tss = map.getTileSets();
			for (int i=0; i<tss.size(); i++) {
				//tss.get(i).getSource();
				java.lang.System.out.println("Requesting the tileset "+tss.get(0).getSource());
				Packet pack = new Packet();
				pack.setCode(7);
				pack.setObject(tss.get(i).getSource());
				session.send(pack);
			}
        	
        	//session.loadMap(map);
			session.currentMap = map;
        	break;
        }
        case 7: { // receive tileset
        	java.lang.System.out.println(ins.getPayload().toString());
        	String tilesetString = (String) ins.getPayload();
        	
        	TileSet ts = new TileSet();
        	
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        	try {
				dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
				
				DocumentBuilder db = dbf.newDocumentBuilder();
				
				InputStream is = new ByteArrayInputStream(tilesetString.getBytes());
				
				Document doc = db.parse(is);
				
				NodeList list = doc.getElementsByTagName("image");
				Element element = (Element) list.item(0);
				String str = element.getAttribute("source");
				//java.lang.System.out.println(str);
				
				java.lang.System.out.println("Requesting the image "+str);
	        	
	        	Packet pack = new Packet();
	        	pack.setCode(8);
	        	pack.setObject(str);
	        	session.send(pack);
				
				
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	
        	
        	/*TMXMapReader reader;
			try {
				reader = new TMXMapReader();
				
				InputStream is = new ByteArrayInputStream(tilesetString.getBytes());
				
				try {
					ts = reader.readTileset(is);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
        	
        	break;
        }
        case 8: { // receive tileset image
        	java.lang.System.out.println("Received tileset image");
        	
        	byte[] bytes = (byte[]) ins.getPayload();
        	InputStream bais = new ByteArrayInputStream(bytes);
        	try {
				BufferedImage bi = ImageIO.read(bais);
				
				TextureManager.queueTileset(bi, "building");
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	break;
        }
        default:
        	break;
        }
	}
}
