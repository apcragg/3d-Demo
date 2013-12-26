package engine.main;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import engine.renderer.RenderHelper;

public class Window
{
	public static int WIDTH, HEIGHT;
	public static float ASPECT_RATIO;
	public static String title;
	
	public Window(int width, float aspectRatio)
	{
		Window.ASPECT_RATIO = aspectRatio;
		Window.WIDTH = width;
		Window.HEIGHT =(int) (Window.ASPECT_RATIO * Window.WIDTH );
		Window.title = "Clean Project";
		
		createDisplay();
		new RenderHelper();
		
	}
	
	private void createDisplay()
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(HEIGHT, WIDTH));
			Display.setTitle(Window.title);
			Display.create();
			//Display.setVSyncEnabled(true);
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
