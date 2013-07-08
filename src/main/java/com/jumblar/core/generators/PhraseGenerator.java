// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.generators;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import com.jumblar.core.crypto.Shuffler;
import com.lambdaworks.crypto.SCrypt;


import static com.jumblar.core.crypto.Algorithms.*;
import static com.jumblar.core.generators.CharacterGenerator.*;

public class PhraseGenerator {

	static int N = 32768;
	static int r = 8;
	static int p = 1;
	static int keyLength = 64;
	static byte[] salt = new byte[1];
	
	final byte[] input;
	
	public PhraseGenerator (byte[] input){
		this.input = input;
	}	
	
	public String randString (int length){
		validateLength (length);
		byte[] iput = new byte[input.length + 4];
		iput[0] = (byte) (length>>24);
		iput[1] = (byte) (length>>16);
		iput[2] = (byte) (length>>8);
		iput[3] = (byte) (length);
		for (int i=0;i<input.length;i++){ iput[i+4] = input[i];}
		final byte[] scryptHash = doSCryptHash(iput, salt, N, r, p, keyLength);
		String result = "";
		MessageDigest digester = sha256Instance();
		digester.reset();
		byte[] buf = digester.digest (scryptHash);
		result += generateUpperCaseChar (buf);
		result += generateLowerCaseChar (buf);
		result += generateDigit (buf);
		while (result.length() < length){
			digester.reset();
			digester.update (buf);
			buf = digester.digest (scryptHash);
			String b = utf8String(scryptHash);
			result += filterAlphaNumeric (b);
		}
		Shuffler s = new Shuffler (scryptHash);
		return s.shuffleString (result.substring(0, length));
	}
	
	public String randString (String prefix, int length){
		validateLength (length);
		byte[] prefixBytes = utf8Bytes(prefix);
		byte[] buf1 = new byte[input.length+prefixBytes.length+4];
		buf1[0] = (byte) (length>>24);
		buf1[1] = (byte) (length>>16);
		buf1[2] = (byte) (length>>8);
		buf1[3] = (byte) (length);
		int i=0; for (; i<input.length; i++){ buf1[i+4] = input[i];}
		for (int j=0; j<prefixBytes.length; j++){ buf1[j+i+4] = prefixBytes[j];}
		
		final byte[] scryptHash = doSCryptHash(buf1, salt, N, r, p, keyLength);
		String result = "";
		MessageDigest digester = sha256Instance();
		digester.reset();
		byte[] buf = digester.digest(scryptHash);
		result += generateUpperCaseChar (buf);
		result += generateLowerCaseChar (buf);
		result += generateDigit (buf);
		while (result.length() < length){
			digester.reset();
			digester.update(buf);
			buf = digester.digest (scryptHash);
			String b = utf8String (scryptHash);
			result += filterAlphaNumeric (b);
		}
		Shuffler s = new Shuffler (scryptHash);
		return s.shuffleString (result.substring(0, length));
		
	}
	
	public void validateLength (int length){
		if (length < 3){ throw new RuntimeException ("Password length must be greater than 2");}
	}
	
	protected byte[] doSCryptHash (byte[] password, byte[] salt, int N, int r, int p, int keyLength){
		try {
			return SCrypt.scrypt(password, salt, N, r, p, keyLength);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			throw new RuntimeException (e);
		}
	}
}
