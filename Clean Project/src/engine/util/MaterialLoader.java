package engine.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;

import engine.materials.Material;
import engine.materials.Texture;
import engine.math.Vector3f;

public class MaterialLoader
{
	public static boolean loadMaterial(String path)
	{
		Material material = null;

		try
		{
			Scanner file = new Scanner(new File(System.getProperty("user.dir")
					+ path));

			while (file.hasNext())
			{
				String line = file.nextLine();

				// Loads a new material and names it

				if (line.startsWith("newmtl"))
				{
					material = new Material(RegexHelper.getRegex(".+\\s(.*)", line).group(1));
				}

				// loads the diffuse map from file

				if (RegexHelper.find("\\smap_Kd", line))
				{
					String t_path = RegexHelper.getRegex("\\\\([\\w|-|_]*)\\.", line).group(1);

					String t_extension = RegexHelper.getRegex("(\\..*)", line).group(1);

					LogHelper.printInfo("Texture loaded from: " + t_path
							+ t_extension);

					material.setTexture(0, new Texture(t_path + t_extension).getTexture().getTextureID());
				}

				// loads the normal map, if there is one.

				if (RegexHelper.find("\\smap_bump", line))
				{
					String t_path = RegexHelper.getRegex("\\\\([\\w|-|_|\\s]*)\\.", line).group(1);

					String t_extension = RegexHelper.getRegex("(\\..*)", line).group(1);

					material.setTexture(1, new Texture(t_path + t_extension).getTexture().getTextureID());
				}

				// loads the specular color

				if (RegexHelper.find("kS", line))
				{
					Matcher m = RegexHelper.getRegex("(d+\\.d+)\\s(d+\\.d+)\\s(d+\\.d+)", line);

					material.setSpecularColor(new Vector3f(Float.parseFloat(m.group(1)), Float.parseFloat(m.group(2)), Float.parseFloat(m.group(3))));
				}

				// loads the specular intensity

				if (RegexHelper.find("nS", line))
				{
					Matcher m = RegexHelper.getRegex("(d+)", line);

					material.setSpecularExponenet(Integer.parseInt(m.group(1)));
				}
			}

			file.close();

		}
		catch (FileNotFoundException e)
		{
			LogHelper.printError(e.getStackTrace().toString());
			e.printStackTrace();
		}

		return false;
	}
}
