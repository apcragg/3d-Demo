package engine.util;

import engine.main.Time;

public class Profile
	{
		private static long start;
		private static float average;
		private static int num;
		
		public static void startProfile()
		{		
			start = Time.getTime();
		}
		
		public static float stopProfile()
		{
			//System.out.printf("Profiled method took %f seconds to complete.\n", (((double) Time.getTime() - start) / Time.SECOND));			
			
			return (float) (((double) Time.getTime() - start) / Time.SECOND);
		}

		public static float getAverage()
		{
			float result = average / num;
			average = 0;
			num 	= 0;
			
				return result;
		}

		public static void setAverage(float average)
		{
				Profile.average += average;
				num++;
		}
	}
