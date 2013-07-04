package com.jumblar.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

	public static String regexFindFirst(String text, String pattern){
		Matcher matcher = Pattern.compile (pattern).matcher(text);
		String result = null;
		if (matcher.find()) result = matcher.group();
		return result;
	}
}
