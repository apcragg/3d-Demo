package engine.main;

import org.lwjgl.opengl.*;

import engine.renderer.RenderHelper;

public class Main
{
	private static Window window;
	private static Game game;

	public static final float timeStep = 1f / 60f;

	private static boolean loop;

	public static void main(String[] args)
	{
		new Main();
	}

	public Main()
	{
		window = new Window(720, 16 / 9f);
		game = new Game();
		startLoop();

	}

	public void startLoop()
	{
		loop = true;
		long startTime = Time.getTime();
		long frameTime = 0;
		long currentTime = Time.getTime();
		int frameCount = 0;

		while (!Display.isCloseRequested() && loop)
		{
			long elapsedTime = Time.getTime() - startTime;

			if ((float) elapsedTime / Time.SECOND > timeStep)
			{
				startTime = Time.getTime();

				game.update();
			}

			game.render();
			Display.update();
			Display.sync(120);

			frameTime += Time.getTime() - currentTime;
			frameCount++;

			currentTime = Time.getTime();

			if (frameTime > Time.SECOND / 4)
			{
				Window.updateFPS(frameCount * 4);

				frameCount = 0;
				frameTime = 0;
			}

			RenderHelper.clear();
		}

		quit();
	}

	public static void stopLoop()
	{
		loop = false;
	}

	public static void quit()
	{
		stopLoop();
		game.quit();
		window.destroy();
		System.exit(0);
	}

}
