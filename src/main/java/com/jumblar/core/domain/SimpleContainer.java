// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.domain;

import java.io.Serializable;

/**
 * @author Micheal Swiggs
 *
 * Do Not Serialize To Disk.
 *
 * Parent container useful for serialization within android. Do
 * not use this to serialize to disk. Only PointsReference should
 * be serialized to disk.
 */
public class SimpleContainer implements Serializable{
	private static final long serialVersionUID = -3195482909656417729L;
	private final HashBase hashbase;
	private final PointsReference spf;
	
	public SimpleContainer(HashBase hb, PointsReference spf){
		this.hashbase = hb;
		this.spf = spf;
	}
	
	public HashBase getHashBase(){
		return hashbase;
	}
	public PointsReference getPointsReference(){
		return spf;
	}
	
}
