// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.generators;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.jumblar.core.crypto.SCryptDerivation;
import com.jumblar.core.encodings.Base64;
import com.jumblar.core.utils.Arrays;

/**
 * @author Micheal Swiggs
 *
 * Basis of generating vague hashes. Hashing done via
 * SCrypt.
 * 
 */
public class VagueHashGenerator {
	
	/** Number of bytes used of the resulting hash for
	 * the vague-hash.
	 */
	public static final int NBYTES = 2;
	
	SCryptDerivation hd;
	byte[] hashArray;
	
	public VagueHashGenerator (int x, int y, String password, byte[] salt,
			int N, int r, int p, int keyLength){
		hd = new SCryptDerivation (x,y, password, salt, N, r, p, keyLength);
		try {
			hashArray = hd.hash();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			throw new RuntimeException (e);
		}
	}
	
	public String shortenedHash(){
		return "#64#"+Base64.encodeBytes(Arrays.copyOfRange (hashArray, 0, NBYTES));
	}
	
	public static String base64VagueHash (int x, int y, String password, byte[] salt,
			int N, int r, int p, int keyLength){
		return new VagueHashGenerator (x, y, password, salt, N, r, p, keyLength).shortenedHash();
	}
	
	public static byte[] base64VagueHashDecode (String vHash) throws IOException{
		if (vHash.startsWith("#64#")){
			return Base64.decode (vHash.substring(4));
		}
		return null;
	}
	
}
