// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.generators;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.jumblar.core.crypto.SCryptDerivationTwoPoint;
import com.jumblar.core.encodings.Base64;
import com.jumblar.core.utils.Arrays;

/**
 * 
 * @author Micheal Swiggs
 *
 */
public class VagueHashGeneratorTwoPoint {

	public static final int NBYTES = 3;
	
	private final byte[] hashArray;
	
	private VagueHashGeneratorTwoPoint(int[] pt1, int[] pt2, String password, byte[] salt,
			int N, int r, int p, int keyLength){
		SCryptDerivationTwoPoint hd = new SCryptDerivationTwoPoint(pt1, pt2, password, salt, N, r, p, keyLength);
		try{
			hashArray = hd.hash();
		} catch (GeneralSecurityException e){
			throw new RuntimeException(e);
		}
	}
	
	private String shortenedHash(){
		return "#64#"+Base64.encodeBytes(Arrays.copyOfRange(hashArray, 0, NBYTES));
	}
	
	public static String base64VagueHash (int[] pt1, int[] pt2, String password, byte[] salt,
			int N, int r, int p, int keyLength	){
		return new VagueHashGeneratorTwoPoint (pt1, pt2, password, salt, N, r, p, keyLength).shortenedHash();
	}
	
}
