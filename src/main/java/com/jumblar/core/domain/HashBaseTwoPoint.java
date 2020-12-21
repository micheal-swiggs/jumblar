package com.jumblar.core.domain;

import com.jumblar.core.utils.Arrays;

public class HashBaseTwoPoint extends HashBase {

    private static final int NBYTES = 4;

    public HashBaseTwoPoint(byte[] base) {
        super(base);
    }

    public byte[] vagueHash() {
        return shortenedHash();
    }

    private byte[] shortenedHash(){
        return Arrays.copyOfRange(base, 0, NBYTES);
    }

}
