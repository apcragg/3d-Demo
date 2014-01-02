package engine.shaders;

import engine.main.Game;
import engine.util.ShaderLoader;
import static org.lwjgl.opengl.GL20.*;

public class PhongShader extends ShaderBase
{
	private static int programHandle;
	public final static int MAX_POINT_LIGHTS = 64;
	public final static int MAX_SPOT_LIGHTS = 8;
	public static final int MAX_SHADOW_SPOT_LIGHTS = 4;

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
		
		for (int i = 0; i < MAX_SHADOW_SPOT_LIGHTS; i++)
		{
			createUniform("lightSpace[" + i + "]");
		}		

		/*
		 * Lighting uniforms
		 */
		createUniform("ambient.base.intensity");
		createUniform("ambient.base.color");
		createUniform("mainLight.base.color");
		createUniform("mainLight.base.intensity");
		createUniform("mainLight.direction");
		createUniform("cameraPos");

		for (int i = 0; i < MAX_POINT_LIGHTS; i++)
		{
			createUniform("pointLights[" + i + "].base.intensity");
			createUniform("pointLights[" + i + "].base.color");
			createUniform("pointLights[" + i + "].coefficient");
			createUniform("pointLights[" + i + "].pos");
		}

		for (int i = 0; i < MAX_SPOT_LIGHTS; i++)
		{
			createUniform("spotLights[" + i + "].base.intensity");
			createUniform("spotLights[" + i + "].base.color");
			createUniform("spotLights[" + i + "].angle");
			createUniform("spotLights[" + i + "].direction");
			createUniform("spotLights[" + i + "].position");
		}
		
		for (int i = 0; i < MAX_SHADOW_SPOT_LIGHTS; i++)
		{
			createUniform("shadowSpotLights[" + i + "].base.intensity");
			createUniform("shadowSpotLights[" + i + "].base.color");
			createUniform("shadowSpotLights[" + i + "].angle");
			createUniform("shadowSpotLights[" + i + "].direction");
			createUniform("shadowSpotLights[" + i + "].position");
		}

		createUniform("plNum");
		createUniform("slNum");
		createUniform("s2Num");

		/*
		 * Material Uniforms
		 */
		createUniform("specIntensity");
		createUniform("specExp");
		createUniform("specColor");
		createUniform("textureScale");

		createUniform("normalMap");
		createUniform("displacementMapping");
		createUniform("parallaxMapping");
		createUniform("displacementFactor");

		createUniform("diffuseTex");
		createUniform("normalTex");
		createUniform("displacementTex");
		createUniform("parallaxTex");
		
		for (int i = 0; i < MAX_SHADOW_SPOT_LIGHTS; i++)
		{
			createUniform("shadowTex[" + i + "]");
		}

		uniformData1i("diffuseTex", 0);
		uniformData1i("normalTex", 1);
		uniformData1i("displacementTex", 2);
		uniformData1i("parallaxTex", 2);

		for (int i = 0; i < MAX_SHADOW_SPOT_LIGHTS; i++)
		{
			uniformData1i("shadowTex[" + i + "]", 3 + i);			
		}
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
