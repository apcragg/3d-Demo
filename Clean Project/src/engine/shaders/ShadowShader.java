package engine.shaders;

import engine.util.ShaderLoader;

public class ShadowShader extends ShaderBase
{
	private static int programHandle;
	
	public ShadowShader()
	{
		super();		
		setup();
	}
	
	private void setup()
	{
		setProgramHandle(programId);
		
		addVertexShader(ShaderLoader.loadShader("/res/shaders/ShadowPassVertexShader.vs"));
		addFragmentShader(ShaderLoader.loadShader("/res/shaders/ShadowPassFragmentShader.fs"));
		
		linkProgram();
		use();
		
		//transforms
		createUniform("lightSpace");
		createUniform("worldSpace");

	}

	public static int getProgramHandle()
	{
		return programHandle;
	}

	public static void setProgramHandle(int programHandle)
	{
		ShadowShader.programHandle = programHandle;
	}
}
