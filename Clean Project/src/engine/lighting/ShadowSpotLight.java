package engine.lighting;

import engine.math.Transform;
import engine.math.Vector3f;
import engine.renderer.Camera;

public class ShadowSpotLight extends SpotLight
{

	public ShadowSpotLight(Vector3f pos, Vector3f color, Vector3f direction, float angle, float intensity)
	{
		super(pos, color, direction, angle, intensity);
	}
	
	public void update()
	{
		Transform.lightOrthoMatrix = Transform.perspectiveMatrix();//Transform.orthographicSpace(-100,100,-20, 50, -75,75);
		Transform.lightViewMatrix = Transform.lightViewSpace(pos, direction);
		//Transform.lightViewMatrix = Transform.lightViewSpace(Camera.getPos(), Camera.getForward());
		
		super.update();
	}

}
