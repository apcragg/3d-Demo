package engine.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class ShaderLoader
{

	public static String loadShader(String location)
	{
		StringBuilder source = new StringBuilder();
		Scanner file;

		try
		{
			file = new Scanner(new FileReader(System.getProperty("user.dir")
					+ location));

			LogHelper.printInfo(System.getProperty("user.dir") + location);

			while (file.hasNext())
			{
				source.append(file.nextLine() + '\n');
			}

			file.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println();
			e.printStackTrace();
		}

		return source.toString();
	}

}
