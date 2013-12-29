package engine.lighting;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL13;

import engine.main.Main;
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
	}

	public void initializeFBO()
	{
		fbo = glGenFramebuffers();		
		d_texture = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, d_texture);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
		
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, d_texture, 0);
		
		glDrawBuffer(GL_NONE);
		glReadBuffer(GL_NONE);	
		
		int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
		
		if(status != GL_FRAMEBUFFER_COMPLETE)
		{
			LogHelper.printError("Could not intialize framebuffer.");
			Main.quit();
		}
	}
	
	public void writeBind()
	{
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fbo);
	}
	
	/**
	 * Binds the framebuffer as a texture to be read by the shader.
	 * @param textureSlot Specifies the GL_TEXTUREi slot the framebuffer will be read from.
	 */Fpublic void readBind(int textureSlot)
	{
		glActiveTexture(GL_TEXTURE0 + textureSlot);
		glBindTexture(GL_TEXTURE_2D, d_texture);
	}
}
