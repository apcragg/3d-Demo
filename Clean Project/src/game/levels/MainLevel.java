package game.levels;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL14;

import static org.lwjgl.opengl.GL11.*;
import world.scenery.SceneryPool;
import engine.levels.Level;
import engine.lighting.AmbientLight;
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
import engine.renderer.RenderHelper;
import engine.shaders.PhongShader;
import engine.util.InputHelper;
import engine.util.MaterialLoader;
import engine.util.ObjectLoader;

public class MainLevel extends Level
{
	private LightingHandler lights;
	private SceneryPool pool;
	private ShadowMapFBO shadowMap;
	private List<Vertex> basicModel;

	private List<StandardMesh> meshes;

	public MainLevel()
	{
		super();
		setup();
	}

	private void setup()
	{
		lights = new LightingHandler();
		meshes = new ArrayList<StandardMesh>();

		basicModel = ObjectLoader.loadOBJ("/res/OBJ/sphere.obj");
		shadowMap = new ShadowMapFBO(1024, 1024);
		shadowMap.writeUnBind();

		new Camera();
		RenderHelper.setBackfaceCulling(true);

		lights.addLight(new AmbientLight(.001f, new Vector3f(1f, 1f, 1f)));
		// lights.addLight(new DirectionalLight(.7f, new Vector3f(1f, 1f, 1f),
		// new Vector3f(0.5f, -.5f, .3f)));
		lights.addLight(new ShadowSpotLight(new Vector3f(75f, 50f, 3f), new Vector3f(1f, 0f, 0f), new Vector3f(-0.77229255f, -0.48175365f, -0.41409853f),  .55f, 40f)); //-0.77229255f, -0.48175365f, -0.41409853f
		lights.addLight(new PlayerSpotLight(new Vector3f(1f, 1f, 1f), .7f, 10f));

		defaultMaterial.setTexture(0, new Texture("blank.png").getTexture().getTextureID());

		Transform.setupPerspective(85, 1000f);
		PhongShader.useProgram();

		MaterialLoader.loadMaterial("/res/mtl/ground.mtl");
		MaterialLoader.loadMaterial("/res/mtl/sphere.mtl");

		Material.getMaterial("groundMtl").setTexture(0, new Texture("ConcreteTri_COLOR.png").getTexture().getTextureID());
		Material.getMaterial("groundMtl").setTexture(1, new Texture("ConcreteTri_NRM.png").getTexture().getTextureID());
		Material.getMaterial("groundMtl").setTexture(2, new Texture("dentTexture.jpg").getTexture().getTextureID());		
		Material.getMaterial("groundMtl").setDisplacementFactor(.5f);
		Material.getMaterial("groundMtl").setParallaxMapping(false);
		Material.getMaterial("groundMtl").setDisplacementMapping(true);
		

		Material brick = new Material("brickMtl");
		brick.setTexture(0, new Texture("brickColor.jpg").getTextureID());
		brick.setTexture(1, new Texture("brickNormal.jpg").getTextureID());
		brick.setTexture(2, new Texture("brickHeight.jpg").getTextureID());
		brick.setParallaxMapping(true);
		brick.setDisplacementFactor(.025f);

		StandardMesh teapot = new StandardMesh();
		teapot.addVertices(ObjectLoader.loadOBJ("/res/OBJ/sphere.obj"));
		teapot.setScale(1.5f);
		teapot.setTranslation(new Vector3f(0f, 15.725f, 0f)); // 5.725f
		teapot.formMesh();
		teapot.setMaterial("lightMaterial");
		meshes.add(teapot);

		Material testMaterial = new Material("testMaterial");
		testMaterial.setTexture(0, new Texture("grid2.png").getTexture().getTextureID());
		testMaterial.setSpecularExponenet(128);
		testMaterial.setSpecIntensity(.7f);

		Material lightMaterial = new Material("lightMaterial");
		lightMaterial.setTexture(0, new Texture("blank.png").getTextureID());
		// lightMaterial.setTexture(1, new
		// Texture("weirdNormal.jpg").getTexture().getTextureID());

		StandardMesh floor = new StandardMesh();
		floor.addVertices(ObjectLoader.loadOBJ("/res/OBJ/ground.obj"));
		floor.setMaterial("stone");
		floor.setTranslation(new Vector3f());
		floor.setTextureScale(.25f);
		floor.formMesh();
		floor.setScale(new Vector3f(4f, 1f, 4f));
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

		StandardMesh ground = new StandardMesh();
		ground.addVertices(ObjectLoader.loadOBJ("/res/OBJ/ground.obj"));
		ground.setMaterial("groundMtl");
		ground.setTranslation(new Vector3f(35f, 7f, 35f));
		ground.setTextureScale(1f);
		ground.formMesh();
		ground.setScale(.25f);
		meshes.add(ground);

		StandardMesh metalObject = new StandardMesh();
		metalObject.addVertices(ObjectLoader.loadOBJ("/res/OBJ/sphere.obj"));
		metalObject.setMaterial("metalMtl");
		metalObject.setTranslation(new Vector3f(-35f, 15f, -35f));
		metalObject.setTextureScale(.35f);
		metalObject.formMesh();
		metalObject.setScale(2f);
		meshes.add(metalObject);

		pool = new SceneryPool();

		for (int i = 0; i < 250; i++)
		{
			addTestBlock(pool);
		}

		pool.formMesh();
		addLights();
	}

