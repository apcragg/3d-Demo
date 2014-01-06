package engine.renderer;

import engine.main.Game;
import engine.main.Window;

import org.lwjgl.input.*;

import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.util.InputHelper;
import engine.util.LogHelper;

public class Camera
{
	private final static float moveAmount = 1.4f;
	public static final Vector3f yAxis = new Vector3f(0f, 1f, 0f);
	private static Vector3f forward;
	private static Vector3f up;
	private static Vector3f pos;
	private static float sensitivity;
	private static float smoothing;
	private static float smoothBias;
	private static Vector3f rotation;

	public Camera()
	{
		pos = new Vector3f();
		forward = new Vector3f(0f, 0f, -1f);
		sensitivity = 1.5f;
		smoothing = 1.3f;
		smoothBias = 4f;
		up = yAxis;
		rotation = new Vector3f();
	}

	public static void rotateX(float amount)
	{
		Vector3f axis = yAxis.cross(forward).normalized();

		forward = forward.rotate(amount, axis).normalized();

		up = forward.cross(axis).normalized();
	}

	public static void rotateY(float amount)
	{
		Vector3f axis = yAxis.cross(forward).normalized();

		forward = forward.rotate(amount, yAxis).normalized();

		up = forward.cross(axis).normalized();
	}

	public static void update()
	{
		Game.getShader().uniformData3f("cameraPos", pos);

		updateMouse();
	}

	public static void resetView()
	{
		forward.setX(0f);
		forward.setY(0f);
		forward.setZ(1f);

		up = yAxis;
	}

	private static void updateMouse()
	{		
		float dx = Mouse.getDX() / smoothBias;
		float dy = -Mouse.getDY() / smoothBias;
		
		dx = (dx < 0 ? -1 : 1) * ((float) Math.pow(Math.abs(dx),  smoothing) < 25f ? (float) Math.pow(Math.abs(dx),  smoothing) : 25f);
		dy = (dy < 0 ? -1 : 1) * ((float) Math.pow(Math.abs(dy),  smoothing) < 25f ? (float) Math.pow(Math.abs(dy),  smoothing) : 25f);

		rotation = rotation.add(new Vector3f(0f, dx * sensitivity, 0f));
		rotation = rotation.add(new Vector3f(dy * sensitivity, 0f, 0f));

		// y-look bounds

		if (rotation.getX() < -89f) rotation.setX(-89);

		if (rotation.getX() > 89f) rotation.setX(89);

		// x look 360 degree reset

		if (rotation.getY() > 360f) rotation.setY(rotation.getY() - 360f);

		if (rotation.getY() < -360f) rotation.setY(rotation.getY() + 360f);

		// Y has to come first to ensure the axis isn't messed up beforehand.

		resetView();

		rotateY(rotation.getY());
		rotateX(rotation.getX());

		Mouse.setGrabbed(true);
		Mouse.setCursorPosition(Window.WIDTH / 2, Window.HEIGHT / 2);

		if (InputHelper.isKeyDown(Keyboard.KEY_W)) move(forward.normalized(), moveAmount
				* sensitivity);
		if (InputHelper.isKeyDown(Keyboard.KEY_S)) move(forward.normalized(), -moveAmount
				* sensitivity);

		if (InputHelper.isKeyDown(Keyboard.KEY_A)) move(forward.cross(yAxis).normalized(), moveAmount
				* sensitivity);
		if (InputHelper.isKeyDown(Keyboard.KEY_D)) move(yAxis.cross(forward).normalized(), moveAmount
				* sensitivity);

		if (InputHelper.isKeyDown(Keyboard.KEY_LSHIFT)) move(yAxis, moveAmount
				* sensitivity);
		if (InputHelper.isKeyDown(Keyboard.KEY_LCONTROL)) move(yAxis, -moveAmount
				* sensitivity);
	}

	private static void move(Vector3f axis, float amount)
	{
		if (amount > 0) pos = pos.add(axis.mul(amount));

		if (amount < 0) pos = pos.subtract(axis.mul(-amount));
	}

	public static Vector3f getPos()
	{
		return pos;
	}

	public static void setPos(Vector3f pos)
	{
		Camera.pos = pos;
	}

	public static Vector3f getForward()
	{
		return forward;
	}

	public static Vector3f getUp()
	{
		return up;
	}
	
	public static void setForward(Vector3f forward)
	{
		Camera.forward = forward;
	}

	public static void setUp(Vector3f up)
	{
		Camera.up = up;
	}
}
