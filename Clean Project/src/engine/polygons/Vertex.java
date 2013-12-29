package engine.polygons;

import engine.math.Vector2f;
import engine.math.Vector3f;

public class Vertex
{
	private float x, y, z, u, v;
	private Vector3f normal, tangent;

	public Vertex(float x, float y, float z)
	{
		this(x, y, z, 0f, 0f, 0f, 0f, 0f);
	}

	public Vertex(Vector3f v)
	{
		this(v.getX(), v.getY(), v.getZ());
	}

	public Vertex(Vector3f v, Vector2f uv)
	{
		this(v);
		this.u = uv.getX();
		this.v = uv.getY();
		this.normal = new Vector3f();
	}

	public Vertex(Vector3f v, Vector2f uv, Vector3f normal)
	{
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
		this.u = uv.getX();
		this.v = uv.getY();
		this.normal = normal;
		this.tangent = new Vector3f();
	}

	public Vertex(Vector3f r, float u, float v, Vector3f normal)
	{
		this(r, new Vector2f(u, v), normal);
	}

	public Vertex(Vector3f r, float u, float v, float x, float y, float z)
	{
		this(r, u, v, new Vector3f(x, y, z));
	}

	public Vertex(float x, float y, float z, float u, float v, float nX, float nY, float nZ)
	{
		this(new Vector3f(x, y, z), u, v, nX, nY, nZ);
	}

	public Vertex(Vector3f v, Vector3f n)
	{
		this(v, new Vector2f(0f, 0f), n);
	}

	public float getX()
	{
		return x;
	}

	public void setX(float x)
	{
		this.x = x;
	}

	public float getY()
	{
		return y;
	}

	public void setY(float y)
	{
		this.y = y;
	}

	public float getZ()
	{
		return z;
	}

	public void setZ(float z)
	{
		this.z = z;
	}

	public Vector3f getPosition()
	{
		return new Vector3f(x, y, z);
	}

	public float getU()
	{
		return -u;
	}

	public void setU(float u)
	{
		this.u = u;
	}

	public float getV()
	{
		return v;
	}

	public void setV(float v)
	{
		this.v = v;
	}

	public Vector3f getNormal()
	{
		return normal;
	}

	public void setNormal(Vector3f normal)
	{
		this.normal = normal;
	}

	public void setPos(Vector3f m)
	{
		this.x = m.getX();
		this.y = m.getY();
		this.z = m.getZ();

	}

	public Vector3f getTangent()
	{
		return tangent;
	}

	public void setTangent(Vector3f tangent)
	{
		this.tangent = tangent;
	}

}
