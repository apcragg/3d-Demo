package engine.polygons;

/**
 * Interface for renderable openGL Meshes. Objects implementing this are able to be handled by
 * the MeshHandler class which optimizes thier render calling and updating. 
 * @author Andrew Cragg
 *
 */

public interface Mesh
{
	final static int SIZE = 3;
	
	public void render();
	public void update();
}
