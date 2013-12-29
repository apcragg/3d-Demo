package engine.lighting;

import org.lwjgl.input.Keyboard;

import engine.main.Game;
import engine.math.Vector3f;
import engine.renderer.Camera;
import engine.shaders.PhongShader;
import engine.util.InputHelper;

public class PlayerSpotLight extends SpotLight
{
	private boolean isOn;

	public PlayerSpotLight(Vector3f pos, Vector3f color, Vector3f direction, float angle, float intensity)
	{
		super(pos, color, direction, angle, intensity);

		isOn = true;
	}

	@Override
	public void update()
	{
		input();

		if (updateCounter < PhongShader.MAX_SPOT_LIGHTS && isOn)
		{
			pos = Camera.getPos(); // .add(Camera.getUp().mul(10f));
			direction = Camera.getForward();

			direction = direction.rotate(rotation, Camera.getForward().cross(Camera.getUp()));

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
		else
		{
			Game.shader.uniformData1f("spotLights[" + updateCounter
					+ "].base.intensity", 0f);

			updateCounter++;
		}

	}

	public void input()
	{
		if (InputHelper.isKeyDown(Keyboard.KEY_E))
		{
			rotation -= 1.0f;
		}
		if (InputHelper.isKeyDown(Keyboard.KEY_Q))
		{
			rotation += 1.0f;
		}

		if (InputHelper.isKeyPressed(Keyboard.KEY_C))
		{
			isOn = isOn ? false : true;
		}
	}

	public boolean isOn()
	{
		return isOn;
	}

	public void setOn(boolean isOn)
	{
		this.isOn = isOn;
	}
}
