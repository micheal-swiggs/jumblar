package com.jumblar.core.controllers;

import com.jumblar.core.domain.HashBase;
import com.jumblar.core.generators.PhraseGenerator;

public class PhraseController {

	public String generatePhrase(HashBase hb, int length){
		PhraseGenerator pg = new PhraseGenerator(hb.getHashBase());
		return pg.randString(length);
	}

	public String generatePhrase (HashBase hb, String prefix, int length){
		PhraseGenerator pg = new PhraseGenerator (hb.getHashBase());
		return pg.randString(prefix, length);
	}
}
