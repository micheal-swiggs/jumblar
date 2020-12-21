// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.domain;

import java.util.Arrays;

/**
 * @author Micheal Swiggs
 * Contains enough information to determine the correct
 * password and coordinates without needing to contact
 * a PGP server.
 */
public class PointsReference {

    final byte[] salt;
    final byte[] vagueHash;
    public final ScryptParams scryptParams;
    public final int nPoints;

    public PointsReference(byte[] salt,
                           byte[] vHash,
                           ScryptParams scryptParams,
                           int nPoints) {
        this.salt = salt;
        this.vagueHash = vHash;
        this.scryptParams = scryptParams;
        this.nPoints = nPoints;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getVagueHash() {
        return vagueHash;
    }

    @Override
    public String toString() {
        return "PointsReference{" +
                "salt=" + Arrays.toString(salt) +
                ", vagueHash=" + Arrays.toString(vagueHash) +
                ", scryptParams=" + scryptParams +
                ", nPoints=" + nPoints +
                '}';
    }
}
