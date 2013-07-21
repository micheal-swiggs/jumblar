// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.domain;

import java.io.Serializable;

/**
 * @author Micheal Swiggs
 * Contains enough information to determine the correct
 * password and coordinates without needing to contact 
 * a PGP server.
 *
 */
public class PointsReference implements Serializable{
	private static final long serialVersionUID = -2144931680542044058L;
	
	byte[] salt;
	byte[] vagueHash;
	int N, r, p, keyLength;
	String username;
	String email;
	String personalInfo;
	int nPoints;	
	
	public PointsReference (byte[] salt, byte[] vHash,
			String username, String email, String personalInfo,
			int N, int r, int p, int keyLength, int nPoints){
		this.salt = salt;
		this.vagueHash = vHash;
		this.username = username;
		this.email = email;
		this.personalInfo = personalInfo;
		
		this.N = N;
		this.r = r;
		this.p = p;
		this.keyLength = keyLength;
	}
	
	public byte[] getSalt(){
		return salt;
	}
	public byte[] getVagueHash(){
		return vagueHash;
	}
	public String getUsername(){
		return username;
	}
	public String getEmail(){
		return email;
	}
	public String getPersonalInfo(){
		return personalInfo;
	}
	public int getN(){ return N;}
	public int getR(){return r;}
	public int getP(){return p;}
	public int getKeyLength(){return keyLength;}
	
}
