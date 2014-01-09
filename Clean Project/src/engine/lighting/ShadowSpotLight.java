package engine.lighting;

import engine.main.Game;
import engine.math.Matrix4f;
import engine.math.Transform;
import engine.math.Vector3f;
import engine.renderer.Camera;
import engine.shaders.PhongShader;

public class ShadowSpotLight extends SpotLight
{
	private static int updateCounter0 = 0;
	private ShadowMapFBO lightMap;
	private float fov;
	
	public static int size = 2048;
	
	public ShadowSpotLight(Vector3f pos, Vector3f color, Vector3f direction, float angle, float intensity)
	{
		super(pos, color, direction, angle, intensity);
		
		lightMap = new ShadowMapFBO(size, size);
		fov = 180 * (1 - angle);
	}
	
	/**
	 * Sets up the orthographic matrix and lightViewSpace matrix for use in the Transform class
	 */
	
	public void lightSpaceUpdate()
	{
		//Transform.setupPerspective((int) fov, 1000f);
		//Transform.lightOrthoMatrix = Transform.perspectiveMatrix();
		Transform.lightOrthoMatrix = Transform.orthographicSpace(-175, 175, -100, 100, -150, 150);
		Transform.lightViewMatrix = Transform.lightViewSpace(pos, direction);
	}
	
	/**
	 * Updates the uniform values for each shadow casting light
	 */
	
	public void update()
	{		
		if (updateCounter0 < PhongShader.MAX_SHADOW_SPOT_LIGHTS)
		{
			Game.getShader().uniformData1f("shadowSpotLights[" + updateCounter0 + "].angle", angle);
			Game.getShader().uniformData1f("shadowSpotLights[" + updateCounter0 + "].base.intensity", intensity);
			Game.getShader().uniformData3f("shadowSpotLights[" + updateCounter0 + "].base.color", color);
			Game.getShader().uniformData3f("shadowSpotLights[" + updateCounter0 + "].position", pos);
			Game.getShader().uniformData3f("shadowSpotLights[" + updateCounter0 + "].direction", direction);

			updateCounter0++;
		}
	}
	
	/**
	 * Resets the update counter. Should be called after all the ShadowSpotLights have been updated each frame.
	 */
	
	public static void reset()
	{
		updateCounter0 = 0;
	}

	public ShadowMapFBO getLightMap()
	{
		return lightMap;
	}

}
