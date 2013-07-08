// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.generators;

import java.io.UnsupportedEncodingException;

import com.jumblar.core.crypto.CustomRandom;

/**
 * 
 * @author Micheal Swiggs
 * 
 * Provides utility alpha-numeric functions.
 *
 */
public class CharacterGenerator {

	public static final int A = (int)'A';
	public static final int Z = (int)'Z';
	public static final int a = (int)'a';
	public static final int z = (int)'z';
	public static final int zero = (int)'0';
	public static final int nine = (int)'9';
	
	public static char generateUpperCaseChar(byte[] bytes){
		CustomRandom s = new CustomRandom (bytes);
		return (char)s.nextInt(A, Z);
	}
	
	public static char generateLowerCaseChar (byte[] bytes){
		CustomRandom s = new CustomRandom (bytes);
		return (char)s.nextInt(a, z);
	}
	
	public static char generateDigit (byte[] bytes){
		CustomRandom s = new CustomRandom (bytes);
		return (char)s.nextInt (zero, nine);
	}
	
	public static boolean isAlphaNumeric (char c){
		return (c >= A && c<=Z) || (c >= a && c <= z) || ( c >= zero && c <= nine);
	}
	
	public static String filterAlphaNumeric (String s){
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<s.length(); i++){
			char b = s.charAt(i);
			if (isAlphaNumeric (b)){
				sb.append(b);
			}			
		}
		return sb.toString();		
	}
	
	public static byte[] utf8Bytes (String str){
		try {
			return str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException (e);
		}
	}
	
	public static String utf8String (byte[] bytes){
		try {
			return new String (bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException (e);
		}
	}
}
