package engine.renderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.List;

import engine.main.Game;
import engine.polygons.Vertex;
import engine.util.BufferHelper;

public class ScreenQuadHelper
{
	private int abo, vbo, tbo;
	private float s0, s1;
	private int texture;
	
	public ScreenQuadHelper(int texture, float s0, float s1)
	{
		this.texture = texture;
		this.s0 = s0;
		this.s1 = s1;
		
		setup();
	}
	
	private void setup()
	{
		abo = glGenVertexArrays();
		
		vbo = glGenBuffers();
		tbo = glGenBuffers();
		
		List<Vertex> vList = new ArrayList<Vertex>();
		vList.add(new Vertex(0f, 1f, 0f, 0f, 1f));
		vList.add(new Vertex(0f, 0f, 0f, 0f, 0f));
		vList.add(new Vertex(1f, 0f, 0f, 1f, 0f));
		
		vList.add(new Vertex(0f, 1f, 0f, 0f, 1f));
		vList.add(new Vertex(1f, 0f, 0f, 1f, 0f));
		vList.add(new Vertex(1f, 1f, 0f, 1f, 1f));
		
		glBindVertexArray(abo);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, BufferHelper.createVertexFloatArray(vList) , GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
		glBufferData(GL_ARRAY_BUFFER, BufferHelper.createVertexFloatArray(vList) , GL_STATIC_DRAW);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
	}
	
	public void render(int shader)
	{		
		Game.setShader(shader);
		
		Game.getShader().uniformData1f("scale0", s0);
		Game.getShader().uniformData1f("scale1", s1);
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture);
		
		glEnableClientState(GL_VERTEX_ARRAY);
		glBindVertexArray(abo);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glDrawArrays(GL_TRIANGLES, 0, 6);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindVertexArray(abo);
				
		glBindTexture(GL_TEXTURE_2D, 0);
		glActiveTexture(0);
		
		Game.setShader(Game.PHONG);
	}

	public void setTexture(int texture)
	{
		this.texture = texture;
	}
}
