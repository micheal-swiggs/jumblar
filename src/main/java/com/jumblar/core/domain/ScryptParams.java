package com.jumblar.core.domain;

import java.util.Objects;

public class ScryptParams {

    public static final ScryptParams BASIC = new ScryptParams(1024, 8, 1, 64);
    public static final ScryptParams MEMORY_16MB = new ScryptParams(16384,
            8, 1,64);

    public final int N;
    public final int r;
    public final int p;
    public final int keyLength;

    public ScryptParams(int N, int r, int p, int keyLength) {
        this.N = N;
        this.r = r;
        this.p = p;
        this.keyLength = keyLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScryptParams that = (ScryptParams) o;
        return N == that.N &&
                r == that.r &&
                p == that.p &&
                keyLength == that.keyLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(N, r, p, keyLength);
    }
}
