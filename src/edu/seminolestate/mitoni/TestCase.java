package edu.seminolestate.mitoni;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestCase
{
	static String testString = "561-386-5974";
	static int phone;
	static boolean valid;
	static String intString;

	public static void main(String[] args)
	{
		if (testString != null)
		{
			Pattern p = Pattern.compile("(\\d{3})-?(\\d{3})-?(\\d{4})");
			Matcher m = p.matcher(testString);
			
			if (m.matches())
			{
				intString = m.group(1) + m.group(2) + m.group(3);
				System.out.println(m.groupCount());
				System.out.println(intString);
			}
			else 
			{
				System.out.println("no match");
			}

		}

	}

}
