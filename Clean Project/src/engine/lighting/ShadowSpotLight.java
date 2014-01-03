package engine.lighting;

import engine.main.Game;
import engine.math.Transform;
import engine.math.Vector3f;
import engine.renderer.Camera;
import engine.shaders.PhongShader;

public class ShadowSpotLight extends SpotLight
{
	private static int updateCounter0 = 0;
	private ShadowMapFBO lightMap;
	private float fov;
	
	public ShadowSpotLight(Vector3f pos, Vector3f color, Vector3f direction, float angle, float intensity)
	{
		super(pos, color, direction, angle, intensity);
		
		lightMap = new ShadowMapFBO(3072, 3072);
		fov = 180 * (1 - angle);
	}
	
	public void lightSpaceUpdate()
	{
		System.out.println(fov);
		Transform.setupPerspective((int) fov, 1000f);
		Transform.lightOrthoMatrix = Transform.perspectiveMatrix();//Transform.orthographicSpace(-10,10,-10, 10, -10,20);
		Transform.lightViewMatrix = Transform.lightViewSpace(pos.add(pos.mul(.2f)), direction);
		//Transform.lightViewMatrix = Transform.lightViewSpace(Camera.getPos(), Camera.getForward());
		
	}
	
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
	
	public static void reset()
	{
		updateCounter0 = 0;
	}

	public ShadowMapFBO getLightMap()
	{
		return lightMap;
	}

}
