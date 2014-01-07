package engine.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import engine.math.Matrix4f;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.polygons.Vertex;

public class ObjectLoader
{
	public static List<Vertex> loadOBJ(String path)
	{
		Scanner file;
		List<Vector3f> positions = new ArrayList<Vector3f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Vector2f> uvs = new ArrayList<Vector2f>();
		List<Vertex> faceVertices = new LinkedList<Vertex>();

		boolean uv = false;
		boolean normal = false;

		// ro
		Matrix4f fixMatrix = new Matrix4f().arbitraryAxisRotate(new Vector3f(1f, 0f, 0f), 270);
		
		try
		{
			file = new Scanner(new FileReader(new File(System.getProperty("user.dir")
					+ path)));

			// read the file until it's finsihed
			while (file.hasNext())
			{
				String line = file.nextLine() + '\n';

				// ignore comments and empty lines
				if (line.startsWith("#")) continue;
				if (line.length() == 0) continue;

				// loads vertex positions
				if (line.startsWith("v"))
				{
					Pattern regex = Pattern.compile("(-?\\d+\\.\\d+)");
					Matcher match = regex.matcher(line);

					Vector3f v = new Vector3f();

					for (int i = 0; i < 3 && match.find(); i++)
					{
						v.set(Float.valueOf(match.group(0)), (i));
					}
					
					v.swapYZ();

				//	positions.add(fixMatrix.mul(v));
					positions.add(v);
				}

				// loads texture coords
				if (line.startsWith("vt"))
				{
					uv = true;

					Pattern regex = Pattern.compile("(-?\\d+\\.\\d+)");
					Matcher match = regex.matcher(line);

					Vector2f v = new Vector2f();

					for (int i = 0; i < 2 && match.find(); i++)
					{
						v.set(Float.valueOf(match.group(0)), i);
					}
		
					uvs.add(v);
				}

				// loads normals
				if (line.startsWith("vn"))
				{
					normal = true;

					Pattern regex = Pattern.compile("(-?\\d+\\.\\d+)");
					Matcher match = regex.matcher(line);

					Vector3f v = new Vector3f();

					for (int i = 0; i < 3 && match.find(); i++)
					{
						v.set(Float.valueOf(match.group(0)), i);
					}
					
					v.swapYZ();

					normals.add(v);

				}

				/*
				 * I couldn't tell you why this one is group(1). It just works.
				 * Regular expressions are weird.
				 */

				if (line.startsWith("f"))
				{
					Pattern regex = Pattern.compile("(\\d+)(?:(?:\\r|\\n)|(?:\\/|\\s|\\/\\/))");
					Matcher match = regex.matcher(line);

					int[] faces = new int[(uv && normal ? 9 : uv || normal ? 6
							: 3)];

					for (int i = 0; i < faces.length && match.find(); i++)
					{
						faces[i] = Integer.valueOf(match.group(1));
					}

					int mul = faces.length / 3;

					for (int j = 0; j < 3; j++)
					{
						if (!normal && !uv) faceVertices.add(new Vertex(positions.get(faces[mul
								* j] - 1)));
						if (normal && !uv) faceVertices.add(new Vertex(positions.get(faces[mul
								* j] - 1), normals.get(faces[j * mul + 1] - 1)));
						if (!normal && uv) faceVertices.add(new Vertex(positions.get(faces[mul
								* j] - 1), uvs.get(faces[j * mul + 1] - 1)));
						if (normal && uv) faceVertices.add(new Vertex(positions.get(faces[mul
								* j] - 1), uvs.get(faces[j * mul + 1] - 1), normals.get(faces[j
								* mul + 2] - 1)));
					}

				}

				// loads the material file which should be located in /res/mtl
				if (RegexHelper.find(("mtllib"), line))
				{
					Matcher match = RegexHelper.getRegex("mtllib (.*)", line);

					MaterialLoader.loadMaterial("/res/mtl/" + match.group(1));
				}

			}

			file.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println();
			e.printStackTrace();
		}

		calculateTangents(faceVertices);

		return faceVertices;
	}

	// why the hell did i put this method in this class? java, why can't you be
	// like c++ and let me have global functions or at least class independent
	// ones
	public static String[] removeEmptyStrings(String[] data)
	{
		ArrayList<String> result = new ArrayList<String>();

		for (int i = 0; i < data.length; i++)
		{
			if (!data[i].equals(" ") && !data[i].equals("")
					&& !data.equals("  "))
			{
				result.add(data[i]);
			}
		}
		String[] res = new String[result.size()];
		result.toArray(res);

		return res;
	}

	/**
	 * Mostly benny's code. Github at
	 * https://github.com/BennyQBD/GDXJ/blob/master
	 * /src/com/base/engine/Mesh.java
	 * 
	 * @param vertices
	 *            List of vertices?
	 */

	public static void calculateTangents(List<Vertex> vertices)
	{
		for (int i = 0; i < vertices.size(); i += 3)
		{
			Vertex v0 = vertices.get(i);
			Vertex v1 = vertices.get(i + 1);
			Vertex v2 = vertices.get(i + 2);

			Vector3f edge1 = v1.getPosition().subtract(v0.getPosition());
			Vector3f edge2 = v2.getPosition().subtract(v0.getPosition());

			float deltaU1 = v1.getU() - v0.getU();
			float deltaU2 = v2.getU() - v0.getU();
			float deltaV1 = v1.getV() - v0.getV();
			float deltaV2 = v2.getV() - v0.getV();

			float f = 1.0f / (deltaU1 * deltaV2 - deltaU2 * deltaV1);

			Vector3f tangent = new Vector3f(0, 0, 0);

			tangent.setX(f * (deltaV2 * edge1.getX() - deltaV1 * edge2.getX()));
			tangent.setY(f * (deltaV2 * edge1.getY() - deltaV1 * edge2.getY()));
			tangent.setZ(f * (deltaV2 * edge1.getZ() - deltaV1 * edge2.getZ()));

			v0.setTangent(v0.getTangent().add(tangent));
			v1.setTangent(v1.getTangent().add(tangent));
			v2.setTangent(v2.getTangent().add(tangent));
		}
	}

}
