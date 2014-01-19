package engine.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import engine.main.Game;

public class GaussianBlur
{
	private static FramebufferHelper ping = new FramebufferHelper();
	private static FramebufferHelper pong = new FramebufferHelper();
	
	public GaussianBlur()
	{
		ping.generateFramebuffer(0, 0);
		pong.generateFramebuffer(0, 0);
	}
	
	public static int blurTexture(int texture, int textureCopy, int height, int width, boolean shadow)
	{		
		ping.setDimensions(height, width);
		pong.setDimensions(height, width);
		
		ping.bind();
		ping.attatchTexture(GL_COLOR_ATTACHMENT0, textureCopy);
		ping.writeBind();
		
		ScreenQuadHelper quad = new ScreenQuadHelper(texture, 2f, -1f);
		quad.render(shadow ? Game.SHADOW_BLUR_H : Game.GAUSSIN_H);
		
		ping.writeUnBind();
		ping.readBind(GL_COLOR_ATTACHMENT0, 0);
		
		pong.bind();
		pong.attatchTexture(GL_COLOR_ATTACHMENT0, texture);
		pong.writeBind();
		
		quad = new ScreenQuadHelper(textureCopy, 2f, -1f);
		quad.render(shadow ? Game.SHADOW_BLUR_V : Game.GAUSSIN_V);
		
		pong.writeUnBind();
		
		Game.setShader(Game.PHONG);
		
		return texture;
	}
}
