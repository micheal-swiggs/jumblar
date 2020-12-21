// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.spiral;

import java.security.GeneralSecurityException;
import java.util.Arrays;

import com.jumblar.core.crypto.SCryptDerivationTwoPoint;
import com.jumblar.core.domain.HashBaseTwoPoint;
import com.jumblar.core.domain.ScryptParams;

public class SpiralScanTwoPoint {

	String password;
	byte[] salt;
	byte[] vagueHash;

	final ScryptParams scryptParams;
	DoubleSpiral doubleSpiral;
	
	int actualRounds;
	
	public SpiralScanTwoPoint (int[] guess1, int[] guess2, String password,
							   byte[] verifyingHash, byte[] salt, ScryptParams scryptParams){
		this.password = password;
		this.vagueHash = verifyingHash;
		this.doubleSpiral = new DoubleSpiral (guess1, guess2);
		this.salt = salt;
		this.scryptParams = scryptParams;
	}
	
	public int[][] attemptMatch (int nRounds){
		SCryptDerivationTwoPoint hd = new SCryptDerivationTwoPoint(
				password, salt,
				scryptParams);
		byte[] guessHash;
		actualRounds = -1;
		for (int i=0; i<nRounds; i++){
			int[][] nextPoint = doubleSpiral.nextItem();
			HashBaseTwoPoint hashBase = hd.hash(nextPoint[0], nextPoint[1]);
			guessHash = hashBase.vagueHash();
			if (Arrays.equals (vagueHash, guessHash)){
				actualRounds = i;
				System.out.println("Num rounds: " + actualRounds);
				return nextPoint;
			}
		}
		return null;
	}
	
	public int getActualRounds(){
		return actualRounds;
	}
}
