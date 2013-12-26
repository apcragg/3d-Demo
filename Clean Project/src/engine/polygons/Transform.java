package engine.polygons;

import engine.math.Matrix4f;
import engine.math.Vector3f;
import engine.renderer.Camera;

public class Transform
{
	private static Vector3f translation, rotation, scale;
	private static float aspectRatio, depth;
	private static int fov;
	
	public static void setupPerspective(float ar, int fov, float depth)
	{
		Transform.aspectRatio = ar;
		Transform.fov = fov;
		Transform.depth = depth;
	}
	
	public static void setTranslation(Vector3f v)
	{
		Transform.translation = v;
	}
	
	public static void setRotation(Vector3f v)
	{
		Transform.rotation = v;
	}
	
	public static void setScale(Vector3f v)
	{
		Transform.scale = v;
	}
	
	public static void setScale(float f)
	{
		Transform.scale = new Vector3f(f, f, f);
	}
	
	/**
	 * Calculates the projection space Matrix.
	 * @return The projection space Matrix4f.
	 */
	
	public static Matrix4f perspectiveMatrix()
	{
		Matrix4f perspective = new Matrix4f().identity().perspective(fov, .01f, depth);
		
		return perspective;
	}
	
	/**
	 * Calculates the world space matrix for the object.
	 * @return The world space Matrix4f
	 **/
	
	public static Matrix4f spatialMatrix()
	{		
		Matrix4f mScale 	= new Matrix4f().scale(scale);
		Matrix4f mTranslate	= new Matrix4f().translate(translation);
		Matrix4f mRotate	= new Matrix4f().rotate(rotation);
		
		return mTranslate.mul(mRotate.mul(mScale));	
	}
	
	/**
	 * Calculates the object space matrix for the object.
	 * @return The object space Matrix4f
	 */
	
	public static Matrix4f normalMatrix()
	{
		return new Matrix4f().rotate(rotation).mul(new Matrix4f().scale(scale));
	}
	
	public static Matrix4f viewSpace()
	{
		Matrix4f rotation = new Matrix4f().initCamera(Camera.getForward(), Camera.getUp());
		Matrix4f transform = new Matrix4f().translate(Camera.getPos().negated());
		
		return rotation.mul(transform);
	}
	

}
