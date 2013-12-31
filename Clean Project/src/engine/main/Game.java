package engine.main;

import engine.levels.Level;
import engine.shaders.PhongShader;
import engine.shaders.QuadShader;
import engine.shaders.ShadowShader;
import engine.util.InputHelper;
import engine.util.LogHelper;
import game.levels.MainLevel;
import game.levels.ShadowLevel;

public class Game
{
	public static PhongShader shader;
	public static ShadowShader shadowShader;
	public static QuadShader quadShader;
	private Level[] levels = new Level[5];
	private static int currentLevel;

	public Game()
	{
		setup();
	}

	public void setup()
	{
		new InputHelper();
		shader = new PhongShader();
		shadowShader = new ShadowShader();
		quadShader = new QuadShader();
		levels[0] = new MainLevel();
		levels[1] = new ShadowLevel();
		currentLevel = 1;
	}

	public void update()
	{
		levels[currentLevel].update();
	}

	public void render()
	{
		levels[currentLevel].render();
	}

	public void quit()
	{
		// TODO: Quit behavior.
		LogHelper.printInfo("Quiting...");

		LogHelper.dumpInfoLog(false);
	}

	public static PhongShader getShader()
	{
		return shader;
	}

	public static void setShader(PhongShader shader)
	{
		Game.shader = shader;
	}

	public static int getCurrentLevel()
	{
		return currentLevel;
	}

	public static void setCurrentLevel(int currentLevel)
	{
		Game.currentLevel = currentLevel;
	}

}
