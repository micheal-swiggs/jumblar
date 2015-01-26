// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.network;

import static com.jumblar.core.encodings.Base64.decodePGPComment;
import static com.jumblar.core.encodings.Base64.encodePGPComment;
import static com.jumblar.core.generators.CharacterGenerator.utf8Bytes;
import static com.jumblar.core.generators.CharacterGenerator.utf8String;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jumblar.core.crypto.WeakSymmetricEncryption;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.spongycastle.crypto.CryptoException;

public class PGPKeyRecord {

	public static String uploadURL = "http://94.242.219.198:7080/";
	public static String mitpgp = "http://pgp.mit.edu:11371/";

	public static String mitSearch (String query){
		return mitpgp + "pks/lookup?search=" + query + "&op=index";
	}

	public List<String[]> getMITEntries (String name, String email){
		String searchStr = urlencode(name+" "+email);
		String address = mitSearch (searchStr);
		List<String[]> result = new ArrayList<String[]>();

		try{
		Document doc = Jsoup.connect(address).get();
		Elements elems = doc.select("pre");
		for(Element e:elems){
			String datePattern = "(\\d{4}-\\d{2}-\\d{2} )";
			if (e.text().split(datePattern).length < 2){ continue;}
			String text = e.text().split(datePattern)[1];
			Pattern ePattern = Pattern.compile ("<.*>");
			Matcher emailMatcher = ePattern.matcher(text);
			String parsedEmail = null;
			if (emailMatcher.find()){
				parsedEmail = emailMatcher.group();
			}

			Matcher commentMatcher = Pattern.compile ("\\(.*\\)").matcher(text);
			String parsedComment = null;
			if (commentMatcher.find()) parsedComment = commentMatcher.group();
			if (parsedComment == null || parsedEmail == null)continue;

			String parsedUsername = text.split("\\(.*\\)")[0];

			parsedUsername = parsedUsername.substring(0,parsedUsername.length()-1);
			parsedEmail = parsedEmail.substring (1, parsedEmail.length()-1);
			parsedComment = parsedComment.substring (1, parsedComment.length()-1);
			result.add(new String[]{ parsedUsername, parsedEmail, parsedComment});
		}
		}catch (Exception e){ throw new RuntimeException(e);}
		return result;
	}

	/**
	 * Retrieves all PGP comments that share the same username, email and the comment
	 * is deciphered/decoded without error.
	 *
	 * @param username
	 * @param email
	 * @param personalInfo
	 * @return list of deciphered/decoded comments.
	 */
	public List<String> getPGPComments (String username, String email, String personalInfo){
		List<String[]> possibleEntries = getMITEntries (username, email);
		List<String> filteredComments = new ArrayList<String>();

		for (String[] item: possibleEntries){
			if (!item[0].equals(username))continue;
			if (!item[1].equals(email)) continue;
			String buf = null;
			try{
				buf = decodeComment (item[0], item[1], personalInfo, item[2]);
			} catch (Exception e){
				e.printStackTrace(); //Should log this.
			}
			if (buf != null) filteredComments.add(buf);
		}
		return filteredComments;
	}


	public boolean uploadPGPRecord (String username, String email, String personalInfo, String comment) throws IOException{
		String eComment = encodeComment (username, email, personalInfo, comment);
		String queryUrl = uploadURL + "?username="+urlencode(username)+
				"&email="+urlencode(email)+"&comment="+urlencode(eComment);
		BufferedReader rd = new BufferedReader(new InputStreamReader (urlToInputStream (queryUrl)));
		String line = null;
		String result = "";
		while ((line = rd.readLine()) != null){
			result += line + "\n";
		}
		return result.contains("New public keys added");

	}

	public InputStream urlToInputStream (String queryUrl) throws IOException{
		URLConnection conn = null;
		InputStream inputStream = null;

		URL url = new URL(queryUrl);
		conn = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) conn;
		httpConn.setRequestMethod("GET");
		httpConn.connect();
		inputStream = httpConn.getInputStream();
		return inputStream;
	}

	/**
	 * Symmetric-encryption of the comment where the key
	 * is derived from the username, email and personalInfo.
	 * The personalInfo shouldn't be uploaded to the key server.
	 * The user will need to remember the personalInfo too.
	 *
	 * @param comment
	 */
	protected String encodeComment(String username, String email, String personalInfo, String comment){
		String passphrase = username+email+personalInfo;
		WeakSymmetricEncryption wse = new WeakSymmetricEncryption();
		try {
			byte[] encryption = wse.encrypt(passphrase, utf8Bytes(comment));
			return encodePGPComment (encryption);
		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException (e);
		}
	}

	public static String decodeComment (String username, String email, String personalInfo, String encodedComment) throws IOException, CryptoException{
		String passphrase = username+email+personalInfo;
		byte[] decoding = decodePGPComment (encodedComment);
		byte[] decryption = new WeakSymmetricEncryption().decrypt(passphrase, decoding);
		return utf8String (decryption);
	}

	public static String urlencode (String arg){
		try {
			return URLEncoder.encode(arg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException (e);
		}
	}
}
