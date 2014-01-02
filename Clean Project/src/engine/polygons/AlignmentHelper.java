package engine.polygons;

import engine.math.Vector3f;

public class AlignmentHelper
{
	public static void alignLowerTo(StandardMesh m, float yPlane)
	{
		float lowest = m.getDepth();
		
		Vector3f currentTransform = m.getTranslation();
		currentTransform.setY(yPlane + lowest);
		
		m.setTranslation(currentTransform);
	}
	
	public static void alignUpperTo(StandardMesh m, float yPlane)
	{
		float lowest = m.getHeight();
		
		Vector3f currentTransform = m.getTranslation();
		currentTransform.setY(yPlane - lowest);
		
		m.setTranslation(currentTransform);
	}
}
