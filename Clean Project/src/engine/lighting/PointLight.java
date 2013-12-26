package engine.lighting;

import engine.main.Game;
import engine.math.Vector3f;
import engine.renderer.Camera;
import engine.renderer.PhongShader;

public class PointLight implements Light
{
	public static int updateCounter = 0;
	
	private Vector3f pos;
	private Vector3f coefficients;
	private Vector3f color;
	private float intensity;
	
	public PointLight(Vector3f pos)
	{
		this(pos, new Vector3f(1f, .12f, 0.15f), new Vector3f(1f, 1f, 1f), 2f);
	}
	
	/**
	 * Creates a new point light
	 * @param pos Position
	 * @param coefficient Attenuation formula coefficients
	 * @param color Light color
	 * @param intensity Light intensity
	 */
	
	public PointLight(Vector3f pos, Vector3f coefficient, Vector3f color, float intensity)
	{
		this.pos = pos;
		this.coefficients = coefficient;
		this.intensity = intensity;
		this.color = color;
	}
	
	public void update()
	{	
		if(updateCounter < PhongShader.MAX_POINT_LIGHTS)
		{	
			Game.shader.uniformData1f("pointLights[" + updateCounter + "].base.intensity", intensity);
			Game.shader.uniformData3f("pointLights[" + updateCounter + "].base.color", color);	
			Game.shader.uniformData3f("pointLights[" + updateCounter + "].coefficient", coefficients);	
			Game.shader.uniformData3f("pointLights[" + updateCounter + "].pos", pos);	
			
			updateCounter++;
		}
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public Vector3f getCoefficients() {
		return coefficients;
	}

	public void setCoefficients(Vector3f coefficients) {
		this.coefficients = coefficients;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public int compareTo(Light l) 
	{
		if(Math.abs(Camera.getPos().subtract(pos).length()) > Math.abs(Camera.getPos().subtract(((PointLight) l).getPos()).length()))
		{
			return 1;
		}
		
		if(Math.abs(Camera.getPos().subtract(pos).length()) < Math.abs(Camera.getPos().subtract(((PointLight) l).getPos()).length()))
		{
			return -1;
		}
		
		if(Math.abs(Camera.getPos().subtract(pos).length()) == Math.abs(Camera.getPos().subtract(((PointLight) l).getPos()).length()))
		{
			return 0;
		}
		
		return 0;
	}
	
	public static void reset()
	{
		PointLight.updateCounter = 0;
	}

}
