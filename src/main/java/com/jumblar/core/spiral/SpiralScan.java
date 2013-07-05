// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.spiral;

import com.jumblar.core.utils.Arrays;

import com.jumblar.core.crypto.HashDerivation;


public class SpiralScan {

	int xGuess, yGuess;
	String password;
	byte[] salt;
	byte[] vagueHash;
	
	SingleSpiral ss;
	
	public SpiralScan (int xGuess, int yGuess, String password, byte[] verifyingHash, byte[] salt){
		this.xGuess = xGuess;
		this.yGuess = yGuess;
		this.password = password;
		this.vagueHash = verifyingHash;
		ss = new SingleSpiral (xGuess, yGuess);
		this.salt = salt;
	}
	
	public static void println (Object o){
		System.out.println (""+o);
	}
	
	public int[] attemptMatch (int nRounds){
		for (int i=0; i<nRounds; i++){
			int[] nextPoint = ss.nextItem();
			//println (nextPoint[0]+" "+nextPoint[1]);
			HashDerivation hd = new HashDerivation (nextPoint[0], nextPoint[1], password, salt);
			byte[] guessHash = hd.hash();
			guessHash = Arrays.copyOfRange(guessHash, 0, vagueHash.length);
			//assert guessHash.length == vagueHash.length;
			if (Arrays.equals(vagueHash, guessHash)){
				return nextPoint;
			}
		}
		return null;
	}
}
