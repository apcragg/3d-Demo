package engine.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.HashMap;
import java.util.Map;

import engine.main.Window;

public class FramebufferHelper
{
	private int fbo, width, height, renderBuffer;
	private Map<Integer, Integer> textures;
	
	public FramebufferHelper()
	{
		setTextures(new HashMap<Integer, Integer>());
	}
	
	public int generateFramebuffer(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		fbo = glGenFramebuffers();
		
		return fbo;
	}
	
	public void attatchTexture(int attachmentPoint, int texture)
	{
		textures.put(attachmentPoint, texture);
		
		bind();
		
		glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, attachmentPoint, GL_TEXTURE_2D, texture, 0);
		
		if(attachmentPoint != GL_DEPTH_ATTACHMENT)
		{
			glDrawBuffer(attachmentPoint);
		}
		
		unBind();
	}
	
	public void attachDepthRenderbuffer()
	{
		bind();
		
		renderBuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, renderBuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
		glFramebufferRenderbuffer(GL_DRAW_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderBuffer);
	}
	
	public void bind()
	{
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
	}
	
	public void unBind()
	{
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	public void writeBind()
	{
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fbo);
		
		glViewport(0,0, width, height);
		
		glClearColor(.41f, .804f, 1f, 1f);
		glClearDepth(1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);	
	}
	
	public void readBind(int attachmentPoint, int textureIndex)
	{
		glActiveTexture(GL_TEXTURE0 + textureIndex);
		glBindTexture(GL_TEXTURE_2D, textures.get(attachmentPoint));	  	
	}
	
	public void readUnBind()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void writeUnBind()
	{		
		glViewport(0,0, Window.WIDTH, Window.HEIGHT);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);	
	}
	
	public void setDimensions(int h, int w)
	{
		height = h;
		width = w;
	}

	public Map<Integer, Integer> getTextures()
	{
		return textures;
	}

	public void setTextures(Map<Integer, Integer> textures)
	{
		this.textures = textures;
	}
}
