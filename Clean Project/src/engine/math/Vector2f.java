package engine.math;

public class Vector2f
{
	private float x, y;

	public Vector2f(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public Vector2f(Vector2f v)
	{
		this.x = v.getX();
		this.y = v.getY();
	}

	public Vector2f()
	{
		this.x = 0.0f;
		this.y = 0.0f;
	}

	public void normalize()
	{
		float magnitude = (float) Math.sqrt(x * x + y * y);

		x /= magnitude;
		y /= magnitude;
	}

	public Vector2f normalized()
	{
		float magnitude = (float) Math.sqrt(x * x + y * y);

		x /= magnitude;
		y /= magnitude;

		return this;
	}

	public float length()
	{
		return (float) Math.sqrt(x * x + y * y);
	}

	public Vector2f add(Vector2f v)
	{
		return new Vector2f(x + v.getX(), y + v.getY());
	}

	public Vector2f subtract(Vector2f v)
	{
		return new Vector2f(x - v.getX(), y - v.getY());
	}

	public Vector2f mul(float f)
	{
		return new Vector2f(x * f, y * f);
	}

	public Vector2f div(float f)
	{
		return new Vector2f(x / f, y / f);
	}

	public float dot(Vector2f v)
	{
		return x * v.getX() + y * v.getY();
	}

	public void negate()
	{
		x = -x;
		y = -y;
	}

	public Vector2f negated()
	{
		negate();

		return this;
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

	public void set(Float f, int index)
	{
		switch (index)
		{
			case 0:
				x = f;
				break;

			case 1:
				y = f;
				break;

			default:
				System.out.println("Not a valid vector index. @engine.math.Vector2f");
				break;
		}

	}
	
	public String toString()
	{
		return "X: " + x + " Y: " + y;
	}

}
