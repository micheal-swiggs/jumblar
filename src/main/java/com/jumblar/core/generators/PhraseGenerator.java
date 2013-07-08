// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.generators;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import com.jumblar.core.crypto.Shuffler;
import com.lambdaworks.crypto.SCrypt;


import static com.jumblar.core.crypto.Algorithms.*;
import static com.jumblar.core.generators.CharacterGenerator.*;

/**
 * @author Micheal Swiggs
 * 
 * Used for generating phrases which are functionally 
 * equivalent to 'passwords'. Important, as a captured
 * password could give information about the 'seed'
 * which is used to generate all passwords. Therefore
 * generated phrases should be the result of a 'hard' hashing
 * function. Currently SCrypt is used to hash the seed
 * and other information prior to phrase generation.
 *
 */
public class PhraseGenerator {

	static int N = 32768;
	static int r = 8;
	static int p = 1;
	static int keyLength = 64;
	static byte[] salt = new byte[1];
	
	final byte[] seed;
	
	/**
	 * @param seed - used in phrase generation.
	 */
	public PhraseGenerator (byte[] seed){
		this.seed = seed;
	}	
	
	/**
	 * 
	 * @param length - length of phrase (must be > 2)
	 * @return a phrase of specified length & containing atleast 
	 * one uppercase character, one lowercase character and one digit.
	 */
	public String randString (int length){
		validateLength (length);
		byte[] iput = new byte[seed.length + 4];
		iput[0] = (byte) (length>>24);
		iput[1] = (byte) (length>>16);
		iput[2] = (byte) (length>>8);
		iput[3] = (byte) (length);
		for (int i=0;i<seed.length;i++){ iput[i+4] = seed[i];}
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
	
	/**
	 * 
	 * @param spice - used to dramatically change output and 
	 * easy for users to remember. 
	 * @param length - length of phrase (must be > 2)
	 * @return a phrase of specified length & containing atleast 
	 * one uppercase character, one lowercase character and one digit.
	 */
	public String randString (String spice, int length){
		validateLength (length);
		byte[] prefixBytes = utf8Bytes(spice);
		byte[] buf1 = new byte[seed.length+prefixBytes.length+4];
		buf1[0] = (byte) (length>>24);
		buf1[1] = (byte) (length>>16);
		buf1[2] = (byte) (length>>8);
		buf1[3] = (byte) (length);
		int i=0; for (; i<seed.length; i++){ buf1[i+4] = seed[i];}
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
	
	protected void validateLength (int length){
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
