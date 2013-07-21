// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.domain;

import java.io.Serializable;

/**
 * Stores the result of hash derivation. Used for all subsequent
 * jumbles.
 * @author micheal
 * 
 * Do Not Serialize To Disk.
 *
 */
public class HashBase implements Serializable{
	byte[] base;
	int[][] actualLocations;
	int nGuesses; //Number of spiral attempts before finding the 
	//correct hashbase.
	
	public HashBase( byte[] base){
		this.base = base;
	}
	
	public byte[] getHashBase(){
		return this.base;
	}
	
	public void setActualLocations (int[][] val ){
		actualLocations = val;
	}
	
	public int[][] getActualLocations(){
		return actualLocations;
	}
	
	public int getNumGuesses(){
		return nGuesses;
	}
	
	public void setNumGuesses(int i){ nGuesses = i;}
	
	private static final long serialVersionUID = -3581639282921534022L;
	
}
