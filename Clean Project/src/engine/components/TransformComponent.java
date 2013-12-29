package engine.components;

import engine.main.Game;
import engine.math.Transform;
import engine.math.Vector3f;

public class TransformComponent implements Component
{
	private Vector3f translation, rotation, scale;

	public TransformComponent()
	{
		translation = new Vector3f();
		rotation = new Vector3f();
		scale = new Vector3f(1f, 1f, 1f);
	}

	public void update()
	{
		updateUniforms();
	}

	public void updateUniforms()
	{
		updateTransforms();

		Game.shader.uniformData4f("worldSpace", Transform.spatialMatrix());
	}

	public void updateTransforms()
	{
		Transform.setRotation(rotation);
		Transform.setScale(scale);
		Transform.setTranslation(translation);
	}

	public void render()
	{
		// on render do nothing
	}

	public void setup()
	{
		// does nothing yet
	}

}
