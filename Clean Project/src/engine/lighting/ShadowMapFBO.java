package engine.lighting;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class ShadowMapFBO
{
	private int fbo;
	private int d_texture;
	private int width, height;

	
	public void initializeFBO()
	{
		fbo = glGenFramebuffers();		
		d_texture = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, d_texture);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, null);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	}
}
