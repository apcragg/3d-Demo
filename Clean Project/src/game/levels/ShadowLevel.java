package game.levels;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import engine.levels.Level;
import engine.lighting.AmbientLight;
import engine.lighting.Light;
import engine.lighting.LightingHandler;
import engine.lighting.ShadowMapFBO;
import engine.lighting.ShadowSpotLight;
import engine.lighting.SpotLight;
import engine.main.Game;
import engine.main.Main;
import engine.materials.Texture;
import engine.math.Matrix4f;
import engine.math.Transform;
import engine.math.Vector3f;
import engine.polygons.StandardMesh;
import engine.renderer.Camera;
import engine.renderer.RenderHelper;
import engine.util.InputHelper;
import engine.util.ObjectLoader;

public class ShadowLevel extends Level
{
	private LightingHandler lights;
	private List<StandardMesh> meshes;
	private ShadowMapFBO shadowMap;
	
	int test;
	
	public ShadowLevel()
	{
		lights = new LightingHandler();
		meshes = new ArrayList<StandardMesh>();
		
		test = new Texture("test.png").getTextureID();
		shadowMap = new ShadowMapFBO(1024, 1024);
		
		setup();
	}
	
	private void setup()
	{
		lights.addLight(new AmbientLight(.02f, Light.WHITE_LIGHT));
		lights.addLight(new ShadowSpotLight(new Vector3f(-50f, 26f, 0f), new Vector3f(1f, 1f, 1f), new Vector3f(1f, -.5f, 0f), .4f, 5f));
		
		//Meshes		
		StandardMesh floor = new StandardMesh();
		floor.addVertices(ObjectLoader.loadOBJ("/res/OBJ/floor.obj"));
		floor.setMaterial("lightMaterial");
		floor.formMesh();
		floor.setTranslation(new Vector3f());
		floor.setScale(1f);
		meshes.add(floor);
		
		StandardMesh sphere = new StandardMesh();
		sphere.addVertices(ObjectLoader.loadOBJ("/res/OBJ/light.obj"));
		sphere.setMaterial("testMaterial");
		sphere.formMesh();
		sphere.setTranslation(new Vector3f(0f, 16f, 0f));
		sphere.setScale(4f);
		meshes.add(sphere);
		
		
	}
	
	public void render()
	{
		shadowPass();
		renderPass();
		RenderHelper.renderQuad(shadowMap.getD_texture());
	}
	
	private void shadowPass()
	{
		shadowMap.writeBind();
		
		Game.shader.uniformData4f("viewSpace", Transform.lightSpace());
		Game.shader.uniformData4f("projectedSpace", new Matrix4f());	
		
		for(StandardMesh m : meshes) m.render();
		
		shadowMap.writeUnBind();
	}
	
	private void renderPass()
	{
		//pre-render
		RenderHelper.clear();
		
		//shader uniform updating
		Game.shader.uniformData4f("viewSpace", Transform.viewSpace());
		Game.shader.uniformData4f("projectedSpace", Transform.perspectiveMatrix());	
		
		//shadow
		shadowMap.readBind();

		//render
		for(StandardMesh m : meshes) m.render();
		
		shadowMap.readUnBind();
	}

	public void update()
	{
		for(StandardMesh m : meshes) m.update();
		lights.update();
		
		//inputs
		Camera.update();
		InputHelper.update();
		if (InputHelper.isKeyDown(Keyboard.KEY_ESCAPE)) Main.quit();
	}

}
