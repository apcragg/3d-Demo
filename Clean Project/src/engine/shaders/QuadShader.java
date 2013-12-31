package engine.shaders;

import engine.util.ShaderLoader;

public class QuadShader extends ShaderBase
{
	private int programHandle;
	
	public QuadShader()
	{
		setProgramHandle(programId);
		
		addVertexShader(ShaderLoader.loadShader("/res/shaders/quad.vs"));
		addFragmentShader(ShaderLoader.loadShader("/res/shaders/quad.fs"));
		
		linkProgram();
		use();
		
		setup();
	}
	
	private void setup()
	{
		createUniform("viewSpace");
		createUniform("worldSpace");
		createUniform("projectedSpace");
		
		createUniform("tex");		
		uniformData1i("tex", 0);
	}

	public int getProgramHandle()
	{
		return programHandle;
	}

	public void setProgramHandle(int programHandle)
	{
		this.programHandle = programHandle;
	}
}
