package com.jumblar.core.crypto;

import com.jumblar.core.domain.HashBaseThreePoint;
import com.jumblar.core.domain.HashBaseTwoPoint;
import com.jumblar.core.domain.ScryptParams;
import com.lambdaworks.crypto.SCrypt;

import java.security.GeneralSecurityException;

public class SCryptDerivationThreePoint {

    private final byte[] salt;
    private final byte[] base;
    private final ScryptParams scryptParams;

    public SCryptDerivationThreePoint(byte[] salt, ScryptParams scryptParams) {
        this.salt = salt;
        this.base = new byte[24];
        this.scryptParams = scryptParams;
    }

    public HashBaseThreePoint hashBase() {
        try {
            byte[] bytes = SCrypt.scrypt(base, salt, scryptParams.N, scryptParams.r, scryptParams.p, scryptParams.keyLength);
            return new HashBaseThreePoint(bytes);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public HashBaseThreePoint hash(int[] pt1, int[] pt2, int[] pt3){
        placePoints(pt1, pt2, pt3);
        return hashBase();
    }

    private void placePoints(int[] pt1, int[] pt2, int[] pt3) {
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

        base[16] = (byte) (pt3[0] >> 24);
        base[17] = (byte) (pt3[0] >> 16);
        base[18] = (byte) (pt3[0] >> 8);
        base[19] = (byte) (pt3[0]);
        base[20] = (byte) (pt3[1] >> 24);
        base[21] = (byte) (pt3[1] >> 16);
        base[22] = (byte) (pt3[1] >> 8);
        base[23] = (byte) (pt3[1]);
    }
}
