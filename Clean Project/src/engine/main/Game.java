package engine.main;

import engine.levels.Level;
import engine.shaders.PhongShader;
import engine.shaders.QuadShader;
import engine.shaders.ShaderBase;
import engine.shaders.ShadowShader;
import engine.util.InputHelper;
import engine.util.LogHelper;
import game.levels.MainLevel;
import game.levels.ShadowLevel;

public class Game
{
	public static int PHONG = 0;
	public static int SHADOW = 1;
	public static int QUAD = 2;
	
	public static int currentShader;
	private static ShaderBase[] shaders = new ShaderBase[5];
	private Level[] levels = new Level[5];
	private static int currentLevel;

	public Game()
	{
		setup();
	}

	public void setup()
	{
		new InputHelper();
		shaders[0] = new PhongShader();
		shaders[1] = new ShadowShader();
		shaders[2] = new QuadShader();
		levels[0] = new MainLevel();
		levels[1] = new ShadowLevel();
		currentLevel = 1;
		currentShader = 0;
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

	public static int getCurrentLevel()
	{
		return currentLevel;
	}

	public static void setCurrentLevel(int currentLevel)
	{
		Game.currentLevel = currentLevel;
	}
	
	public static void setShader(int shader)
	{
		currentShader = shader;
		shaders[currentShader].use();
	}
	
	public static ShaderBase getShader()
	{
		return shaders[currentShader];
	}

}
