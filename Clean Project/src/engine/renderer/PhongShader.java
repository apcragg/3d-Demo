package engine.renderer;

import engine.util.ShaderLoader;
import static org.lwjgl.opengl.GL20.*;

public class PhongShader extends ShaderBase
{
	private static int programHandle;
	public final static int MAX_POINT_LIGHTS = 64;
	public final static int MAX_SPOT_LIGHTS = 8;
	
	public PhongShader()
	{
		super();
		
		setProgramHandle(programId);
		
		addVertexShader(ShaderLoader.loadShader("/res/shaders/VertexShader.vs"));
		addFragmentShader(ShaderLoader.loadShader("/res/shaders/FragmentShader.fs"));
		
		linkProgram();
		use();
				
		/*
		 * Transform space uniforms
		 */
			createUniform("projectedSpace");
			createUniform("worldSpace");
			createUniform("viewSpace");
	
		/*
		 * Lighting uniforms
		 */
			createUniform("ambient.base.intensity");
			createUniform("ambient.base.color");
			createUniform("mainLight.base.color");
			createUniform("mainLight.base.intensity");
			createUniform("mainLight.direction");
			createUniform("cameraPos");
			
			for(int i = 0; i < MAX_POINT_LIGHTS; i++)
			{				
				createUniform("pointLights[" + i + "].base.intensity");
				createUniform("pointLights[" + i + "].base.color");
				createUniform("pointLights[" + i + "].coefficient");
				createUniform("pointLights[" + i + "].pos");
			}
			
			for(int i = 0; i < MAX_SPOT_LIGHTS; i++)
			{				
				createUniform("spotLights[" + i + "].base.intensity");
				createUniform("spotLights[" + i + "].base.color");
				createUniform("spotLights[" + i + "].angle");
				createUniform("spotLights[" + i + "].direction");
				createUniform("spotLights[" + i + "].position");
			}
			
			createUniform("plNum");
			createUniform("slNum");
		
		/*
		 * Material Uniforms
		 */
			createUniform("specIntensity");
			createUniform("specExp");
			createUniform("specColor");
			createUniform("textureScale");
			createUniform("normalMap");
			
			createUniform("tex0");
			createUniform("tex1");
			createUniform("tex2");
			createUniform("tex3");
			
			uniformData1i("tex0", 0);
			uniformData1i("tex1", 1);
			uniformData1i("tex2", 2);
			uniformData1i("tex3", 3);
	}

	public static int getProgramHandle()
	{
		return programHandle;
	}

	public static void setProgramHandle(int programHandle)
	{
		PhongShader.programHandle = programHandle;
	}
	
	public static void useProgram()
	{
		glUseProgram(programHandle);
	}
}
