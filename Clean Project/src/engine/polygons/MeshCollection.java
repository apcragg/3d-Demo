package engine.polygons;

import java.util.ArrayList;
import java.util.List;

public class MeshCollection
{
	List<StandardMesh> meshes;

	public MeshCollection()
	{
		meshes = new ArrayList<StandardMesh>();
	}
	
	public void render()
	{
		for(StandardMesh m  : meshes)
		{
			m.render();
		}
	}
	
	public void update()
	{
		for(StandardMesh m  : meshes)
		{
			m.update();
		}
	}

	public void addMesh(StandardMesh m)
	{
		meshes.add(m);
	}
	
	public StandardMesh getMesh(int index)
	{
		return meshes.get(index);
	}
}
