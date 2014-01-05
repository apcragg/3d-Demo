package game.levels;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import engine.levels.Level;
import engine.lighting.AmbientLight;
import engine.lighting.Light;
import engine.lighting.LightingHandler;
import engine.lighting.PointLight;
import engine.lighting.ShadowMapFBO;
import engine.lighting.ShadowSpotLight;
import engine.main.Game;
import engine.main.Main;
import engine.materials.Material;
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
	
	int test;
	
	public ShadowLevel()
	{
		lights = new LightingHandler();
		meshes = new ArrayList<StandardMesh>();
		
		test = new Texture("test.png").getTextureID();
		
		setup();
	}
	
	private void setup()
	{
		RenderHelper.setBackfaceCulling(false);
		
		lights.addLight(new AmbientLight(.01f, Light.WHITE_LIGHT));
		//lights.addLight(new ShadowSpotLight(new Vector3f(-80f, 68f, 0f), new Vector3f(1f, .894f, .807f), new Vector3f(1f, -.65f, 0f), .4f, 35f));
		lights.addLight(new ShadowSpotLight(new Vector3f(90f, 68f, 80f), new Vector3f(1f, .89f, .89f), new Vector3f(-1f, -.65f, -1f), .45f, 35f));
		//lights.addLight(new ShadowSpotLight(new Vector3f(0f, 66f, -50f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, -.8f, 1f), .4f, 35f));
		
		//starting point
		Camera.setPos(new Vector3f(70f, 56f, 60f));
		
		Material building = new Material("buildingMtl");
		building.setTexture(0, new Texture("buildingTex_COLOR.png").getTextureID());
		building.setTexture(1, new Texture("buildingTex_NRM.png").getTextureID());
		
		//Meshes		
		StandardMesh floor = new StandardMesh();
		floor.addVertices(ObjectLoader.loadOBJ("/res/OBJ/floor.obj"));
		floor.setMaterial("metalMtl");
		floor.formMesh();
		floor.setTranslation(new Vector3f());
		floor.setScale(2f);
		floor.setTextureScale(.05f);
		meshes.add(floor);
		
		StandardMesh object0 = new StandardMesh();
		object0.addVertices(ObjectLoader.loadOBJ("/res/OBJ/pyro0.obj"));
		object0.setMaterial("Material__10");
		object0.formMesh();
		object0.setTranslation(new Vector3f(0f, 38f, 0f));
		object0.setRotation(new Vector3f(0f, 0f, 0f));
		object0.setScale(1f);
		meshes.add(object0);	
		
		StandardMesh object1 = new StandardMesh();
		object1.addVertices(ObjectLoader.loadOBJ("/res/OBJ/building.obj"));
		object1.setMaterial("buildingMtl");
		object1.formMesh();
		object1.setTranslation(new Vector3f(15f, 1f + meshes.get(0).getHeight() * 2, -25f));
		object1.setRotation(new Vector3f(0f, 0f, 0f));
		object1.setScale(.75f);
		meshes.add(object1);	
		
		StandardMesh object2 = new StandardMesh();
		object2.addVertices(ObjectLoader.loadOBJ("/res/OBJ/light.obj"));
		object2.setMaterial("buildingMtl");
		object2.formMesh();
		object2.setTranslation(new Vector3f(15f, 1f + meshes.get(0).getHeight() * 2, -25f));
		object2.setRotation(new Vector3f(0f, 0f, 0f));
		object2.setScale(3f);
		//meshes.add(object2);	
	}
	
	public void render()
	{
		shadowPass();
		renderPass();
		RenderHelper.renderQuad(((ShadowSpotLight) lights.getShadowSpotLights().get(0)).getLightMap().getC_texture());
	}
	
	private void shadowPass()
	{
		for(Light l :  lights.getShadowSpotLights())
		{
			ShadowSpotLight light = ((ShadowSpotLight) l);
			
			light.lightSpaceUpdate();
			light.getLightMap().writeBind();
			
			Game.setShader(Game.SHADOW);
			Game.getShader().uniformData4f("lightSpace", Transform.lightSpace());
			
			for(StandardMesh m : meshes) m.render();
			
			light.getLightMap().writeUnBind();		
		}	
	}
	
	private void renderPass()
	{
		//pre-render
		RenderHelper.clear();
		
		//shader uniform updating
		Transform.setupPerspective(85, 1000f);
		
		Game.setShader(Game.PHONG);
		Game.getShader().uniformData4f("viewSpace", Transform.viewSpace());
		Game.getShader().uniformData4f("projectedSpace", Transform.perspectiveMatrix());
		
		defaultMaterial.update();
		
		for(int i = 0; i < lights.getShadowSpotLights().size(); i++)
		{
			((ShadowSpotLight) lights.getShadowSpotLights().get(i)).lightSpaceUpdate();
			Game.getShader().uniformData4f("lightSpace[" + i + "]", Transform.lightSpace());	
			
			//shadow
			((ShadowSpotLight) (lights.getShadowSpotLights().get(i))).getLightMap().readBind(i);;
		}		

		//render
		for(StandardMesh m : meshes) m.render();
		
		ShadowMapFBO.readUnBind();
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
		
		//conditional updating
		if(InputHelper.isKeyDown(Keyboard.KEY_ESCAPE)) Main.quit();
		
		if(InputHelper.isKeyPressed(Keyboard.KEY_L)) deployLight();
		
		if(InputHelper.isKeyDown(Keyboard.KEY_Q)) meshes.get(1).translate(new Vector3f(0f, 0f, .15f));
		
		if(InputHelper.isKeyDown(Keyboard.KEY_E)) meshes.get(1).translate(new Vector3f(0f, 0f, -.15f));
		
		if(InputHelper.isKeyDown(Keyboard.KEY_UP)) meshes.get(1).translate(new Vector3f(0f, .15f, 0f));
		
		if(InputHelper.isKeyDown(Keyboard.KEY_DOWN)) meshes.get(1).translate(new Vector3f(0f, -.15f, 0f));
		
		if(InputHelper.isKeyDown(Keyboard.KEY_LEFT)) meshes.get(1).translate(new Vector3f(.15f, 0f, 0f));
		
		if(InputHelper.isKeyDown(Keyboard.KEY_RIGHT)) meshes.get(1).translate(new Vector3f(-.15f, 0f, 0f));
		
		if(!InputHelper.isKeyDown(Keyboard.KEY_R)) meshes.get(1).rotate(new Vector3f(0f, 1f, 0f));
	}
	
	private void deployLight()
	{
		StandardMesh light = new StandardMesh();
		light.setMaterial("lightMaterial");
		light.addVertices(ObjectLoader.loadOBJ("/res/OBJ/light.obj"));
		light.formMesh();
		light.setScale(.5f);
		Vector3f lightPos = new Vector3f(Camera.getPos());

		light.setTranslation(lightPos);
		lights.addLight(new PointLight(lightPos, new Vector3f(0f, (0f / 2), (1f / 1)), new Vector3f((float) Math.random(), (float) Math.random(), (float) Math.random()), 50f));
		meshes.add(light);
	}

}
