package engine.lighting;

import org.lwjgl.input.Keyboard;

import engine.main.Game;
import engine.math.Vector3f;
import engine.renderer.Camera;
import engine.renderer.PhongShader;
import engine.util.InputHelper;

public class PlayerSpotLight extends SpotLight
{
	public PlayerSpotLight(Vector3f pos, Vector3f color, Vector3f direction, float angle, float intensity) 
	{
		super(pos, color, direction, angle, intensity);
	}

	@Override
	public void update()
	{
		if(updateCounter < PhongShader.MAX_SPOT_LIGHTS)
		{					
			pos = Camera.getPos(); //.add(Camera.getUp().mul(10f));
			direction = Camera.getForward();
			
			if(InputHelper.isKeyDown(Keyboard.KEY_E))
			{
				rotation -= .25f;			
			}
			
			if(InputHelper.isKeyDown(Keyboard.KEY_Q))
			{
				rotation += .25f;			
			}
			
			direction = direction.rotate(rotation, Camera.getForward().cross(Camera.getUp()));
			
			Game.shader.uniformData1f("spotLights[" + updateCounter + "].angle", angle);
			Game.shader.uniformData1f("spotLights[" + updateCounter + "].base.intensity", intensity);
			Game.shader.uniformData3f("spotLights[" + updateCounter + "].base.color", color);
			Game.shader.uniformData3f("spotLights[" + updateCounter + "].position", pos);
			Game.shader.uniformData3f("spotLights[" + updateCounter + "].direction", direction);
			
			updateCounter++;
		}
	}
}
