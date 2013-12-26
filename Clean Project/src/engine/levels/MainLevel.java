package engine.levels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Keyboard;

import world.scenery.SceneryPool;
import engine.lighting.AmbientLight;
import engine.lighting.DirectionalLight;
import engine.lighting.Light;
import engine.lighting.LightingHandler;
import engine.lighting.PlayerSpotLight;
import engine.lighting.PointLight;
import engine.lighting.SpotLight;
import engine.main.Game;
import engine.main.Main;
import engine.main.Window;
import engine.materials.Material;
import engine.materials.Texture;
import engine.math.Transform;
import engine.math.Vector3f;
import engine.polygons.StandardMesh;
import engine.polygons.Vertex;
import engine.renderer.Camera;
import engine.shaders.PhongShader;
import engine.util.InputHelper;
import engine.util.MaterialLoader;
import engine.util.ObjectLoader;
import engine.util.ProfilerHelper;


public class MainLevel extends Level
{
	private LightingHandler lights;
	private SceneryPool pool;
	private List<Vertex> basicModel;
	
	private List<StandardMesh> meshes;
	
	public MainLevel()
	{
		super();	
		setup();
	}
	
	private void setup()
	{
		lights 		= new LightingHandler();
		meshes		= new ArrayList<StandardMesh>();
		
		basicModel	= ObjectLoader.loadOBJ("/res/OBJ/light.obj");
		
		new Camera();
		
		lights.addLight(new AmbientLight(.001f, new Vector3f(1f, 1f, 1f)));
		//lights.addLight(new DirectionalLight(.7f, new Vector3f(1f, 1f, 1f), new Vector3f(0.5f, -.5f, .3f)));
		lights.addLight(new PlayerSpotLight(new Vector3f(), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, 1f), .7f, 10f));
		
		defaultMaterial.setTexture(0, new Texture("test.png").getTexture().getTextureID());
		
		Transform.setupPerspective(Window.ASPECT_RATIO, 85, 1000f);
		PhongShader.useProgram();
		
		MaterialLoader.loadMaterial("/res/mtl/ground.mtl");
		MaterialLoader.loadMaterial("/res/mtl/sphere.mtl");
		
		StandardMesh teapot = new StandardMesh();
		teapot.addVertices(ObjectLoader.loadOBJ("/res/OBJ/sphere.obj"));
		teapot.setScale(1f);
		teapot.setTranslation(new Vector3f(0f, 15.725f, 0f)); //5.725f
		teapot.formMesh();
		teapot.setMaterial("sphere");
		meshes.add(teapot);
		
		Material testMaterial = new Material("testMaterial");
		testMaterial.setTexture(0, new Texture("grid2.png").getTexture().getTextureID());
		testMaterial.setSpecularExponenet(128);
		testMaterial.setSpecIntensity(.7f);
		
		Material lightMaterial = new Material("lightMaterial");
		lightMaterial.setTexture(0, new Texture("blank.png").getTexture().getTextureID());
		//lightMaterial.setTexture(1, new Texture("weirdNormal.jpg").getTexture().getTextureID());
		
		StandardMesh floor = new StandardMesh();
		floor.addVertices(ObjectLoader.loadOBJ("/res/OBJ/floor.obj"));
		floor.setMaterial("stone");
		floor.setTranslation(new Vector3f());
		floor.setTextureScale(.25f);
		floor.formMesh();
		meshes.add(floor);
		
		Material wood = new Material("wood");
		wood.setTexture(0, new Texture("wood.png").getTexture().getTextureID());
		wood.setTexture(1, new Texture("weirdNormal.png").getTexture().getTextureID());
		wood.setSpecIntensity(1.5f);
		wood.setSpecularExponenet(32);
		
		Material stone = new Material("stone");
		stone.setTexture(0, new Texture("stone.png").getTexture().getTextureID());
		stone.setTexture(1, new Texture("stoneNormal.png").getTexture().getTextureID());
		stone.setSpecIntensity(2f);
		stone.setSpecularExponenet(128);
		
		StandardMesh house = new StandardMesh();
		house.addVertices(ObjectLoader.loadOBJ("/res/OBJ/ground.obj"));
		house.setMaterial("GroundMtl");
		house.setTranslation(new Vector3f(120f, 0f, 120f));
		house.setTextureScale(1f);
		house.formMesh();
		house.setScale(.25f);
		meshes.add(house);
		
