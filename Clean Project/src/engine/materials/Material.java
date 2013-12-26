package engine.materials;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import engine.main.Game;
import engine.main.Main;
import engine.math.Vector3f;
import engine.util.LogHelper;

public class Material
{
	private static Map<String, Material> materials = new HashMap<String, Material>();
	private static String lastUpdated = "";
	
	private String name;	// The unique id of the material.
	
	private Vector3f specularColor;
	private float specIntensity;
	private int specularExponenet;
	
	private int[] textures;
	private boolean[] activatedTextures;
	
	/**
	 * Creates a new material with the passed in name id. If there already exists a material by that name it returns,
	 * else it initializes the values of the material to the hard-coded defaults.
	 * @param name The unique name of the material.
	 */
	
	public Material(String name)
	{		
		if(!materials.containsKey(name))
		{
			this.setName(name);
			
			textures = new int[4];
			specularColor = new Vector3f(1f, 1f, 1f);
			setSpecularExponenet(64);
			setSpecIntensity(1.0f);
			
			activatedTextures = new boolean[4];
			
			for(int i = 0; i < 4; i++)
				activatedTextures[i] = false;
			
			materials.put(name, this);
			
			LogHelper.printInfo(name + " added as a new material.");
		}
		else
		{
			LogHelper.printInfo("Material named " + name + " already exists.");
			
			Main.quit();
		}
	}
	
	/**
	 * Updates the material if the previous material was different than this one.
	 * Activates the GL texture slots for each activated texture with a valid id and
	 * binds the id to that shader slot.
	 */
	
	public void update()
	{
		if(!this.name.equals(lastUpdated))
		{
			lastUpdated = this.name;
			
			/*
			 * Texture updating
			 */
			
			for(int i = 0; i < 4; i++)
			{
				if(activatedTextures[i])
				{			
					glActiveTexture(GL_TEXTURE0 + i);
					glBindTexture(GL_TEXTURE_2D, textures[i]);
				}
				if(activatedTextures[i] && i == 1)
				{
					Game.shader.uniformData1i("normalMap", 1);
				}
			}
			
			if(!activatedTextures[1])
			{
				Game.shader.uniformData1i("normalMap", 0);
			}
			
			/*
			 * Uniform updating
			 */
			
			Game.shader.uniformData3f("specColor", specularColor);
			Game.shader.uniformData1i("specExp", specularExponenet);
			Game.shader.uniformData1f("specIntensity", specIntensity);
			
			
		}
	}
	
	/**
	 * Flags a specified texture slot as active and adds the id to that spot.
	 * Slot 3 should be reserved for the normal map as good practice.
	 * @param slot The texture atlas slot for the texture to be added to.
	 * @param id The GL id of the texture.
	 **/
	
	public void setTexture(int slot, int id)
	{
		activatedTextures[slot] = true;
		
		if(slot < 4 && slot >= 0)
			textures[slot] = id;
	}

	public Vector3f getSpecularColor() {
		return specularColor;
	}

	public void setSpecularColor(Vector3f specularColor) {
		this.specularColor = specularColor;
	}

	public int getSpecularExponenet() {
		return specularExponenet;
	}

	public void setSpecularExponenet(int specularExponenet) {
		this.specularExponenet = specularExponenet;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static Material getMaterial(String name)
	{
		return materials.get(name);
	}

	public float getSpecIntensity() {
		return specIntensity;
	}

	public void setSpecIntensity(float specIntensity) {
		this.specIntensity = specIntensity;
	}

}
