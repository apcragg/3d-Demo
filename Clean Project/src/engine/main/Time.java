package engine.main;

public class Time
{
	private static float delta = 0;
	public static final long SECOND = 1000000000L;

	public static long getTime()
	{
		return System.nanoTime();
	}

	public static double getDelta()
	{
		return delta;
	}

	public static void setDelta(float delta)
	{
		Time.delta = delta;
	}
}
