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
	
	public org.newdawn.slick.opengl.Texture getTexture()
	{
		if(texture.getTextureID() < 0)
			LogHelper.printError("Failed to load texture.");
		
		return texture;
	}

}
