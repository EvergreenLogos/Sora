package com.egl.sora.client;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL40.glUniform1f;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_ProfileRGB;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import sun.awt.image.IntegerInterleavedRaster;
import sun.java2d.StateTrackable;
import sun.java2d.StateTrackableDelegate;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.mapeditor.core.Compression;
import org.mapeditor.core.Data;
import org.mapeditor.core.Encoding;
import org.mapeditor.core.ImageData;
import org.mapeditor.core.Map;
import org.mapeditor.core.MapLayer;
import org.mapeditor.core.Orientation;
import org.mapeditor.core.RenderOrder;
import org.mapeditor.core.StaggerAxis;
import org.mapeditor.core.StaggerIndex;
import org.mapeditor.core.TileLayer;
import org.mapeditor.core.TileSet;
import org.mapeditor.util.BasicTileCutter;
import org.mapeditor.util.TileCutter;

import com.egl.sora.graphics.*;
import com.egl.sora.server.Packet;
import com.egl.sora.server.Room;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

public class ClientSystem {
	
	double UPS;
	double FPS;
	
	Client client;
	
	Session currentSession;
	
	static ShaderProgram shaderProgram;
	
	static Camera camera;
	
	static Window window;
	
	Sprite test;
	
	public ClientSystem() {
		
	}
	
	public static ShaderProgram getShaderProgram() {
		return shaderProgram;
	}
	
	public static Camera getCamera() {
		return camera;
	}

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		window = new Window(1280, 960, "Sora Client v1.0", false);
		window.makeVisible();
		
		init();
		loop();
		
		window.destroy();
	}

	private void init() {
		
		//setup the client networking
		
		client = new Client(10000000, 10000000);
		client.start();
		try {
			client.connect(5000, "localhost", 7000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Kryo kryo = client.getKryo();
		
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
		
		
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		
		
		createKeyListener();
		
		
	    
		
		// Create the shade program and load the shaders
		
	    try {
	    	shaderProgram = new ShaderProgram();
			shaderProgram.createFragmentShader(loadResource("graphics/fragment.fs"));
			shaderProgram.createVertexShader(loadResource("graphics/vertex.vs"));
			shaderProgram.link();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    camera = new Camera(0, 0, 1280, 960);
	    
	    // Create the session
	    
	    currentSession = new Session(this, client);
	    
	    //test = new Sprite(0, 0, 64, 64, false);
	    
	    //Texture tex = new Texture();
		//tex.loadFromFile("/home/nnemo/workspace/Sokyu/assets/tux.png");
		//test.setTexture(tex);
	    
	    
	}

	private void loop() {
		//long time = java.lang.System.currentTimeMillis();
		//long now = time;

		
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !window.shouldClose() ) {
			
			//if (now == time+16) {
			
			
			glClear(GL_COLOR_BUFFER_BIT); // clear the framebuffer
			
			//camera.move(2, 0);
			
			shaderProgram.bind();
			
			//System.out.println(camera.getOriginX()+" "+camera.getOriginY());
			
			shaderProgram.setUniform1f("offsetX", camera.getFocusX());
			shaderProgram.setUniform1f("offsetY", camera.getFocusY());
			shaderProgram.setUniform1f("zoomFactor", camera.getZoom()/512);
			
			currentSession.loop();
			//test.setX(test.getX()+10);
			//test.draw();
			//currentSession.getPlayer().draw();
			
			shaderProgram.unbind();

			window.draw();

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
			
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
	
	void createKeyListener() {
		GLFWKeyCallback keyCallback;
		glfwSetKeyCallback(window.getWindowID(), keyCallback = new GLFWKeyCallback() {
		    @Override
		    public void invoke (long window, int key, int scancode, int action, int mods) {
		    	Packet packet = new Packet();
		    	packet.setCode(4);
		    	boolean send = false;
		    	
		    	if (action == GLFW_PRESS)
		    	{
		    		if ( key == GLFW_KEY_W ) {
			    		packet.setObject(new int[] {0, 0}); // {press/release, key id}
			    		currentSession.getPlayer().setDY(30);
			    		send = true;
			    	} else if ( key == GLFW_KEY_A ) {
			    		packet.setObject(new int[] {0, 1});
			    		currentSession.getPlayer().setDX(-30);
			    		send = true;
			    	} else if ( key == GLFW_KEY_S ) {
			    		packet.setObject(new int[] {0, 2});
			    		currentSession.getPlayer().setDY(-30);
			    		send = true;
			    	} else if ( key == GLFW_KEY_D ) {
			    		packet.setObject(new int[] {0, 3});
			    		currentSession.getPlayer().setDX(30);
			    		send = true;
			    	} else if ( key == GLFW_KEY_EQUAL ) {
			    		camera.zoom(0.5f);
			    	} else if ( key == GLFW_KEY_MINUS ) {
			    		camera.zoom(-0.5f);
			    	} else if ( key == GLFW_KEY_L ) {
			    		currentSession.loadMap(currentSession.currentMap);
			    	}
		    	} else if (action == GLFW_RELEASE)
		    	{
		    		if ( key == GLFW_KEY_W ) {
			    		packet.setObject(new int[] {1, 0});
			    		//currentSession.getPlayer().setDY(30);
			    		send = true;
			    	} else if ( key == GLFW_KEY_A ) {
			    		packet.setObject(new int[] {1, 1});
			    		//currentSession.getPlayer().setDX(-30);
			    		send = true;
			    	} else if ( key == GLFW_KEY_S ) {
			    		packet.setObject(new int[] {1, 2});
			    		//currentSession.getPlayer().setDY(-30);
			    		send = true;
			    	} else if ( key == GLFW_KEY_D ) {
			    		packet.setObject(new int[] {1, 3});
			    		//currentSession.getPlayer().setDX(30);
			    		send = true;
			    	}
		    	}
		    	
		    	if (send) {
		    		currentSession.send(packet);
		    	}
		    }
		});
	}
	
	public String loadResource(String path) throws IOException {
	   InputStream is = new FileInputStream(path);
	   BufferedReader buf = new BufferedReader(new InputStreamReader(is));
	   
	   String line = buf.readLine();
	   StringBuilder sb = new StringBuilder();
	   
	   while (line != null) {
		   sb.append(line).append("\n");
		   line = buf.readLine();
	   }
	   
	   buf.close();
	   
	   return sb.toString();
	}
}
