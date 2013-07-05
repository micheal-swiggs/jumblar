// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.generators;

import java.io.IOException;
import com.jumblar.core.utils.Arrays;

import com.jumblar.core.crypto.HashDerivation;
import com.jumblar.core.encodings.Base64;

public class VagueHashGenerator {

	public static final int NBYTES = 3;
	
	HashDerivation hd;
	byte[] hashArray;
	
	public VagueHashGenerator (int x, int y, String password, byte[] salt){
		hd = new HashDerivation (x,y, password, salt);
		hashArray = hd.hash();
	}
	
	public String shortenedHash(){
		return "#64#"+Base64.encodeBytes(Arrays.copyOfRange (hashArray, 0, NBYTES));
	}
	
	public static String base64VagueHash (int x, int y, String password, byte[] salt){
		return new VagueHashGenerator (x, y, password, salt).shortenedHash();
	}
	
	public static byte[] base64VagueHashDecode (String vHash) throws IOException{
		if (vHash.startsWith("#64#")){
			return Base64.decode (vHash.substring(4));
		}
		return null;
	}
	
}
