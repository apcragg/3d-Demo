package engine.shaders;

import engine.util.ShaderLoader;

public class GaussianShaderH extends ShaderBase
{
private int programHandle;
	
	public GaussianShaderH()
	{
		setProgramHandle(programId);
		
		addVertexShader(ShaderLoader.loadShader("/res/shaders/GaussianShader.vs"));
		addFragmentShader(ShaderLoader.loadShader("/res/shaders/GaussianShader_H.fs"));
		
		linkProgram();
		use();
		
		setup();
	}
	
	private void setup()
	{
		createUniform("tex");		
		createUniform("scale0");	
		createUniform("scale1");	
		
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
