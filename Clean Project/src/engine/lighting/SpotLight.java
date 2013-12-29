package engine.lighting;

import org.lwjgl.input.Keyboard;

import engine.main.Game;
import engine.math.Vector3f;
import engine.renderer.Camera;
import engine.shaders.PhongShader;
import engine.util.InputHelper;

public class SpotLight implements Light
{
	protected float angle, intensity;
	protected Vector3f color, pos, direction;

	protected float rotation;

	protected static int updateCounter;

	/**
	 * Creates a new spot light.
	 * 
	 * @param spot
	 *            light position
	 * @param color
	 *            The light's color
	 * @param angle
	 *            The angle the light spreads out at
	 * @param intensity
	 *            The light's intensity
	 */

	public SpotLight(Vector3f pos, Vector3f color, Vector3f direction, float angle, float intensity)
	{
		this.angle = angle;
		this.intensity = intensity;
		this.color = color;
		this.pos = pos;
		this.direction = direction;

		this.rotation = 0f;
	}

	public void update()
	{
		if (updateCounter < PhongShader.MAX_SPOT_LIGHTS)
		{
			Game.shader.uniformData1f("spotLights[" + updateCounter + "].angle", angle);
			Game.shader.uniformData1f("spotLights[" + updateCounter
					+ "].base.intensity", intensity);
			Game.shader.uniformData3f("spotLights[" + updateCounter
					+ "].base.color", color);
			Game.shader.uniformData3f("spotLights[" + updateCounter
					+ "].position", pos);
			Game.shader.uniformData3f("spotLights[" + updateCounter
					+ "].direction", direction);

			updateCounter++;
		}
	}

	public void render()
	{

	}

	public void setup()
	{

	}

	public int compareTo(Light l)
	{
		SpotLight l0 = (SpotLight) l;

		float distance0 = (pos.subtract(Camera.getPos())).length();
		float distance1 = (l0.getPos().subtract(Camera.getPos())).length();

		return distance0 > distance1 ? 1 : -1;
	}

	public float getAngle()
	{
		return angle;
	}

	public void setAngle(float angle)
	{
		this.angle = angle;
	}

	public float getIntensity()
	{
		return intensity;
	}

	public void setIntensity(float intensity)
	{
		this.intensity = intensity;
	}

	public Vector3f getColor()
	{
		return color;
	}

	public void setColor(Vector3f color)
	{
		this.color = color;
	}

	public static int getUpdateCount()
	{
		return updateCounter;
	}

	public static void setUpdateCount(int updateCount)
	{
		SpotLight.updateCounter = updateCount;
	}

	public Vector3f getPos()
	{
		return pos;
	}

	public void setPos(Vector3f pos)
	{
		this.pos = pos;
	}

	public static void reset()
	{
		updateCounter = 0;
	}

}
