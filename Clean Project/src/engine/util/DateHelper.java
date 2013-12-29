package engine.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateHelper
{
	private static String[] months = { "OFFBYONE", "January", "February",
			"March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December" };

	public static String getFormatedDate()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date date = new Date();

		String dateString = dateFormat.format(date);

		Pattern pattern = Pattern.compile("\\d+_(\\d++)");
		Matcher match = pattern.matcher(dateString);

		if (match.find())
		{
			int month = Integer.parseInt(match.group(1));
			dateString = match.replaceFirst("_" + months[month]);

			return dateString;
		}
		else
		{
			return dateString;
		}
	}
}
