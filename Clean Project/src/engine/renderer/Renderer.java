package engine.renderer;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;

import java.nio.ByteBuffer;
import java.util.List;

import org.lwjgl.opengl.GL11;

import engine.levels.Level;
import engine.lighting.Light;
import engine.lighting.LightingHandler;
import engine.lighting.ShadowSpotLight;
import engine.main.Game;
import engine.main.Window;
import engine.math.Transform;
import engine.polygons.StandardMesh;

public class Renderer
{
	private int renderTarget0, renderTarget1;
	private FramebufferHelper screenRender;
	
	public Renderer()
	{
		setupRenderTarget();
	}
	
	public void startRender()
	{
		bind();
		preRender();
	}
	
	public void endRender()
	{
		unBind();
		renderToScreen();
	}
	
	public void shadowRender(LightingHandler lights, List<StandardMesh> meshes)
	{		
		for(Light l :  lights.getShadowSpotLights())
		{
//			RenderHelper.setBackfaceCulling(true);
//			GL11.glCullFace(GL11.GL_FRONT_FACE);
			
			ShadowSpotLight light = ((ShadowSpotLight) l);
			
			light.lightSpaceUpdate();
			light.getLightMap().writeBind();
			
			Game.setShader(Game.SHADOW);
			Game.getShader().uniformData4f("lightSpace", Transform.lightSpace());
			
			for(StandardMesh m : meshes) m.render();			
			light.getLightMap().writeUnBind();		
			
			RenderHelper.setBackfaceCulling(false);
			GL11.glCullFace(GL11.GL_BACK);
//			GaussianBlur.blurTexture(light.getLightMap().getC_texture(), light.getLightMap().getC_texture_copy(), ShadowSpotLight.size,  ShadowSpotLight.size, true);
		}	
	}
	
	public void render(LightingHandler lights, List<StandardMesh> meshes)
	{
		bind();
		
		for(int i = 0; i < lights.getShadowSpotLights().size(); i++)
		{
			((ShadowSpotLight) lights.getShadowSpotLights().get(i)).lightSpaceUpdate();
			Game.getShader().uniformData4f("lightSpace[" + i + "]", Transform.lightSpace());	
			
			//shadow
			((ShadowSpotLight) (lights.getShadowSpotLights().get(i))).getLightMap().readBind(i);
		}		

		//render
		for(StandardMesh m : meshes) m.render();
		
		unBind();
	}
	
	private void preRender()
	{
		RenderHelper.setBackfaceCulling(true);
		
		//pre-render
		RenderHelper.clear();
		
		//shader uniform updating
		Transform.setupPerspective(Game.getFOV(), 1000f);
		
		Game.setShader(Game.PHONG);
		Game.getShader().uniformData4f("viewSpace", Transform.viewSpace());
		Game.getShader().uniformData4f("projectedSpace", Transform.perspectiveMatrix());
		
		//updates the default material (not sure if this is necessary)
		Level.defaultMaterial.update();
	}
		
	private void renderToScreen()
	{
		RenderHelper.setBackfaceCulling(false);		
		RenderHelper.renderFullscreenQuad(renderTarget0, Game.SCREEN_QUAD);
	}
	
	private void setupRenderTarget()
	{		
		renderTarget0 = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, renderTarget0);
				
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Window.WIDTH, Window.HEIGHT, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);	
		
		renderTarget1 = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, renderTarget1);
				
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Window.WIDTH, Window.HEIGHT, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);	
		
		screenRender = new FramebufferHelper();
		screenRender.generateFramebuffer(Window.WIDTH, Window.HEIGHT);
		screenRender.attatchTexture(GL_COLOR_ATTACHMENT0, renderTarget0);
		screenRender.attachDepthRenderbuffer();
	}
	
	public void bind()
	{
		screenRender.writeBind();
	}
	
	public void unBind()
	{
		screenRender.writeUnBind();
	}
}
