// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.domain;

import java.io.Serializable;

/**
 * Contains enough information to determine the correct
 * password and coordinates.
 * @author micheal
 *
 */
public class SinglePointReference implements Serializable{
	private static final long serialVersionUID = -2164493180542044058L;
	
	byte[] salt;
	byte[] vagueHash;
	String username;
	String email;
	String personalInfo;
	
	public SinglePointReference (byte[] salt, byte[] vHash,
			String username, String email, String personalInfo){
		this.salt = salt;
		this.vagueHash = vHash;
		this.username = username;
		this.email = email;
		this.personalInfo = personalInfo;
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
}