		pool = new SceneryPool();
		
		for(int i = 0 ; i < 250; i++)
		{
			addTestBlock(pool);
		}
		
		pool.formMesh();
		addLights();
	}

	public void render()
	{
		Game.shader.uniformData4f("viewSpace", Transform.viewSpace());
		Game.shader.uniformData4f("projectedSpace", Transform.perspectiveMatrix());
		
		pool.render();
		
		for(StandardMesh m : meshes)
		{
			m.render();
		}
	}

	public void update()
	{
		//Light updating

		lights.update();
		
		//Mesh updating
		for(StandardMesh m : meshes)
			m.update();
		
		pool.update();
		
		//input junk and shit idk
		Camera.update();
		InputHelper.update();
		
		if(InputHelper.isKeyDown(Keyboard.KEY_ESCAPE))
			Main.quit();	
		if(InputHelper.isKeyPressed(Keyboard.KEY_L))
			deployLight();
	
	}
	
	private void addLights()
	{
		StandardMesh whiteLight = new StandardMesh();
		whiteLight.setMaterial("lightMaterial");
		whiteLight.addVertices(ObjectLoader.loadOBJ("/res/OBJ/lightL.obj"));
		whiteLight.formMesh();
		whiteLight.setScale(.5f);
		Vector3f whitePos = new Vector3f(2f, 22f, 30f);
			
		whiteLight.setTranslation(whitePos);
		lights.addLight(new PointLight(whitePos, new Vector3f(0f, (0f/2), (1f/1)), new Vector3f(.9f, .9f, 1f), 35f));
		meshes.add(whiteLight);
		
		StandardMesh blueLight = new StandardMesh();
		blueLight.setMaterial("lightMaterial");
		blueLight.addVertices(ObjectLoader.loadOBJ("/res/OBJ/lightL.obj"));
		blueLight.formMesh();
		blueLight.setScale(.5f);
		Vector3f bluePos = new Vector3f(15f, 12f, -30f);
			
		blueLight.setTranslation(bluePos);
		lights.addLight(new PointLight(bluePos, new Vector3f(0f, (0f/2), (1f/1)), new Vector3f(.3f, .1f, 1f), 50f));
		meshes.add(blueLight);
		
		StandardMesh greenLight = new StandardMesh();
		greenLight.setMaterial("lightMaterial");
		greenLight.addVertices(ObjectLoader.loadOBJ("/res/OBJ/lightL.obj"));
		greenLight.formMesh();
		greenLight.setScale(.5f);
		Vector3f greenPos = new Vector3f(40f, 26f, 10f);
			
		greenLight.setTranslation(greenPos);
		lights.addLight(new PointLight(greenPos, new Vector3f(0f, (0f/2), (1f/1)), new Vector3f(.1f, 1f, .4f), 50f));
		meshes.add(greenLight);
		
		StandardMesh pinkLight = new StandardMesh();
		pinkLight.setMaterial("lightMaterial");
		pinkLight.addVertices(ObjectLoader.loadOBJ("/res/OBJ/lightL.obj"));
		pinkLight.formMesh();
		pinkLight.setScale(.5f);
		Vector3f pinkPos = new Vector3f(-30f, 26f, 12f);
			
		pinkLight.setTranslation(pinkPos);
		lights.addLight(new PointLight(pinkPos, new Vector3f(0f, (0f/2), (1f/1)), new Vector3f(1f, (105f/255f), (180f/255f)), 50f));
		meshes.add(pinkLight);
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
		lights.addLight(new PointLight(lightPos, new Vector3f(0f, (0f/2), (1f/1)), new Vector3f((float) Math.random(), (float) Math.random(), (float) Math.random()), 50f));
		//testBoxes.addMesh(light);
	}
	
	private void addTestBlock(SceneryPool p)
	{
		StandardMesh light = new StandardMesh();
		light.setMaterial("lightMaterial");
		light.addVertices(basicModel);
		light.setScale(.5f);
		light.formMesh();
		Vector3f lightPos = new Vector3f((float) (Math.random() * 2f) - 1f, (float) Math.random(), (float) (Math.random() * 2f) - 1f).mul(75);	
		light.setTranslation(lightPos);
		p.addMesh(light);
	}
	
}
