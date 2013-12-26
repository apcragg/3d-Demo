package engine.shaders;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;

import engine.main.Main;
import engine.math.Matrix4f;
import engine.math.Vector3f;
import engine.util.LogHelper;
import engine.util.ProfilerHelper;

public class ShaderBase
{
	protected int programId;
	protected HashMap<String, Integer> uniformLocations = new HashMap<String, Integer>();
	
	public ShaderBase()
	{
		programId = glCreateProgram();
		
		if(!(programId > 0))
		{
			LogHelper.printError("Could not create shader program.");
			Main.quit();
		}
	}
	
	public void addShader(String source, int type)
	{
		int shaderId = glCreateShader(type);
		
		if(!(programId > 0))
		{
			LogHelper.printError("Could not create " + type + " shader.\n");
			Main.quit();
		}
		
		glShaderSource(shaderId, source);
		glCompileShader(shaderId);
		
		glAttachShader(programId, shaderId);
	}
	
	public void addVertexShader(String source)
	{
		addShader(source, GL_VERTEX_SHADER);
	}
	
	public void addFragmentShader(String source)
	{
		addShader(source, GL_FRAGMENT_SHADER);
	}

	public void linkProgram()
	{
		glLinkProgram(programId);
		
		int linkStatus = glGetProgrami(programId, GL_LINK_STATUS);
		
		if(linkStatus == 0)
		{
			LogHelper.printError("Failed to link program.");
			LogHelper.printError(glGetProgramInfoLog(programId, 2048));;
			Main.quit();
		}
		
		glValidateProgram(programId);
		
		int validationStatus = glGetProgrami(programId, GL_VALIDATE_STATUS);
		
		if(validationStatus == 0)
		{
			LogHelper.printError("Failed to validate program.");
			LogHelper.printError(glGetProgramInfoLog(programId, 2048));;
			Main.quit();
		}
	}
	
	public void use()
	{
		glUseProgram(programId);
	}
	
	public void createUniform(String name)
	{
		int location = glGetUniformLocation(programId, name);
		
		if(location == -1)
		{
			LogHelper.printError("Could not create uniform " + name);
		}
		
		uniformLocations.put(name, location);
	}
	
	public void uniformData1i(String name, int i)
	{
		checkExistence(name);
		
		glUniform1i(uniformLocations.get(name), i);
	}
	
	public void uniformData1f(String name, float f)
	{
		checkExistence(name);
		
		glUniform1f(uniformLocations.get(name), f);
	}
	
	public void uniformData3f(String name, Vector3f v) 
	{
		glUniform3f(uniformLocations.get(name), v.getX(), v.getY(), v.getZ());	
	}
	
	public void uniformData4f(String name, Matrix4f m)
	{
		checkExistence(name);
		
		glUniformMatrix4(uniformLocations.get(name), true, engine.util.BufferHelper.createMatrixFloatArray(m)); 
	}
	
	private void checkExistence(String name)
	{
		if(!uniformLocations.containsKey(name))
		{
			LogHelper.printError("Could not find uniform: " + name);
			Main.quit();
		}
	}

}
