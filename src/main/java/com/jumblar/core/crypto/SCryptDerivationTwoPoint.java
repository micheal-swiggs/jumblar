// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.crypto;

import java.security.GeneralSecurityException;

import com.jumblar.core.domain.HashBaseTwoPoint;
import com.jumblar.core.domain.ScryptParams;
import com.lambdaworks.crypto.SCrypt;

import static com.jumblar.core.generators.CharacterGenerator.*;

/**
 * @author Micheal Swiggs
 */
public class SCryptDerivationTwoPoint {
    final byte[] salt;
    // first 16 bytes contain the two locations & remaining bytes contain the password.
    final byte[] base;
    final ScryptParams scryptParams;

    public SCryptDerivationTwoPoint(String password,
                                    byte[] salt,
                                    ScryptParams scryptParams){
        byte[] pwordBytes = utf8Bytes(password);
        base = new byte[16 + pwordBytes.length];
        for (int i = 0; i < pwordBytes.length; i++) {
            base[i + 16] = pwordBytes[i];
        }
        this.salt = salt;
        this.scryptParams = scryptParams;
    }

    public HashBaseTwoPoint hashBase() {
        try {
            byte[] bytes = SCrypt.scrypt(base, salt, scryptParams.N, scryptParams.r, scryptParams.p, scryptParams.keyLength);
            return new HashBaseTwoPoint(bytes);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public HashBaseTwoPoint hash(int[] pt1, int[] pt2) {
        placePoints(pt1, pt2);
        return hashBase();
    }

    private void placePoints(int[] pt1, int[] pt2) {
        base[0] = (byte) (pt1[0] >> 24);
        base[1] = (byte) (pt1[0] >> 16);
        base[2] = (byte) (pt1[0] >> 8);
        base[3] = (byte) (pt1[0]);
        base[4] = (byte) (pt1[1] >> 24);
        base[5] = (byte) (pt1[1] >> 16);
        base[6] = (byte) (pt1[1] >> 12);
        base[7] = (byte) (pt1[1]);

        base[8] = (byte) (pt2[0] >> 24);
        base[9] = (byte) (pt2[0] >> 16);
        base[10] = (byte) (pt2[0] >> 8);
        base[11] = (byte) (pt2[0]);
        base[12] = (byte) (pt2[1] >> 24);
        base[13] = (byte) (pt2[1] >> 16);
        base[14] = (byte) (pt2[1] >> 8);
        base[15] = (byte) (pt2[1]);

    }
}
