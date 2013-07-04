package com.jumblar.core.generators;

import java.security.MessageDigest;

import com.jumblar.core.crypto.Shuffler;


import static com.jumblar.core.crypto.Algorithms.*;
import static com.jumblar.core.generators.CharacterGenerator.*;

public class PhraseGenerator {

	MessageDigest digester;
	byte[] input;
	
	public PhraseGenerator (byte[] input){
		this.input = input;
		digester = sha256Instance();
	}	
	
	public String randString (int length){
		validateLength (length);
		String result = "";
		byte[] buf = input;
		digester.reset();
		digester.update (input);
		buf = digester.digest (buf);
		result += generateUpperCaseChar (buf);
		result += generateLowerCaseChar (buf);
		result += generateDigit (buf);
		while (result.length() < length){
			digester.reset();
			buf = digester.digest (buf);
			String b = utf8String(buf);
			result += filterAlphaNumeric (b);
		}
		Shuffler s = new Shuffler (buf);
		return s.shuffleString (result.substring(0, length));
	}
	
	public String randString (String prefix, int length){
		validateLength (length);
		byte[] buf1 = input;
		byte[] buf2 = utf8Bytes (prefix);
		byte[] buf = input;
		String result = "";
		digester.reset();
		digester.update(buf1);
		digester.update(buf2);
		buf = digester.digest(buf);
		result += generateUpperCaseChar (buf);
		result += generateLowerCaseChar (buf);
		result += generateDigit (buf);
		while (result.length() < length){
			digester.reset();
			digester.update(buf1);
			digester.update(buf2);
			buf = digester.digest (buf);
			String b = utf8String (buf);
			result += filterAlphaNumeric (b);
		}
		Shuffler s = new Shuffler (buf);
		return s.shuffleString (result.substring(0, length));
		
	}
	
	public void validateLength (int length){
		if (length < 3){ throw new RuntimeException ("Password length must be greater than 2");}
	}
}
