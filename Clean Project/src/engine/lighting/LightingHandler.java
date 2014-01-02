package engine.lighting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import engine.components.Component;
import engine.main.Game;

public class LightingHandler implements Component
{
	private List<Light> lights;
	private List<Light> pointLights;
	private List<Light> spotLights;
	private List<Light> shadowSpotLights;
	
	private List<Iterable<Light>> lightCollection;

	public LightingHandler()
	{
		lightCollection = new ArrayList<Iterable<Light>>();

		lights = new ArrayList<Light>();
		pointLights = new ArrayList<Light>();
		spotLights = new ArrayList<Light>();
		shadowSpotLights = new ArrayList<Light>();

		lightCollection.add(lights);
		lightCollection.add(pointLights);
		lightCollection.add(spotLights);
		lightCollection.add(shadowSpotLights);
	}

	public void update()
	{
		Game.getShader().uniformData1i("plNum", pointLights.size());
		Game.getShader().uniformData1i("slNum", spotLights.size());
		Game.getShader().uniformData1i("s2Num", shadowSpotLights.size());

		for (Iterable<Light> l : lightCollection)
		{
			Iterator<Light> it = l.iterator();
			Collections.sort((List<Light>) l);

			while (it.hasNext())
			{
				it.next().update();
			}
		}

		SpotLight.reset();
		ShadowSpotLight.reset();
		PointLight.reset();
	}

	public boolean addLight(Light l)
	{
		if (l instanceof AmbientLight)
		{
			lights.add(l);

			return true;
		}

		if (l instanceof DirectionalLight)
		{
			lights.add(l);

			return true;
		}

		if (l instanceof PointLight)
		{
			pointLights.add(l);

			return true;
		}
		
		if (l instanceof ShadowSpotLight)
		{
			shadowSpotLights.add(l);

			return true;
		}


		if (l instanceof SpotLight)
		{
			spotLights.add(l);

			return true;
		}

		return false;
	}

	public void render()
	{

	}

	public void setup()
	{

	}

	public List<Light> getLights()
	{
		return lights;
	}

	public List<Light> getPointLights()
	{
		return pointLights;
	}

	public List<Light> getSpotLights()
	{
		return spotLights;
	}

	public List<Light> getShadowSpotLights()
	{
		return shadowSpotLights;
	}

}
