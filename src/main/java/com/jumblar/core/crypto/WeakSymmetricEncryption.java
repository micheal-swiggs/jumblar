// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.crypto;

import static com.jumblar.core.generators.CharacterGenerator.utf8Bytes;

import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.CryptoException;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.crypto.engines.AESEngine;
import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.crypto.paddings.PKCS7Padding;
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;

/**
 * 
 * @author Micheal Swiggs
 *
 * Maybe unnecessary. Was meant to provide a way of 'obsfucating'
 * PGP comments. PGP comments would be encrypted using non-essential
 * personal data, e.g place-of-birth. This would make it difficult
 * for others to see the Jumble. 
 * 
 * For the same username & email it may be possible to have multiple
 * PGP entries. The order could be encoded in the comment. Which would
 * be protected by weak-encryption. If an adversary broke the encryption
 * they could only disrupt the order.
 */
public class WeakSymmetricEncryption {
	
	protected PaddedBufferedBlockCipher createCipher(String passphrase, boolean isEncrypting) throws CryptoException{
		CustomRandom cRandom = new CustomRandom(utf8Bytes(passphrase));
		byte[] salt = new byte[8];
		cRandom.nextBytes(salt);
		byte[] password = new byte[32];
		cRandom.nextBytes(password);
		int iterationCount = 1000;
		int keyLength = 256;		
		int blockSize = 128;
		
		PKCS5S2ParametersGenerator generator = new PKCS5S2ParametersGenerator(new SHA256Digest());
		generator.init(password, salt, iterationCount);
	
		CipherParameters key = generator
					.generateDerivedParameters(keyLength, blockSize);
		
		CBCBlockCipher cbcCipher = new CBCBlockCipher(new AESEngine());
		PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
				cbcCipher, new PKCS7Padding());
		cipher.init(isEncrypting, key);
		return cipher;
	}
	
	public byte[] encrypt(String passphrase, byte[] plainText ) throws CryptoException{
		PaddedBufferedBlockCipher cipher = createCipher (passphrase, true);
		byte[] cipherTextTmp = new byte[cipher.getOutputSize(plainText.length)];
		int offset = cipher.processBytes(plainText, 0,  plainText.length, cipherTextTmp, 0);
		int last = cipher.doFinal(cipherTextTmp, offset);
		final byte[] encryption = new byte[offset+last];
		System.arraycopy(cipherTextTmp, 0, encryption, 0, encryption.length);
		return encryption;	
	}
	
	public byte[] decrypt (String passphrase, byte[] cipherText) throws CryptoException{
		PaddedBufferedBlockCipher cipher = createCipher(passphrase, false);
		byte[] plainTemp = new byte[cipher.getOutputSize(cipherText.length)];
	    int offset = cipher.processBytes(cipherText, 0, cipherText.length, plainTemp, 0);
	    int last = cipher.doFinal(plainTemp, offset);
	    final byte[] plain = new byte[offset + last];
	    System.arraycopy(plainTemp, 0, plain, 0, plain.length);
	    return plain;
	}

}
