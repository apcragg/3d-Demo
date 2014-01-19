package engine.shaders;

import engine.util.ShaderLoader;

public class ShadowBlurH extends ShaderBase
{
private int programHandle;
	
	public ShadowBlurH()
	{
		setProgramHandle(programId);
		
		addVertexShader(ShaderLoader.loadShader("/res/shaders/GaussianShader.vs"));
		addFragmentShader(ShaderLoader.loadShader("/res/shaders/ShadowBlurH.fs"));
		
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
