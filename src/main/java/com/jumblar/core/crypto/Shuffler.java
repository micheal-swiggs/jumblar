package com.jumblar.core.crypto;

import java.util.Random;
import com.jumblar.core.utils.Arrays;



import static com.jumblar.core.generators.CharacterGenerator.*;

public class Shuffler {

	byte[] baseBytes;
	
	public Shuffler (byte[] bytes){
		this.baseBytes = bytes;
	}
	
	public String shuffleString (String str) {
		char[] cArr = str.toCharArray();
		byte[] byteSeed = concat (baseBytes, utf8Bytes (str));
		CustomRandom cRandom = new CustomRandom (byteSeed);
		shuffleArray (cArr, cRandom);
		return new StringBuilder ().append(cArr).toString();
	}
	
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
