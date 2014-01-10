package engine.shaders;

import engine.util.ShaderLoader;

public class SuperSampleShader extends ShaderBase
{
	private int programHandle;
	
	public SuperSampleShader()
	{
		setProgramHandle(programId);
		
		addVertexShader(ShaderLoader.loadShader("/res/shaders/screenQuad.vs"));
		addFragmentShader(ShaderLoader.loadShader("/res/shaders/screenQuadSSAA.fs"));
		
		linkProgram();
		use();
		
		setup();
	}
	
	protected void setup()
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

