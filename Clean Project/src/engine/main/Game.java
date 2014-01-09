package engine.main;

import engine.levels.Level;
import engine.renderer.GaussinBlur;
import engine.shaders.GaussinShaderH;
import engine.shaders.GaussinShaderV;
import engine.shaders.PhongShader;
import engine.shaders.QuadShader;
import engine.shaders.ScreenQuadShader;
import engine.shaders.ShaderBase;
import engine.shaders.ShadowShader;
import engine.util.InputHelper;
import engine.util.LogHelper;
import game.levels.MainLevel;
import game.levels.ShadowLevel;

public class Game
{
	public static int PHONG  	  = 0;
	public static int SHADOW 	  = 1;
	public static int QUAD 		  = 2;
	public static int SCREEN_QUAD = 3;
	public static int GAUSSIN_V   = 4;
	public static int GAUSSIN_H   = 5;
	

	private static ShaderBase[] shaders;
	private static Level[] levels;
	private static int currentLevel;
	public static int currentShader;

	public Game()
	{
		setup();
	}

	public void setup()
	{
		new InputHelper();
		new GaussinBlur();
		shaders = new ShaderBase[]{ new PhongShader(), new ShadowShader(), new QuadShader(), new ScreenQuadShader(), new GaussinShaderV(), new GaussinShaderH()};
		levels 	= new Level[]{ new MainLevel(), new ShadowLevel()};
		currentLevel 	= 1;
		currentShader 	= 0;
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
