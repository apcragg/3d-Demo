package world.scenery;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL15;

import engine.main.Game;
import engine.math.Matrix4f;
import engine.math.Transform;
import engine.math.Vector3f;
import engine.polygons.StandardMesh;
import engine.polygons.Vertex;

public class SceneryPool 
{
	private int vbo, nbo, tbo, ubo, abo, ibo, size;
	private List<Vertex> vertices;
	private FloatBuffer[] buffers;
	
	public SceneryPool()
	{
		vertices = new LinkedList<Vertex>();
		
		abo = glGenVertexArrays();
	}
	
	public void update()
	{
		
	}
	
	public void render()
	{
		Transform.setTranslation(new Vector3f());
		Transform.setRotation(new Vector3f());
		Transform.setScale(1f);
		
		Game.shader.uniformData4f("worldSpace", Transform.spatialMatrix());	
		
		glEnableClientState(GL_VERTEX_ARRAY);
		glBindVertexArray(abo);	
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);

		glDrawElements(GL_TRIANGLES, size * 3, GL_UNSIGNED_INT, 0);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		glDisableVertexAttribArray(0);	
		glDisableVertexAttribArray(1);	
		glDisableVertexAttribArray(2);	
		glDisableVertexAttribArray(3);
		glBindVertexArray(0);
	}
	
	public void formMesh()
	{
		size = vertices.size();
		
		glBindVertexArray(abo);
		
		System.out.println(size * 3 * 4);
		
		buffers = new FloatBuffer[] {engine.util.BufferHelper.createVertexFloatArray(vertices), engine.util.BufferHelper.createVertexUVFloatArray(vertices),
									 engine.util.BufferHelper.createVertexNormalFloatArray(vertices), engine.util.BufferHelper.createVertexTangnetFloatArray(vertices)};
		
		vbo = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffers[0], GL_STATIC_DRAW);
		glVertexAttribPointer(0 , 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		ubo = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER, ubo);
		glBufferData(GL_ARRAY_BUFFER, buffers[1], GL_STATIC_DRAW);
		glVertexAttribPointer(1 , 2, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		nbo = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER, nbo);
		glBufferData(GL_ARRAY_BUFFER, buffers[2], GL_STATIC_DRAW);
		glVertexAttribPointer(2 , 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		tbo = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
		glBufferData(GL_ARRAY_BUFFER, buffers[3], GL_STATIC_DRAW);
		glVertexAttribPointer(3 , 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
		
		vertices = null;
		
		List<Integer> ints = new LinkedList<Integer>();
		
		for(int i = 0; i < size; i++)
		{
			ints.add(i);
		}
		
		ibo = glGenBuffers();
		
		System.out.println("IBO: " + ibo);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, engine.util.BufferHelper.createIntBuffer(ints), GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public void reformMesh()
	{	
		delete();	
		formMesh();
	}
	
	public void delete()
	{
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(ibo);
		GL15.glDeleteBuffers(abo);
		GL15.glDeleteBuffers(nbo);
		GL15.glDeleteBuffers(tbo);
		GL15.glDeleteBuffers(ubo);
		
		for(FloatBuffer b : buffers)
		{
			b.clear();
			b = null;
		}
	}
	
	public void addMesh(StandardMesh m)
	{	
		m.updateTransforms();
		
		Matrix4f mat = Transform.spatialMatrix();
			
		for(Vertex v : m.getVertices())
		{		
			Vertex temp = new Vertex(v.getPosition(), v.getU(), v.getV(), v.getNormal());
			temp.setTangent(v.getTangent());
	
			Vector3f pos = mat.mul(temp.getPosition());

			temp.setPos(pos);
							
			vertices.add(temp);	
		}
		
		mat = null;
		m.setVertices(null);

	}
}
