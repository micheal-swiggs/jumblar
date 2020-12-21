// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.crypto;

import java.security.GeneralSecurityException;

import com.jumblar.core.domain.ScryptParams;
import com.lambdaworks.crypto.SCrypt;

import static com.jumblar.core.generators.CharacterGenerator.*;


/**
 * @author Micheal Swiggs
 */
public class SCryptDerivation {

    byte[] salt;
    byte[] base;
    final ScryptParams scryptParams;

    public SCryptDerivation(int x, int y, String password, byte[] salt,
                            ScryptParams scryptParams) {
        byte[] pwordBytes = utf8Bytes(password);
        base = new byte[8 + pwordBytes.length];
        placePoint(x, y);
        for (int i = 0; i < pwordBytes.length; i++) {
            base[i + 8] = pwordBytes[i];
        }
        this.salt = salt;
        this.scryptParams = scryptParams;
    }

    public byte[] hash() throws GeneralSecurityException {
        return SCrypt.scrypt(base, salt, scryptParams.N, scryptParams.r, scryptParams.p, scryptParams.keyLength);
    }

    public byte[] hash(int x, int y) throws GeneralSecurityException {
        placePoint(x, y);
        return hash();
    }

    protected void placePoint(int x, int y) {
        base[0] = (byte) (x >> 24);
        base[1] = (byte) (x >> 16);
        base[2] = (byte) (x >> 8);
        base[3] = (byte) (x);
        base[4] = (byte) (y >> 24);
        base[5] = (byte) (y >> 16);
        base[6] = (byte) (y >> 8);
        base[7] = (byte) y;
    }
}
