// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.crypto;

import java.security.MessageDigest;
import java.util.Random;

import static com.jumblar.core.crypto.Algorithms.*;

public class CustomRandom extends Random{
	private static final long serialVersionUID = -6055461167136178306L;
	final byte[] baseBytes;
	byte[] byteBuffer;
	MessageDigest digester;
	
	public CustomRandom (byte[] baseBytes) {
		this.baseBytes = baseBytes.clone();
		this.byteBuffer = baseBytes.clone();
		digester = sha256Instance();
	}
	
	public int next(int nbits){
		long value = 0;
		digester.reset();
		digester.update (byteBuffer);
		byteBuffer = digester.digest (baseBytes);
		for (int i = 0; i < byteBuffer.length; i++)
		{
		   value += ((long) byteBuffer[i] & 0xffL) << (8 * i);
		}
		return (int) (value &= ((1L << nbits) -1));
	}
	
	/* Inclusive [start, end]*/
	public int nextInt(int start, int end){
		return nextInt(end-start+1) + start;
	}
}
