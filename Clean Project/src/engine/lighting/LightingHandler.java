package engine.lighting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import engine.components.Component;
import engine.main.Game;

public class LightingHandler implements Component
{
	public  List<Light> lights;
	public  List<Light> pointLights;
	public  List<Light> spotLights;
	
	public  List<Iterable<Light>> lightCollection;
	
	public LightingHandler()
	{
		lightCollection = new ArrayList<Iterable<Light>>();
		
		lights 		= new ArrayList<Light>();
		pointLights = new ArrayList<Light>();
		spotLights = new ArrayList<Light>();	
			
		lightCollection.add(lights);
		lightCollection.add(pointLights);
		lightCollection.add(spotLights);
	}
	
	public void update()
	{
		Game.shader.uniformData1i("plNum", pointLights.size());
		Game.shader.uniformData1i("slNum", spotLights.size());
		
		for(Iterable<Light> l : lightCollection)
		{
			Iterator<Light> it = l.iterator();
			Collections.sort((List<Light>) l);
			
			while(it.hasNext())
			{
				it.next().update();			
			}
		}
		
		SpotLight.reset();
		PointLight.reset();
		
	}
	
	public boolean addLight(Light l)
	{
		if(l instanceof AmbientLight)
		{
			lights.add(l);
			
			return true;
		}
		
		if(l instanceof DirectionalLight)
		{
			lights.add(l);
			
			return true;
		}
		
		if(l instanceof PointLight)
		{
			pointLights.add(l);
			
			return true;
		}
		
		if(l instanceof SpotLight)
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
	
}
