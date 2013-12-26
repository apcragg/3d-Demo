package engine.lighting;

import engine.components.Component;

public interface Light extends Comparable<Light>, Component
{
	public void update();
}
