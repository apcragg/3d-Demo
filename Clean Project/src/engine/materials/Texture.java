package engine.materials;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import engine.util.LogHelper;

public class Texture 
{
	private String filePath;
	private org.newdawn.slick.opengl.Texture texture;
	
	/**
	 * Creates and loads a new texture from file using slick util. Must provide the file name and path
	 * from the main project directory - ie. "/res/textures/foo.png"
	 * @param filePath The file path pointing to the texture from the main project directory.
	 */
	
	public Texture(String filePath)
	{
		this.filePath = System.getProperty("user.dir") + "/res/textures/" + filePath;
		
		loadTexture();
	}
	
	private void loadTexture()
	{
		Pattern regex = Pattern.compile("(\\..+)");
		Matcher match = regex.matcher(filePath);
		
		match.find();
		
		String extension = match.group();
		
		try 
		{
			texture = TextureLoader.getTexture(extension, ResourceLoader.getResourceAsStream(filePath));
		} 
		catch (IOException e) 
		{
			System.out.println("Failed to load texture at: " + filePath);
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the SlickUtil-loaded texture object.
	 * @return Texture object loaded by slick util
	 */
	
	public org.newdawn.slick.opengl.Texture getTexture()
	{
		if(texture.getTextureID() < 0)
			LogHelper.printError("Failed to load texture.");
		
		return texture;
	}
	
	/**
	 * Gets the OpenGl-usable texture id
	 * @return The OpenGL int texture id
	 */
	
	public int getTextureID()
	{
		return this.texture.getTextureID();
	}

}
