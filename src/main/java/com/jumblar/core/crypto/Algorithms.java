package com.jumblar.core.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Algorithms {

	public static MessageDigest sha256Instance(){
		try {
			return MessageDigest.getInstance ("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException (e);
		}
	}
	
	public static byte[] generateSalt(int length){
		byte[] result = new byte[length];
		SecureRandom rand = new SecureRandom();
		rand.nextBytes(result);
		return result;
	}
}
