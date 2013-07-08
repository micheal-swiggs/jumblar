// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.utils;

/**
 * Some functions that are not available in later versions
 * of android.
 *
 */
public class Arrays {

	
	public static byte[] copyOfRange (byte[] original, int start, int end){
		byte[] result = new byte[end-start];
		int cnt = 0;
		for (int i=start; i<end;i++){
			result[cnt] = original[i];
			cnt++;
		}
		return result;
	}
	
	public static byte[] copyOf (byte[] arr, int l){
		byte[] result = new byte[l];
		for (int i=0; i<arr.length; i++){
			result[i] = arr[i];
		}
		return result;
	}
	
	public static boolean equals (byte[] arr1, byte[] arr2){
		return java.util.Arrays.equals(arr1, arr2);
	}
	
}
