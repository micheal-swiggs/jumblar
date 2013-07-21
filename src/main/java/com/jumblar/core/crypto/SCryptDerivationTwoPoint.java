// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.crypto;

import java.security.GeneralSecurityException;

import com.lambdaworks.crypto.SCrypt;
import static com.jumblar.core.generators.CharacterGenerator.*;

/**
 * 
 * @author Micheal Swiggs
 *
 */
public class SCryptDerivationTwoPoint {
	byte[] salt;
	byte[] base;
	int N, r, p;
	int keyLength;
	
	public SCryptDerivationTwoPoint (int[] point1, int[] point2,
			String password, byte[] salt, int N, int r, int p,
			int keyLength){
		byte[] pwordBytes = utf8Bytes (password);
		base = new byte[16+pwordBytes.length];
		placePoints(point1, point2);
		for (int i=0; i<pwordBytes.length; i++){
			base[i+16] = pwordBytes[i];
		}
		this.salt = salt;
		this.N = N;
		this.r = r;
		this.p = p;
		this.keyLength = keyLength;
	}
	
	public byte[] hash() throws GeneralSecurityException{
		return SCrypt.scrypt(base, salt, N, r, p, keyLength);
	}
	
	public byte[] hash(int[] pt1, int[] pt2) throws GeneralSecurityException{
		placePoints(pt1, pt2);
		return hash();
	}
	
	protected void placePoints(int[] pt1, int[] pt2){
		base[0] = (byte) (pt1[0]>>24);
		base[1] = (byte) (pt1[0]>>16);
		base[2] = (byte) (pt1[0]>>8);
		base[3] = (byte) (pt1[0]);
		base[4] = (byte) (pt1[1]>>24);
		base[5] = (byte) (pt1[1]>>16);
		base[6] = (byte) (pt1[1]>>12);
		base[7] = (byte) (pt1[1]);
		
		base[8] = (byte) (pt2[0]>>24);
		base[9] = (byte) (pt2[0]>>16);
		base[10]= (byte) (pt2[0]>>8);
		base[11]= (byte) (pt2[0]);
		base[12]= (byte) (pt2[1]>>24);
		base[13]= (byte) (pt2[1]>>16);
		base[14]= (byte) (pt2[1]>>8);
		base[15]= (byte) (pt2[1]);
		
	}
}
