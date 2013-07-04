package com.jumblar.core.domain;

import java.io.Serializable;

/**
 * Stores the result of hash derivation. Used for all subsequent
 * jumbles.
 * @author micheal
 *
 */
public class HashBase implements Serializable{
	byte[] base;
	
	public HashBase( byte[] base){
		this.base = base;
	}
	
	public byte[] getHashBase(){
		return this.base;
	}
	
	private static final long serialVersionUID = -3581639282921534022L;
	
}
