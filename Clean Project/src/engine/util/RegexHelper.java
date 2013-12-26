package engine.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHelper 
{
	public static Matcher getRegex(String pattern, String input)
	{
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(input);
		m.find();
		
		return m;
	}
	
	public static boolean find(String pattern, String input)
	{
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(input);
		return m.find();
	}
}
