// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.crypto;

import java.security.MessageDigest;

import static com.jumblar.core.crypto.Algorithms.*;
import static com.jumblar.core.generators.CharacterGenerator.*;

public class HashDerivation {

	public static final int NROUNDS = 10000;
	int x,y;
	String password;
	byte[] base;
	byte[] salt;
	MessageDigest md;
	
	public HashDerivation (int x, int y, String password, byte[] salt){
		this.x = x;
		this.y = y;
		this.password = password;
		md = sha256Instance();
		base = utf8Bytes ("" + x + " " + y + " " + password);
		this.salt = salt;
	}
	
	public byte[] hash(){
		byte[] buf = null;
		for (int i=0; i<NROUNDS; i++){
			md.reset();
			md.update(base);
			md.update(salt);
			if (buf != null){
				md.update(buf);
			}
			buf = md.digest();
		}
		return buf;
	}
}
