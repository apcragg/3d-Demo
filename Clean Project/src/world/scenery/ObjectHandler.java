package world.scenery;

import java.util.ArrayList;
import java.util.HashMap;

import engine.polygons.StandardMesh;

public class ObjectHandler
{
	private HashMap<String, ArrayList<StandardMesh>> materialBuckets;

	public ObjectHandler()
	{
		materialBuckets = new HashMap<String, ArrayList<StandardMesh>>();
	}
	
	public boolean add(StandardMesh m)
	{
		if(materialBuckets.containsKey(m.getMaterial()))
		{
			materialBuckets.get(m.getMaterial()).add(m);
			
			return true;
		}
		else
		{	
			materialBuckets.put(m.getMaterial(), new ArrayList<StandardMesh>());
			
			return false;
		}

	}

}