	public void render()
	{
		shadowPass();
		renderPass();	
	}
	
	private void shadowPass()
	{		
		Game.setShader(Game.SHADOW);
		shadowMap.writeBind();
		
		//TODO: Shadow renderpass
		
		shadowMap.writeUnBind();
	}

	private void renderPass()
	{
		RenderHelper.clear();
		
		Game.setShader(Game.PHONG);
		Game.getShader().uniformData4f("viewSpace", Transform.viewSpace());
		Game.getShader().uniformData4f("projectedSpace", Transform.perspectiveMatrix());
		
		Material.getMaterial("default").update();
		shadowMap.readBind(0);
		
		pool.render();
		for (StandardMesh m : meshes) m.render();	
		
		shadowMap.readUnBind();
	}
	
	public void update()
	{
		// Light updating

		lights.update();

		// Mesh updating
		for (StandardMesh m : meshes)
			m.update();

		pool.update();

		// input junk and shit idk
		Camera.update();
		InputHelper.update();

		if (InputHelper.isKeyDown(Keyboard.KEY_ESCAPE)) Main.quit();
		if (InputHelper.isKeyPressed(Keyboard.KEY_L)) deployLight();

	}

	private void addLights()
	{
		StandardMesh whiteLight = new StandardMesh();
		whiteLight.setMaterial("lightMaterial");
		whiteLight.addVertices(ObjectLoader.loadOBJ("/res/OBJ/sphere.obj"));
		whiteLight.formMesh();
		whiteLight.setScale(.5f);
		Vector3f whitePos = new Vector3f(2f, 22f, 30f);

		whiteLight.setTranslation(whitePos);
		lights.addLight(new PointLight(whitePos, new Vector3f(0f, (0f / 2), (1f / 1)), new Vector3f(.9f, .9f, 1f), 35f));
		meshes.add(whiteLight);

		StandardMesh blueLight = new StandardMesh();
		blueLight.setMaterial("lightMaterial");
		blueLight.addVertices(ObjectLoader.loadOBJ("/res/OBJ/plane.obj"));
		blueLight.formMesh();
		blueLight.setScale(20f);
		Vector3f bluePos = new Vector3f(50f, 15f, -30f);

		blueLight.setTranslation(bluePos);
		//lights.addLight(new PointLight(bluePos, new Vector3f(0f, (0f / 2), (1f / 1)), new Vector3f(.3f, .1f, 1f), 50f));
		meshes.add(blueLight);

		StandardMesh greenLight = new StandardMesh();
		greenLight.setMaterial("lightMaterial");
		greenLight.addVertices(ObjectLoader.loadOBJ("/res/OBJ/sphere.obj"));
		greenLight.formMesh();
		greenLight.setScale(.5f);
		Vector3f greenPos = new Vector3f(40f, 26f, 10f);

		greenLight.setTranslation(greenPos);
		lights.addLight(new PointLight(greenPos, new Vector3f(0f, (0f / 2), (1f / 1)), new Vector3f(.1f, 1f, .4f), 50f));
		meshes.add(greenLight);

		StandardMesh pinkLight = new StandardMesh();
		pinkLight.setMaterial("lightMaterial");
		pinkLight.addVertices(ObjectLoader.loadOBJ("/res/OBJ/sphere.obj"));
		pinkLight.formMesh();
		pinkLight.setScale(.5f);
		Vector3f pinkPos = new Vector3f(-30f, 26f, 12f);

		pinkLight.setTranslation(pinkPos);
		lights.addLight(new PointLight(pinkPos, new Vector3f(0f, (0f / 2), (1f / 1)), new Vector3f(1f, (105f / 255f), (180f / 255f)), 50f));
		meshes.add(pinkLight);
	}

	private void deployLight()
	{
		StandardMesh light = new StandardMesh();
		light.setMaterial("lightMaterial");
		light.addVertices(ObjectLoader.loadOBJ("/res/OBJ/sphere.obj"));
		light.formMesh();
		light.setScale(.5f);
		Vector3f lightPos = new Vector3f(Camera.getPos());

		light.setTranslation(lightPos);
		lights.addLight(new PointLight(lightPos, new Vector3f(0f, (0f / 2), (1f / 1)), new Vector3f((float) Math.random(), (float) Math.random(), (float) Math.random()), 50f));
		// testBoxes.addMesh(light);
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
