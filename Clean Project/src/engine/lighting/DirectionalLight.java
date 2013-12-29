package engine.lighting;

import engine.main.Game;
import engine.math.Vector3f;

public class DirectionalLight implements Light
{
	private Vector3f color, direction;
	private float intensity;

	public DirectionalLight(float intensity, Vector3f color, Vector3f direction)
	{
		this.intensity = intensity;
		this.color = color;
		this.direction = direction;
	}

	public void update()
	{
		Game.shader.uniformData3f("mainLight.base.color", color);
		Game.shader.uniformData1f("mainLight.base.intensity", intensity);
		Game.shader.uniformData3f("mainLight.direction", direction);
	}

	public void render()
	{

	}

	public void setup()
	{

	}

	public Vector3f getColor()
	{
		return color;
	}

	public void setColor(Vector3f color)
	{
		this.color = color;
	}

	public Vector3f getDirection()
	{
		return direction;
	}

	public void setDirection(Vector3f direction)
	{
		this.direction = direction;
	}

	public float getIntensity()
	{
		return intensity;
	}

	public void setIntensity(float intensity)
	{
		this.intensity = intensity;
	}

	public int compareTo(Light l)
	{
		if (l instanceof DirectionalLight) return 0;

		return -1;
	}

}
