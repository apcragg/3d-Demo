package game.levels;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import engine.levels.Level;
import engine.lighting.AmbientLight;
import engine.lighting.Light;
import engine.lighting.LightingHandler;
import engine.lighting.PlayerSpotLight;
import engine.lighting.PointLight;
import engine.lighting.ShadowMapFBO;
import engine.lighting.ShadowSpotLight;
import engine.main.Game;
import engine.main.Main;
import engine.main.Window;
import engine.materials.Material;
import engine.materials.Texture;
import engine.math.Matrix4f;
import engine.math.Transform;
import engine.math.Vector3f;
import engine.polygons.StandardMesh;
import engine.polygons.Vertex;
import engine.renderer.Camera;
import engine.renderer.FramebufferHelper;
import engine.renderer.GaussianBlur;
import engine.renderer.RenderHelper;
import engine.util.InputHelper;
import engine.util.ObjectLoader;

public class ShadowLevel extends Level
{
	private LightingHandler lights;
	private List<StandardMesh> meshes;
	private List<Vertex> stressTest;
	
	public ShadowLevel()
	{
		lights = new LightingHandler();
		meshes = new ArrayList<StandardMesh>();
		
		stressTest = ObjectLoader.loadOBJ("/res/OBJ/highSphere.obj");
		
		setup();
	}
	
	private void setup()
	{		
		RenderHelper.setBackfaceCulling(true);
		
		lights.addLight(new AmbientLight(.01f, Light.WHITE_LIGHT));
		lights.addLight(new ShadowSpotLight(new Vector3f(-80f, 68f, 0f), new Vector3f(1f, .894f, .807f), new Vector3f(1f, -.65f, 0f), .4f, 45f));
		lights.addLight(new ShadowSpotLight(new Vector3f(90f, 68f, 80f), new Vector3f(1f, .89f, .89f), new Vector3f(-1f, -.65f, -1f), .4f, 45f));
		lights.addLight(new ShadowSpotLight(new Vector3f(0f, 66f, -80f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, -.8f, 1f), .4f, 45f));	
		//lights.addLight(new PlayerSpotLight(new Vector3f(1f, 1f, 1f), .7f, 10f));
		
		//starting point
		Camera.setPos(new Vector3f(70f, 56f, 60f));
		
		Material building = new Material("buildingMtl");
		building.setTexture(0, new Texture("buildingTex_COLOR.png").getTextureID());
		building.setTexture(1, new Texture("buildingTex_NRM.png").getTextureID());
		building.setSpecIntensity(.5f);
		building.setSpecularExponenet(64);
		
		//Meshes		
		StandardMesh floor = new StandardMesh();
		floor.addVertices(ObjectLoader.loadOBJ("/res/OBJ/ground.obj"));
		floor.setMaterial("groundMtl");
		floor.formMesh();
		floor.setScale(3f);
		floor.setTranslation(new Vector3f(0f, -floor.getHeight(), 0f));
		floor.setRotation(new Vector3f(0f, 0f, 0f));
		floor.setTextureScale(.1f);
		meshes.add(floor);
		
		StandardMesh object0 = new StandardMesh();
		object0.addVertices(ObjectLoader.loadOBJ("/res/OBJ/pyro.obj"));
		object0.setMaterial("Material__10");
		object0.formMesh();
		object0.setScale(1f);
		object0.setRotation(new Vector3f(0f, 0f, 0f));
		object0.setTranslation(new Vector3f(0f, 38f, 0f));
		meshes.add(object0);	
		
		StandardMesh object1 = new StandardMesh();
		object1.addVertices(ObjectLoader.loadOBJ("/res/OBJ/building.obj"));
		object1.setMaterial("buildingMtl");
		object1.formMesh();
		object1.setScale(.75f);
		object1.setRotation(new Vector3f(0f, 0f, 0f));
		object1.setTranslation(new Vector3f(15f,  - .5f, -25f));	
		meshes.add(object1);	
		
		StandardMesh object2 = new StandardMesh();
		object2.addVertices(ObjectLoader.loadOBJ("/res/OBJ/light.obj"));
		object2.setMaterial("default");
		object2.formMesh();
		object2.setScale(.3f);
		object2.setRotation(new Vector3f(0f, 0f, 0f));
		object2.setTranslation(new Vector3f(15f, 1f + meshes.get(0).getHeight() * 2, -25f));
		meshes.add(object2);	
		
		StandardMesh object3 = new StandardMesh();
		object3.addVertices(stressTest);
		object3.setMaterial("groundMtl");
		object3.setScale(.35f);
		object3.setTextureScale(.15f);
		object3.setRotation(new Vector3f(0f, 0f, 0f));
		object3.setTranslation(new Vector3f(35f, object3.getRWidth() + 1.8f, 55f));
		object3.formMesh();				
		
		meshes.add(object3);
	}
	
	
	public void render()
	{
		Game.renderer.startRender();
		
		Game.renderer.render(lights, meshes);
		Game.renderer.shadowRender(lights, meshes);
		
		Game.renderer.endRender();
	
		RenderHelper.renderTextureQuad(((ShadowSpotLight) lights.getShadowSpotLights().get(0)).getLightMap().getC_texture());
	}

	public void update()
	{		
		updateInput();
		lights.update();
		for(StandardMesh m : meshes) m.update();	
	}
	
	private void updateInput()
	{
		//object updating
		Camera.update();
		InputHelper.update();
		
		//meshes.get(3).setTranslation(Camera.getPos());
		
		//conditional updating
		if(InputHelper.isKeyDown(Keyboard.KEY_ESCAPE)) Main.quit();
		
		if(InputHelper.isKeyPressed(Keyboard.KEY_F)) deployLight();
		
		if(InputHelper.isKeyDown(Keyboard.KEY_Q)) meshes.get(1).translate(new Vector3f(0f, 0f, .15f));
		
		if(InputHelper.isKeyDown(Keyboard.KEY_E)) meshes.get(1).translate(new Vector3f(0f, 0f, -.15f));
		
		if(InputHelper.isKeyDown(Keyboard.KEY_UP)) meshes.get(1).translate(new Vector3f(0f, .15f, 0f));
		
		if(InputHelper.isKeyDown(Keyboard.KEY_DOWN)) meshes.get(1).translate(new Vector3f(0f, -.15f, 0f));
		
		if(InputHelper.isKeyDown(Keyboard.KEY_LEFT)) meshes.get(1).translate(new Vector3f(.15f, 0f, 0f));
		
		if(InputHelper.isKeyDown(Keyboard.KEY_RIGHT)) meshes.get(1).translate(new Vector3f(-.15f, 0f, 0f));
		
		if(!InputHelper.isKeyDown(Keyboard.KEY_R)) meshes.get(1).rotate(new Vector3f(0f, .75f, 0f));
		
		if(!InputHelper.isKeyDown(Keyboard.KEY_Z)) Transform.setZoom(false);
		
		if(InputHelper.isKeyDown(Keyboard.KEY_Z)) Transform.setZoom(true);
	}
	
	private void deployLight()
	{
		StandardMesh light = new StandardMesh();
		light.setMaterial("groundMtl");
		light.addVertices(stressTest);
		light.formMesh();
		light.setScale(.15f);
		Vector3f lightPos = new Vector3f(Camera.getPos());

		light.setTranslation(lightPos);
		//lights.addLight(new PointLight(lightPos, new Vector3f(5f, (1f / 2), (1f / 1)), new Vector3f(1.0f - ((float) Math.random() * .2f), 1.0f - ((float) Math.random() * .2f), 1.0f - ((float) Math.random() * .2f)), 75f));
		meshes.add(light);
	}

}
