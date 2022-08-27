package com.egl.sora.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryUtil;

import com.egl.sora.client.ClientSystem;

public class Sprite {
	
	float x;
	float y;
	int height;
	int width;
	
	int vaoId;
	int vboId;
	int eboId;
	
	Texture texture;
	
	float[] vertices;
	int[] indices;
	
	boolean stat;
	
	float textureOriginX;
	float textureOriginY;
	float texFactorX;
	float texFactorY;
	
	public Sprite(float x, float y, int height, int width, float textureOriginX, float textureOriginY, float texFactorX, float texFactorY, boolean stat) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.stat = stat;
		//java.lang.System.out.println(textureOriginX+", "+textureOriginY);
		this.textureOriginX = textureOriginX;
		this.textureOriginY = textureOriginY;
		this.texFactorX = texFactorX;
		this.texFactorY = texFactorY;
		
		//float xFactor=(32f/(30f*32f)); // 0.033
		//float yFactor=(32f/(16f*32f)); // 0.0625
		//java.lang.System.out.println("Factor: "+texFactorX+", "+texFactorY);
		
		/*vertices = new float[] {
				x, y, 					this.textureOriginX, this.textureOriginY,
				x, y+height, 			this.textureOriginX, this.textureOriginY+(factor),
				x+width, y+height, 		this.textureOriginX+(factor), this.textureOriginY+(factor),
				x+width, y, 			this.textureOriginX+(factor), this.textureOriginY
		};*/
		
		vertices = new float[] {
				x, y, 					textureOriginX, textureOriginY,
				x, y+height, 			textureOriginX, textureOriginY+texFactorY,
				x+width, y+height, 		textureOriginX+texFactorX, textureOriginY+texFactorY,
				x+width, y, 			textureOriginX+texFactorX, textureOriginY
		};
		
		indices = new int[] {
			0, 1, 3,
			1, 2, 3
		};
		
		vaoId = glGenVertexArrays();
		vboId = glGenBuffers();
		eboId = glGenBuffers();
		
		glBindVertexArray(vaoId);
		
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 16, 0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 16, 8);
		glEnableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0); // unbind the vbo
		glBindVertexArray(0);
		
		/*vertices = new float[] {
			x, y,
			x, y+height,
			x+width, y+height,
			x+width, y
		};*/
		
		/*float vertices[] = {
			    -0.5f, -0.5f, 0.0f,
			     0.5f, -0.5f, 0.0f,
			     0.0f,  0.5f, 0.0f
			};  */
		
		/*FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
		verticesBuffer.put(vertices).flip();
		
		//vaoId = glGenVertexArrays();
		//glBindVertexArray(vaoId);
		
		vboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		
		//if (stat) {
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		//} else {
		//	glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_DYNAMIC_DRAW);
		//}
		
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(0, 4, GL_FLOAT, false, 16, 0);
		
		// unbind the VBO and VAO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		
		if (verticesBuffer != null) {
		    MemoryUtil.memFree(verticesBuffer);
		}  // maybe change this so that we have two VBO's, one for position the other for texture, then have a boolean static
		   // that will send more position VBO's every update if set to false
		
		glBindVertexArray(vaoId);
		
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 4, GL_FLOAT, false, 16, 0);
		glEnableVertexAttribArray(0);*/
	}
	
	private float normalize(int num) {
		if (num == 0) {
			return 0;
		} else {
			java.lang.System.out.println("Normalizing: "+num+" to "+(1/(float) num));
			return (float) 1/(float) num;
		}
	}
	
	public void setTextureOriginX(int x) {
		textureOriginX = normalize(x);
		java.lang.System.out.println(textureOriginX);
	}
	
	public void setTextureOriginY(int x) {
		textureOriginY = normalize(x);
	}

	public void draw() {
		
		//glBegin(GL_QUADS);
		
		//glTexCoord2f(0, 0);
		//glVertex2f(x,y);
		
		//glTexCoord2f(0, 1);
		//glVertex2f(x,y+height);
		
		//glTexCoord2f(1, 1);
		//glVertex2f(x+width,y+height);
		
		//glTexCoord2f(1, 0);
		//glVertex2f(x+width,y);
		
		//glEnd();
		//System.out.println(vaoId);
		
		
		
		/*if (!stat) {
			vertices = new float[] {
					x, y, 					textureOriginX, textureOriginY,
					x, y+height, 			textureOriginX, textureOriginY+1,
					x+width, y+height, 		textureOriginX+1, textureOriginY+1,
					x+width, y, 			textureOriginX+1, textureOriginY
			};
			
			FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
			verticesBuffer.put(vertices).flip();
			
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_DYNAMIC_DRAW);
			
			glEnableVertexAttribArray(0);
			
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
			
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}*/
		
		glBindTexture(GL_TEXTURE_2D, texture.getID());
		
		glBindVertexArray(vaoId);
		
	    // Draw the vertices
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
		//glDrawArrays(GL_QUADS, 0, 4);
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
	    
	    //glActiveTexture(GL_TEXTURE0);
		//glBindTexture(GL_TEXTURE_2D, texture.getID());
		
		//ClientSystem.getShaderProgram().setUniform1f("image", texture.getID());

	    // Restore state
	    glBindVertexArray(0);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
		
		//java.lang.System.out.println("set pos called");
		
		vertices = new float[] {
				x, y, 					textureOriginX, textureOriginY,
				x, y+height, 			textureOriginX, textureOriginY+texFactorY,
				x+width, y+height, 		textureOriginX+texFactorX, textureOriginY+texFactorY,
				x+width, y, 			textureOriginX+texFactorX, textureOriginY
		};
		
		indices = new int[] {
			0, 1, 3,
			1, 2, 3
		};
		
		/*FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
		verticesBuffer.put(vertices).flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_DYNAMIC_DRAW);
		
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 16, 0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 16, 8);
		glEnableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);*/
		
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 16, 0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 16, 8);
		glEnableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0); // unbind the vbo
		glBindVertexArray(0);
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}
