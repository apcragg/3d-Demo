package engine.polygons;

import engine.components.Component;

/**
 * Interface for renderable openGL Meshes. Objects implementing this are able to
 * be handled by the MeshHandler class which optimizes thier render calling and
 * updating.
 * 
 * @author Andrew Cragg
 * 
 */

public interface Mesh extends Component
{
	final static int SIZE = 3;

	public void render();

	public void update();
}
