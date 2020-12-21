package com.jumblar.core;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.jumblar.core.controllers.BaseController;
import com.jumblar.core.domain.SimpleContainer;
import com.jumblar.core.encodings.Base64;

public class TwoPointTest extends TestCase{
	public TwoPointTest (String testName){ super (testName);}

	public static Test suite(){ return new TestSuite (TwoPointTest.class);}

	public void testBaseController() throws Exception {
		String username = "testing-user1";
		String email = "testing-user1@special.domain";
		String personalInfo = "";
		String password = "password";
		String coord1 = "48.858404,2.293569"; //Corner of eiffel tower;
		String coord2 = "-24.645764,129.596896"; //Point to the north-east of Uluru;
		int N = 8;
		int r = 8;
		int p = 1;
		int keyLength = 64;

		/** User registration */
		BaseController bc = new BaseController();
		/*
		SimpleJumble simpleJumble = bc.createNewPGPEntry(username, email, personalInfo, password, coord1, coord2, N, r, p, keyLength);
		assertNotNull (simpleJumble);

		HashBase hb = bc.computeHashBase (simpleJumble.getPointsReference(),
				password, coord1, coord2);
		assertTrue (Arrays.equals(simpleJumble.getHashBase().getHashBase(),
				hb.getHashBase()));
		*/
		String g1 = "48.858405,2.293577";
		String g2 = "-24.645764,129.596952";

		SimpleContainer sj = bc.computeHashBase(username, email, personalInfo, password, g1, g2);
		String guessHashBase = (Base64.encodeBytes(sj.getHashBase().getBytes()));
		System.out.println (guessHashBase);
	}
}
