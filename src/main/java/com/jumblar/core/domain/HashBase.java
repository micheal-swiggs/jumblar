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
	final byte[] base;

	public HashBase( byte[] base){
		this.base = base;
	}
	
	public byte[] getBytes(){
		return this.base;
	}
	
	private static final long serialVersionUID = -3581639282921534022L;
	
}
