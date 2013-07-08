package com.jumblar.core;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.jumblar.core.generators.PhraseGenerator;

public class PhraseGeneratorTest extends TestCase{

	public PhraseGeneratorTest (String testName){
		super (testName);
	}
	public static Test suite(){
		return new TestSuite (PhraseGeneratorTest.class);
	}
	
	public void testDifferentPhrases() throws Exception{
		byte[] baseBytes = new byte[32];
		PhraseGenerator pg = new PhraseGenerator (baseBytes);
		List<String> pwords = new ArrayList<String>();
		String pw = pg.randString(4);
		pwords.add(pw);
		pw = pg.randString(5);
		assertFalse(pwords.contains(pw));
		pwords.add(pw);
		pw = pg.randString("a",5);
		assertFalse(pwords.contains(pw));
		pwords.add(pw);
		pw = pg.randString("b",5);
		assertFalse(pwords.contains(pw));
		pwords.add(pw);
		pw = pg.randString("c",5);
		assertFalse(pwords.contains(pw));
		pwords.add(pw);
	}
}
