package com.jumblar.core.domain;

import java.io.Serializable;

public class SimpleJumble implements Serializable{
	private static final long serialVersionUID = -3195482909656417729L;
	HashBase hashbase;
	SinglePointReference spf;
	
	public SimpleJumble (HashBase hb, SinglePointReference spf){
		this.hashbase = hb;
		this.spf = spf;
	}
	
	public HashBase getHashBase(){
		return hashbase;
	}
	public SinglePointReference getSinglePointReference(){
		return spf;
	}
}
