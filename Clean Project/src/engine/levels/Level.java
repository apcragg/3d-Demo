package engine.levels;

import engine.materials.Material;

public abstract class Level
{
	public static Material defaultMaterial = new Material("default");

	public abstract void render();

	public abstract void update();

}
