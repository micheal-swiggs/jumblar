package com.jumblar.core;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.lambdaworks.crypto.SCrypt;
import com.lambdaworks.crypto.SCryptUtil;

public class CryptoTest 
	extends TestCase{

	public CryptoTest(String testName){
		super(testName);
	}
	
	public static Test suite(){
		return new TestSuite (CryptoTest.class);
	}
	
	public void testScryptUtil() throws Exception{    	
    	println (SCryptUtil.scrypt("password", 128, 8, 1));
    }
	
	public void testScrypt() throws Exception{
		byte[] password = "password".getBytes("UTF-8");
		byte[] salt = "salt".getBytes();
		byte[] dKey = SCrypt.scrypt(password, salt, 128,8,1,64);
		assertNotNull (dKey);
		println (dKey.length);
	}
    
    public static void println(Object ob){
    	System.out.println(""+ob);
    }
}
