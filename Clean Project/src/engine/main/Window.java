package engine.main;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import engine.renderer.RenderHelper;

public class Window
{
	public static int WIDTH, HEIGHT;
	public static float ASPECT_RATIO;
	public static String title;

	public Window(int height, float aspectRatio)
	{
		Window.ASPECT_RATIO = aspectRatio;	
		Window.HEIGHT = height;
		Window.WIDTH = (int) (Window.ASPECT_RATIO * Window.HEIGHT);
		Window.title = "Clean Project";

		createDisplay();
		new RenderHelper();

	}

	private void createDisplay()
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle(Window.title);
			Display.create();
			Display.setFullscreen(true);
			// Display.setVSyncEnabled(true);
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
	}

	public void destroy()
	{
		Display.destroy();
	}

	public static void updateFPS(int fps)
	{
		Display.setTitle(Window.title + " FPS: " + fps);
	}

}
