package engine.lighting;

import engine.components.Component;
import engine.math.Vector3f;

public interface Light extends Comparable<Light>, Component
{
	public static final Vector3f WHITE_LIGHT = new Vector3f(1f, 1f, 1f);
	
	public void update();
}
