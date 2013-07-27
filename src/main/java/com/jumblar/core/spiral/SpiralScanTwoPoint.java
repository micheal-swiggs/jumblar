// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.spiral;

import java.security.GeneralSecurityException;
import java.util.Arrays;

import com.jumblar.core.crypto.SCryptDerivationTwoPoint;

public class SpiralScanTwoPoint {

	int[] guess1;
	int[] guess2;
	String password;
	byte[] salt;
	byte[] vagueHash;
	
	int N, r, p, keyLength;
	DoubleSpiral ds;
	
	int actualRounds;
	
	public SpiralScanTwoPoint (int[] guess1, int[] guess2, String password,
			byte[] verifyingHash, byte[] salt, int N, int r, int p, int keyLength){
		this.guess1 = guess1;
		this.guess2 = guess2;
		this.password = password;
		this.vagueHash = verifyingHash;
		ds = new DoubleSpiral (guess1, guess2);
		this.salt = salt;
		this.N = N;
		this.r = r;
		this.p = p;
		this.keyLength = keyLength;
	}
	
	public int[][] attemptMatch (int nRounds){
		SCryptDerivationTwoPoint hd = new SCryptDerivationTwoPoint(
			new int[]{0,0}, new int[]{0,0}, password, salt,
			N, r, p, keyLength);
		byte[] guessHash;
		actualRounds = -1;
		try{
			for (int i=0; i<nRounds; i++){
				int[][] nextPoint = ds.nextItem();
				guessHash = hd.hash(nextPoint[0], nextPoint[1]);
				guessHash = Arrays.copyOfRange(guessHash, 0, vagueHash.length);
				if (Arrays.equals (vagueHash, guessHash)){
					actualRounds = i;
					return nextPoint;
				}
			}
		} catch (GeneralSecurityException e){
			e.printStackTrace();
			throw new RuntimeException (e);
		}
		return SpiralScan.NONE2;
	}
	
	public int getActualRounds(){
		return actualRounds;
	}
}
