package engine.lighting;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL42;

import engine.main.Main;
import engine.main.Window;
import engine.util.LogHelper;

public class ShadowMapFBO
{
	private int fbo, d_texture, c_texture;

	private int width, height;
	
	/**
	 * Creates and intiialzies a new framebuffer with color and depth texture attachments.
	 * @param width Texture width
	 * @param height Texture height
	 */
	
	public ShadowMapFBO(int width, int height) 
	{
		this.width = width;
		this.height = height;
		
		initializeFBO();
	}

	private void initializeFBO()
	{
		//creates and binds a new framebuffer
		fbo = glGenFramebuffers();				
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		
		//creates the color texture and initializes its parameters
		c_texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, c_texture);
				
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);	
		glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_NONE);
		
		//creates the depth texture
		d_texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, d_texture);
				
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);	
		glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_NONE);	
		
		//binds the COLOR0 attachment to the draw framebuffer
		glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, c_texture, 0);	
		glDrawBuffer(GL_COLOR_ATTACHMENT0);	
		
		//binds the Depth attachment to the draw framebuffer
		glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, d_texture, 0);	
				
		//Checks for framebuffer completeness
		int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);		
		if(status != GL_FRAMEBUFFER_COMPLETE)
		{
			LogHelper.printError("Could not intialize framebuffer.");
			Main.quit();
		}
		
	}
	
	/**
	 * Binds the framebuffer from drawing, sets the viewport to the texture size, and clears the frame buffer
	 * of previous contents
	 */
	
	public void writeBind()
	{
		//binds the framebuffer for drawing
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fbo);
		
		//sets the viewport to the bound texture size
		glViewport(0,0, width, height);
		
		//clears the framebuffer from the previous draw
		glClearColor(0f, 0f, 0f, 0f);
		glClearDepth(1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
			
	}
	
	/**
	 * Binds the framebuffer as a texture to be read by the shader.
	 * @param textureSlot Specifies the GL_TEXTUREi slot the framebuffer will be read from.
	 */
	
	public void readBind(int offset)
	{	
		glActiveTexture(GL_TEXTURE3 + 4 + offset);
		glBindTexture(GL_TEXTURE_2D, c_texture);	  
		
		glActiveTexture(GL_TEXTURE3 + offset);
		glBindTexture(GL_TEXTURE_2D, d_texture);	
	}
	
	/**
	 * Unbinds the texture. not entirely necessary but it's being safe.
	 */
	
	public static void readUnBind()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	/**
	 * Resets the viewport to the normal window and unbinds the framebuffer from drawing.
	 */
	
	public void writeUnBind()
	{
		glViewport(0,0, Window.HEIGHT, Window.WIDTH);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);	
	}
	
	/**
	 * Returns the texture id of the depth texture.
	 * @return Depth texture id
	 */
	
	public int getD_texture()
	{
		return d_texture;
	}

	/**
	 * Returns the texture id of the color0 texture.
	 * @return Color0 texture id
	 */
	
	public int getC_texture()
	{
		return c_texture;
	}
}
