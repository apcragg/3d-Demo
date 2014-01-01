package engine.lighting;

import engine.main.Game;
import engine.math.Vector3f;
import engine.util.LogHelper;

public class AmbientLight implements Light
{
	private Vector3f baseColor;
	private float intensity;

	public AmbientLight(float intensity, Vector3f color)
	{
		this.intensity = intensity;
		this.baseColor = color;
	}

	public void update()
	{
		Game.getShader().uniformData3f("ambient.base.color", baseColor);
		Game.getShader().uniformData1f("ambient.base.intensity", intensity);
	}

	public void render()
	{

	}

	public void setup()
	{

	}

	public int compareTo(Light l)
	{
		if (l instanceof AmbientLight) return 0;

		return -1;
	}

	public Vector3f getBaseColor()
	{
		return baseColor;
	}

	public void setBaseColor(Vector3f baseColor)
	{
		this.baseColor = baseColor;
	}

	public float getIntensity()
	{
		return intensity;
	}

	public void setIntensity(float intensity)
	{
		this.intensity = intensity;
	}

}
