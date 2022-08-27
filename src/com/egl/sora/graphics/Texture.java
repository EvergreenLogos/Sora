package com.egl.sora.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

import com.egl.sora.client.ClientSystem;
import com.egl.sora.graphics.PNGDecoder;

public class Texture {
	
	File file;
	
	int height;
	int width;
	
	int id;

	public Texture() {
		
	}
	
	public Texture(String path) {
		loadFromFile(path);
	}
	
	public Texture(BufferedImage image) {
		loadFromImage(image);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
		
	public void loadFromImage(BufferedImage image) {
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		width = image.getWidth();
		height = image.getHeight();
		image.getRGB(0, 0, width, height, pixels, 0, image.getWidth());
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
		
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		
		buffer.flip();
		
		glEnable(GL_TEXTURE_2D);
		
		id = glGenTextures();
		java.lang.System.out.println(id);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, id);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		glGenerateMipmap(GL_TEXTURE_2D);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	}
	
	public void loadFromFile(String path) {
		file = new File(path);
		InputStream input = null;
		PNGDecoder decoder = null;
		ByteBuffer buffer = null;
		
		try {
			input = new FileInputStream(file);
			
			decoder = new PNGDecoder(input);
			
			height = decoder.getHeight();
			width = decoder.getWidth();
			
			final int bpp = 4;
			
			//buffer = BufferUtils.createByteBuffer(bpp * width * height);
			buffer = ByteBuffer.allocateDirect(bpp * decoder.getWidth() * decoder.getHeight());
			
			decoder.decode(buffer, width * bpp, PNGDecoder.Format.RGBA);
			
			buffer.flip();
			
			input.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		glEnable(GL_TEXTURE_2D);
		
		id = glGenTextures();
		java.lang.System.out.println(id);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, id);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		
		//ClientSystem.getShaderProgram().setUniform1f("image", id);
		
		// texutre parameters
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		// upload the byte buffer to the texture
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		glGenerateMipmap(GL_TEXTURE_2D);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	}
	
	public int getID() {
		return id;
	}
}
