package engine.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;

import engine.math.Matrix4f;
import engine.polygons.Vertex;


public class BufferHelper
{

	public static FloatBuffer genFloatBuffer(int size)
	{
		FloatBuffer f = BufferUtils.createFloatBuffer(size);
		
		return f;
	}
	
	public static  FloatBuffer createMatrixFloatArray(Matrix4f m)
	{
		FloatBuffer f = genFloatBuffer(16);
		
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				f.put(m.get(i, j));
			}
		}
		
		f.flip();
		
		return f;
		
	}
	
	public static FloatBuffer createVertexFloatArray(List<Vertex> vList)
	{
		FloatBuffer f = genFloatBuffer(vList.size()*3);
		
		for(Vertex v : vList)
		{
			f.put(v.getX());
			f.put(v.getY());
			f.put(v.getZ());
		}

		f.flip();
		
		return f;
	}
	
	public static FloatBuffer createVertexNormalFloatArray(List<Vertex> vList)
	{
		FloatBuffer f = genFloatBuffer(vList.size()*3);
		
		for(Vertex v : vList)
		{
			f.put(v.getNormal().getX());
			f.put(v.getNormal().getY());
			f.put(v.getNormal().getZ());
		}

		f.flip();
		
		return f;
	}
	
	public static FloatBuffer createVertexTangnetFloatArray(List<Vertex> vList)
	{
		FloatBuffer f = genFloatBuffer(vList.size()*3);
		
		for(Vertex v : vList)
		{
			f.put(v.getTangent().getX());
			f.put(v.getTangent().getY());
			f.put(v.getTangent().getZ());
		}

		f.flip();
		
		return f;
	}
	
	public static FloatBuffer createVertexUVFloatArray(List<Vertex> vList)
	{
		FloatBuffer f = genFloatBuffer(vList.size()*2);
		
		for(Vertex v : vList)
		{
			f.put(v.getU());
			f.put(v.getV());
		}

		f.flip();
		
		return f;
	}
	
	
	public static IntBuffer createIntBuffer(List<Integer> ints)
	{
		IntBuffer in = BufferUtils.createIntBuffer(ints.size());
		
		for(int i : ints)
		{
			in.put(i);
		}
		
		in.flip();
		
		return in;
	}

}
