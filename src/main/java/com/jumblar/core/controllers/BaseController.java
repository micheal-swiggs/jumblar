// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.controllers;

import static com.jumblar.core.crypto.Algorithms.generateSalt;
import static com.jumblar.core.generators.VagueHashGenerator.base64VagueHash;
import static com.jumblar.core.generators.VagueHashGenerator.base64VagueHashDecode;
import static com.jumblar.core.utils.Regex.regexFindFirst;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jumblar.core.crypto.HashDerivation;
import com.jumblar.core.domain.HashBase;
import com.jumblar.core.domain.SimpleJumble;
import com.jumblar.core.domain.SinglePointReference;
import com.jumblar.core.encodings.Base64;
import com.jumblar.core.network.PGPKeyRecord;
import com.jumblar.core.spiral.SpiralScan;

public class BaseController {

	public SimpleJumble createNewPGPEntry(String username, String email, String personalInfo, String password, String coordinate){
		int xCoord,yCoord;
		String[] coords = coordinate.split(",");
		xCoord = (int) (new Double(coords[0]) * 1000000);
		yCoord = (int) (new Double(coords[1])* 1000000);
		byte[] salt = generateSalt(64);
		String vagueHash = base64VagueHash (xCoord, yCoord, password, salt);
		String comment = "[VagueHash]"+vagueHash+"[/VagueHash]";
		comment += "[CreationTime]"+System.currentTimeMillis()+"[/CreationTime]";
		comment += "[Salt]"+Base64.encodeBytes(salt)+"[/Salt]";
		PGPKeyRecord gpgRecord = new PGPKeyRecord();
		boolean result = false;
		try{
			result = gpgRecord.uploadPGPRecord(username, email, personalInfo, comment);
		} catch (IOException e){
			throw new RuntimeException (e);
		}
		if(result){
			SinglePointReference spf;
			try {
				spf = new SinglePointReference(
						salt, base64VagueHashDecode(vagueHash), username, email, personalInfo);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException (e);
			}
			HashBase hb = new HashBase (new HashDerivation (xCoord, yCoord, password, salt).hash());
			return new SimpleJumble (hb, spf);
		}
		return null;
	}

	public HashBase computeHashBase (SinglePointReference spf, String password, String coordinate){
		String[] coords = coordinate.split(",");
		int xCoord, yCoord;
		xCoord = (int) (new Double (coords[0]) * 1000000);
		yCoord = (int) (new Double (coords[1]) * 1000000);
		SpiralScan ss = new SpiralScan (xCoord, yCoord, password, spf.getVagueHash(),spf.getSalt());
		int[] actualCoordinates = ss.attemptMatch(2000);
		if (actualCoordinates == null) return null;
		return new HashBase (new HashDerivation (actualCoordinates[0], actualCoordinates[1], password, spf.getSalt()).hash());
	}

	public SimpleJumble computeHashBase (String username, String email, String personalInfo, String password, String guessCoordinate) throws IOException{
		String[] oldestEntry = urlGetOldestPGPEntry (username, email, personalInfo);
		byte[] vHash = base64VagueHashDecode(oldestEntry[0]);
		byte[] salt = Base64.decode(oldestEntry[2]);
		String[] coords = guessCoordinate.split(",");
		int xCoord, yCoord;
		xCoord = (int) (new Double (coords[0]) * 1000000);
		yCoord = (int) (new Double (coords[1]) * 1000000);
		SpiralScan ss = new SpiralScan (xCoord, yCoord, password, vHash, salt);
		int[] actualCoordinates = ss.attemptMatch(2000);
		if (actualCoordinates == null) return null;
		SinglePointReference spf = new SinglePointReference(
				salt, vHash, username, email, personalInfo);
		HashBase hb = new HashBase (new HashDerivation (actualCoordinates[0], actualCoordinates[1], password, salt).hash());
		return new SimpleJumble (hb, spf);
	}

	public String[] urlGetOldestPGPEntry (String username, String email, String personalInfo){
		PGPKeyRecord pgpRecord = new PGPKeyRecord();
		List<String> comments = pgpRecord.getPGPComments(username, email, personalInfo) ;
		List<String[]> results = new ArrayList<String[]>();
		for (String comment: comments){
			String vHash = regexFindFirst(comment, "\\[VagueHash\\].*\\[/VagueHash\\]");
			if (vHash == null) continue;
			vHash = vHash.split("\\[/?VagueHash\\]")[1];
			String creationTime = regexFindFirst (comment, "\\[CreationTime\\].*\\[/CreationTime\\]");
			if (creationTime == null) continue;
			creationTime = creationTime.split("\\[/?CreationTime\\]")[1];
			String salt = regexFindFirst (comment, "\\[Salt\\].*\\[/Salt\\]");
			if (salt == null) continue;
			salt = salt.split("\\[/?Salt\\]")[1];
			results.add(new String[]{vHash, creationTime, salt});
		}

		//Find the oldest entry
		String[] oldestEntry = null;
		for (String[] i: results){
			if (oldestEntry == null ||
					Long.parseLong(i[1]) < Long.parseLong(oldestEntry[1])){
				oldestEntry = i;
			}
		}
		return oldestEntry;
	}

}
