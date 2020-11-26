// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.controllers;

import com.jumblar.core.domain.HashBase;
import com.jumblar.core.generators.PhraseGenerator;

public class PhraseController {

	public static String generatePhrase(HashBase hb, int length){
		PhraseGenerator pg = new PhraseGenerator(hb.getBytes());
		return pg.randString(length);
	}

	public static String generatePhrase (HashBase hb, String prefix, int length){
		PhraseGenerator pg = new PhraseGenerator (hb.getBytes());
		return pg.randString(prefix, length);
	}
}
