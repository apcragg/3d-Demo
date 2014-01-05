package engine.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import engine.main.Game;
import engine.math.Transform;
import engine.math.Vector3f;
import engine.polygons.StandardMesh;
import engine.util.ObjectLoader;

public class RenderHelper
{
	private static StandardMesh quad;

	public RenderHelper()
	{
		setUp();
	}

	public void setUp()
	{
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glEnable(GL_DEPTH_CLAMP);
		glEnable(GL_CULL_FACE);
		glFrontFace(GL_CW);
		
		quad = new StandardMesh();
		quad.addVertices(ObjectLoader.loadOBJ("/res/OBJ/plane.obj"));
		quad.setTranslation(new Vector3f(0f, 50f, 70f));
		quad.setRotation(new Vector3f(90f, 90f, 0f));
		quad.setScale(30f);
		quad.setTextureScale(1f);
		quad.formMesh();		
	}

	public static void clear()
	{
		// glClearColor(0f, .5f, .5f, 1f);
		glClearColor(1f, 1f, 1f, 1f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public static void setBackfaceCulling(boolean b)
	{
		if (b) glEnable(GL_CULL_FACE);
		else glDisable(GL_CULL_FACE);
	}
	
	public static void renderQuad(int textureId)
	{
		RenderHelper.setBackfaceCulling(false);
		
		Game.setShader(Game.QUAD);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, textureId);		
		
		Game.getShader().uniformData4f("viewSpace", Transform.viewSpace());
		Game.getShader().uniformData4f("projectedSpace", Transform.perspectiveMatrix());
		
		quad.render();
		
		Game.setShader(Game.PHONG);
			
		glBindTexture(GL_TEXTURE_2D, 0);
		 
	}
}
