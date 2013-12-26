package engine.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogHelper
{
	private static StringBuilder infoLog = new StringBuilder();

	
	public static void printInfo(String s)
	{
		infoLog.append("[STDOUT] " + s + '\n');		
		System.out.printf("[STDOUT] %s\n", s);
	}
	
	public static void printError(String s)
	{
		infoLog.append("[STDERR] " + s + '\n');		
		System.out.printf("[STDERR] %s\n", s);
	}
	
	public static String dumpInfoLog(boolean console)
	{
		BufferedWriter writer = null;
		
		File f = new File(System.getProperty("user.dir") + "/log/LOG_" + DateHelper.getFormatedDate() + ".txt");
		try 
		{
			writer = new BufferedWriter(new FileWriter(f));
			writer.write(infoLog.toString());
			
		} 
		catch (IOException e) 
		{
			System.out.println("FAILED TO WRITE TO LOG FILE");
			e.printStackTrace();
		}
		finally
		{
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(console)
			return infoLog.toString();
		else
			return "";
	}
}
