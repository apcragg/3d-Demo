package engine.math;

public class Vector3f
{
	private float x, y, z;
	
	public Vector3f(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(Vector3f v)
	{
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
	}
	
	public Vector3f()
	{
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.0f;
	}
	
	public void normalize()
	{
		float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
		
		x /= magnitude;
		y /= magnitude;
		z /= magnitude;
	}
	
	public Vector3f normalized()
	{
		float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
		
		x /= magnitude;
		y /= magnitude;
		z /= magnitude;
		
		return this;
	}
	
	public float length()
	{
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public Vector3f add(Vector3f v)
	{
		return new Vector3f(x + v.getX(), y + v.getY(), z + v.getZ());
	}
	
	public Vector3f subtract(Vector3f v)
	{
		return new Vector3f(x - v.getX(), y - v.getY(), z - v.getZ());
	}
	
	public Vector3f mul(float f)
	{
		return new Vector3f(x * f, y * f, z * f);
	}

	public Vector3f div(float f)
	{
		return new Vector3f(x / f, y / f, z / f);
	}
	
	public float dot(Vector3f v)
	{
		return x * v.getX() + y * v.getY() + z * v.getZ();
	}
	
	public Vector3f cross(Vector3f v)
	{
		return new Vector3f(y*v.getZ() - z * v.getY(), z * v.getX() - x * v.getZ(), x * v.getY() - y * v.getX() );
	}
	
	public Vector3f rotate(float amount, Vector3f axis)
	{
		return new Matrix4f().arbitraryAxisRotate(axis, amount).mul(this);
	}
	
	public void negate()
	{
		x = -x;
		y = -y;
		z = -z;
	}
	
	public Vector3f negated()
	{		
		return new Vector3f(-x, -y, -z);
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
	
	public void set(float f, int index)
	{
		switch(index)
		{
			case 0:
				x = f;
			break;
			
			case 1:
				y = f;
			break;
			
			case 2:
				z = f;
			break;
			
			default:
				System.out.println("Not a valid vector index. @engine.math.Vector3f");
			break;
		}
	}
	
	public String toString()
	{
		return new String("Vector3f: " + x + " " + y + " " + z);
	}

}
