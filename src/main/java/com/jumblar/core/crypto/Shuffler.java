// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.crypto;

import java.util.Random;
import com.jumblar.core.utils.Arrays;



import static com.jumblar.core.generators.CharacterGenerator.*;

/**
 * 
 * @author Micheal Swiggs
 * 
 * Utility class for shuffling strings.
 */
public class Shuffler {

	byte[] baseBytes;
	
	/**
	 * @param bytes - seed.
	 */
	public Shuffler (byte[] bytes){
		this.baseBytes = bytes;
	}
	/**
	 * 
	 * @param String to be shuffled. The shuffle is a function
	 * of both the seed and the string to be shuffled. So for
	 * the same seed the String 'abc' will always have the same
	 * result. However for two strings 'ABCD' and 'abcd' they
	 * will most likely have their characters shuffled into
	 * different positions, e.g 'BADC' and 'dcba'. 
	 * @return shuffled string.
	 */
	public String shuffleString (String str) {
		char[] cArr = str.toCharArray();
		byte[] byteSeed = concat (baseBytes, utf8Bytes (str));
		CustomRandom cRandom = new CustomRandom (byteSeed);
		shuffleArray (cArr, cRandom);
		return new StringBuilder ().append(cArr).toString();
	}
	
	/**
	 * Inplace shuffle.
	 * 
	 * @param arr - array to be shuffled.
	 * @param rnd - Random used to determine shuffled order.
	 */
	public static void shuffleArray(char[] arr, Random rnd){
		int lastIndex = arr.length - 1;
		for (int i=0; i<=lastIndex; i++){
			int k = rnd.nextInt(lastIndex-i+1) + i;
			char a = arr[i];
			char b = arr[k];
			arr[i] = b;
			arr[k] = a;
		}
	}
	public static byte[] concat(byte[] first, byte[] second) {
		byte[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
}
