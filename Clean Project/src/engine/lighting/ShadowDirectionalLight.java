package engine.lighting;

import engine.math.Vector3f;

public class ShadowDirectionalLight extends DirectionalLight
{
	private static int updateCounter0 = 0;
	private ShadowMapFBO lightMap;
	
	public ShadowDirectionalLight(float intensity, Vector3f color, Vector3f direction)
	{
		super(intensity, color, direction);
	}
	
	@Override
	public void update()
	{
		
	}

	public void reset()
	{
		ShadowDirectionalLight.updateCounter0 = 0;
	}
	
	public ShadowMapFBO getLightMap()
	{
		return lightMap;
	}
}
