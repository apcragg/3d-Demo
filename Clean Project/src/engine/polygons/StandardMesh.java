package engine.polygons;

import java.util.LinkedList;
import java.util.List;

import engine.main.Game;
import engine.materials.Material;
import engine.math.Matrix4f;
import engine.math.Vector3f;
import engine.renderer.Camera;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class StandardMesh implements Mesh
{
	private int vbo, nbo, tbo, ubo, abo, ibo, size;
	private List<Vertex> vertices;
	private float textureScale;
	
	private Vector3f translation, rotation, scale;
	private String material;
	 
	public StandardMesh()
	{
		vertices		= new LinkedList<Vertex>();
		translation 	= new Vector3f();
		rotation 		= new Vector3f();
		scale 			= new Vector3f(1f, 1f, 1f);
		textureScale	= 1f;
		
		setup();
	}
	
	public void setup()
	{		
		material = "default";
		abo = glGenVertexArrays();
	}

	public void render()
	{
		updateUniforms();
		
		glEnableClientState(GL_VERTEX_ARRAY);
		glBindVertexArray(abo);	
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
			
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glDrawElements(GL_TRIANGLES, vertices.size() * 3, GL_UNSIGNED_INT, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		glDisableVertexAttribArray(0);	
		glDisableVertexAttribArray(1);	
		glDisableVertexAttribArray(2);	
		glDisableVertexAttribArray(3);
		glBindVertexArray(0);
	}

	public void update()
	{	
		
	}
	
	public void updateTransforms()
	{
		Transform.setRotation(rotation);
		Transform.setScale(scale);
		Transform.setTranslation(translation);
	}
	
	public void updateUniforms()
	{		
		Material.getMaterial(material).update();
		
		updateTransforms();
		
		Game.shader.uniformData4f("worldSpace", Transform.spatialMatrix());		
		Game.shader.uniformData1f("textureScale", textureScale);
	}

	public void addVertices(List<Vertex> v)
	{	
		for(Vertex vert : v)
		{
			vertices.add(vert);
			//System.out.println(vert.getPosition());
		}
	}
	
	public void formMesh()
	{	
		glBindVertexArray(abo);
		
		vbo = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, engine.util.BufferHelper.createVertexFloatArray(vertices), GL_STATIC_DRAW);
		glVertexAttribPointer(0 , 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		ubo = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER, ubo);
		glBufferData(GL_ARRAY_BUFFER, engine.util.BufferHelper.createVertexUVFloatArray(vertices), GL_STATIC_DRAW);
		glVertexAttribPointer(1 , 2, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		nbo = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER, nbo);
		glBufferData(GL_ARRAY_BUFFER, engine.util.BufferHelper.createVertexNormalFloatArray(vertices), GL_STATIC_DRAW);
		glVertexAttribPointer(2 , 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		tbo = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
		glBufferData(GL_ARRAY_BUFFER, engine.util.BufferHelper.createVertexTangnetFloatArray(vertices), GL_STATIC_DRAW);
		glVertexAttribPointer(3 , 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
		
		List<Integer> ints = new LinkedList<Integer>();
		
		for(int i = 0; i < vertices.size(); i++)
		{
			ints.add(i);
		}
		
		ibo = glGenBuffers();
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, engine.util.BufferHelper.createIntBuffer(ints), GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public float getAbsoluteWidth()
	{
		float max = 0.0f, min = 0.0f;
		
		//finds max
		for(Vertex v : vertices)
		{
			max = max < v.getPosition().getX() ? v.getPosition().getX() : max;
		}
		
		for(Vertex v : vertices)
		{
			min = min > v.getPosition().getX() ? v.getPosition().getX() : min;
		}
		
		return Math.abs(max - min);
	}
	
	public float getAbsoluteDepth()
	{
		float max = 0.0f, min = 0.0f;
		
		//finds max
		for(Vertex v : vertices)
		{
			max = max < v.getPosition().getZ() ? v.getPosition().getZ() : max;
		}
		
		for(Vertex v : vertices)
		{
			min = min > v.getPosition().getZ() ? v.getPosition().getZ() : min;
		}
		
		return Math.abs(max - min);
	}
	
	public float getAbsoluteHeight()
	{
		float max = 0.0f, min = 0.0f;
		
		//finds max
		for(Vertex v : vertices)
		{
			max = max < v.getPosition().getY() ? v.getPosition().getY() : max;
		}
		
		for(Vertex v : vertices)
		{
			min = min > v.getPosition().getY() ? v.getPosition().getY() : min;
		}
		
		return Math.abs(max - min);
	}
	
	/*
	 * Transformation accessors
	 */
	
	public void translate(Vector3f v)
	{
		translation.add(v);
	}
	
	public void rotate(Vector3f v)
	{
		rotation.add(v);
	}
	
	public void sclae(Vector3f v)
	{
		scale.add(v);
	}
	
	public void scale(float f)
	{
		scale.add(new Vector3f(f, f, f));
	}

	public void setTranslation(Vector3f translation)
	{
		this.translation = translation;
	}

	public void setRotation(Vector3f rotation)
	{
		this.rotation = rotation;
	}

	public void setScale(float scale)
	{
		this.scale = new Vector3f(scale, scale, scale);
	}
	
	public void setScale(Vector3f scale)
	{
		this.scale = scale;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public float getTextureScale() {
		return textureScale;
	}

	public void setTextureScale(float textureScale) {
		this.textureScale = textureScale;
	}
	
	public List<Vertex> getVertices() {
		return vertices;
	}

	public void setVertices(List<Vertex> vertices) {
		this.vertices = vertices;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void test()
	{
		for(Vertex v: vertices)
		{
			Matrix4f m = new Matrix4f().rotate(new Vector3f(0f, 1f, 0f));
			
			v.setPos(m.mul(v.getPosition()));
		}
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferSubData(GL_ARRAY_BUFFER, 0L, engine.util.BufferHelper.createVertexFloatArray(vertices));
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
}
