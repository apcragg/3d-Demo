package engine.math;

import engine.renderer.Camera;

public class Transform
{
	private static Vector3f translation, rotation, scale;
	private static float depth;
	private static int fov;
	private static int zoomFov = 20;
	private static boolean zoom = false;
	
	public static Vector3f currentLight = new Vector3f();
	public static Matrix4f lightOrthoMatrix = new Matrix4f().identity();
	public static Matrix4f lightViewMatrix = new Matrix4f().identity();

	public static void setupPerspective(int fov, float depth)
	{
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
	 * 
	 * @return The projection space Matrix4f.
	 */

	public static Matrix4f perspectiveMatrix()
	{
		Matrix4f perspective = new Matrix4f().identity().perspective(zoom ? zoomFov : fov, .01f, depth);

		return perspective;
	}

	/**
	 * Calculates the world space matrix for the object.
	 * 
	 * @return The world space Matrix4f
	 **/

	public static Matrix4f spatialMatrix()
	{
		Matrix4f mScale = new Matrix4f().scale(scale);
		Matrix4f mTranslate = new Matrix4f().translate(translation);
		Matrix4f mRotate = new Matrix4f().rotate(rotation);

		return mTranslate.mul(mScale.mul(mRotate));
	}

	/**
	 * Calculates the object space matrix for the object.
	 * 
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
	
	public static Matrix4f orthographicSpace(float left, float right, float bottom,  float top, float near, float far)
	{
		return new Matrix4f().initOrthographicProjection(left, right, bottom,  top, near, far);
	}
	
	public static Matrix4f lightViewSpace(Vector3f pos, Vector3f direction)
	{
		direction = direction.negated().normalized();
		
		Vector3f left = Camera.yAxis.cross(direction).normalized();
		Vector3f up = direction.cross(left).normalized();
		
		Matrix4f rotation = new Matrix4f().initCamera(direction.normalized(), up);
		Matrix4f transform = new Matrix4f().translate(pos.negated());

		return rotation;//.mul(transform); //no translation for ortho
	}
	
	public static Matrix4f lightSpace()
	{	
		return lightOrthoMatrix.mul(lightViewMatrix);
	}

	public static int getZoomFov()
	{
		return zoomFov;
	}

	public static void setZoomFov(int zoomFov)
	{
		Transform.zoomFov = zoomFov;
	}

	public static boolean isZoom()
	{
		return zoom;
	}

	public static void setZoom(boolean zoom)
	{
		Transform.zoom = zoom;
	}
	
	public static float getMouseSmoothFactor()
	{
		return zoom ? (fov / zoomFov) : 1.0f;
	}

}
