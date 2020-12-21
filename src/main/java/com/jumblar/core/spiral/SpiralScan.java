// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.spiral;

import java.security.GeneralSecurityException;

import com.jumblar.core.crypto.SCryptDerivation;
import com.jumblar.core.domain.ScryptParams;
import com.jumblar.core.utils.Arrays;


public class SpiralScan {

	private static final int[] NONE1 = new int[]{0};
	protected static final int[][] NONE2 = new int[][]{{0,0}};
	
	public static boolean isNone(int[] i){
		return i == NONE1;
	}
	
	public static boolean isNone(int[][] i){
		return i== NONE2;
	}
	
	int xGuess, yGuess;
	String password;
	byte[] salt;
	byte[] vagueHash;

	final ScryptParams scryptParams;
	SingleSpiral ss;
	
	int actualRounds;
	
	public SpiralScan (int xGuess, int yGuess, String password, byte[] verifyingHash, byte[] salt,
			ScryptParams scryptParams){
		this.xGuess = xGuess;
		this.yGuess = yGuess;
		this.password = password;
		this.vagueHash = verifyingHash;
		ss = new SingleSpiral (xGuess, yGuess);
		this.salt = salt;
		this.scryptParams = scryptParams;
	}
	
	public int[] attemptMatch (int nRounds){
		SCryptDerivation hd = new SCryptDerivation (0, 0, password, salt, scryptParams);
		byte[] guessHash;
		actualRounds=-1;
		try{
			for (int i=0; i<nRounds; i++){
				int[] nextPoint = ss.nextItem();
				guessHash = hd.hash(nextPoint[0], nextPoint[1]);
				guessHash = Arrays.copyOfRange(guessHash, 0, vagueHash.length);
				if (Arrays.equals(vagueHash, guessHash)){
					actualRounds = i;
					return nextPoint;
				}
			}
		} catch (GeneralSecurityException e){
			e.printStackTrace();
			throw new RuntimeException (e);
		}
		return NONE1;
	}
	
	public int getActualRounds(){
		return actualRounds;
	}
}
