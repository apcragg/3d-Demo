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
	private int fbo;
	private int d_texture;

	private int width, height;
	
	public ShadowMapFBO(int width, int height) 
	{
		this.width = width;
		this.height = height;
		
		initializeFBO();
	}

	public void initializeFBO()
	{
		//creates and binds a new framebuffer
		fbo = glGenFramebuffers();				
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		
		//creates the color texture and initializes its parameters
		d_texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, d_texture);
				
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);	
		glTexParameteri (GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_NONE);
		
		//creates the depth renderbuffer to allow for depth testing
		int depthrenderbuffer;
		depthrenderbuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, depthrenderbuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthrenderbuffer);	
		
		//binds the COLOR0 attachment to the draw framebuffer
		glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, d_texture, 0);	
		glDrawBuffer(GL_COLOR_ATTACHMENT0);	
				
		//Checks for framebuffer completeness
		int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);		
		if(status != GL_FRAMEBUFFER_COMPLETE)
		{
			LogHelper.printError("Could not intialize framebuffer.");
			Main.quit();
		}
		
	}
	
	public void writeBind()
	{
		//binds the framebuffer for drawing
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fbo);
		
		//sets the viewport to the bound texture size
		glViewport(0,0, width, height);
		
		//clears the framebuffer from the previous draw
		glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
			
	}
	
	/**
	 * Binds the framebuffer as a texture to be read by the shader.
	 * @param textureSlot Specifies the GL_TEXTUREi slot the framebuffer will be read from.
	 */
	
	public void readBind()
	{	
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, d_texture);	    
	}
	
	public void readUnBind()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void writeUnBind()
	{
		glViewport(0,0, Window.HEIGHT, Window.WIDTH);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);	
	}
	
	public int getD_texture()
	{
		return d_texture;
	}
}
