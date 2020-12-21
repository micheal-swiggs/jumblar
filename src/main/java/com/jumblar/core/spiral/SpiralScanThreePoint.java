package com.jumblar.core.spiral;

import com.jumblar.core.crypto.SCryptDerivationThreePoint;
import com.jumblar.core.domain.HashBaseThreePoint;
import com.jumblar.core.domain.ScryptParams;
import com.jumblar.core.utils.Arrays;

public class SpiralScanThreePoint {

    public final byte[] salt;
    public final byte[] vagueHash;
    public final ScryptParams scryptParams;
    public final TripleSpiral tripleSpiral;

    public SpiralScanThreePoint(int[] guess1, int[] guess2, int[] guess3, byte[] salt, byte[] vagueHash, ScryptParams scryptParams) {
        this.salt = salt;
        this.vagueHash = vagueHash;
        this.scryptParams = scryptParams;
        this.tripleSpiral = new TripleSpiral(guess1, guess2, guess3);
    }

    public int[][] attemptMatch (int nRounds, SpiralScanObserver observer){

        SCryptDerivationThreePoint sCryptDeriv = new SCryptDerivationThreePoint(salt, scryptParams);
        byte[] guessHash;

        try {
            for (int i = 0; i < nRounds; i++) {
                observer.increment();
                int[][] nextPoint = tripleSpiral.nextItem();
                HashBaseThreePoint hashBase = sCryptDeriv.hash(nextPoint[0], nextPoint[1], nextPoint[2]);
                guessHash = hashBase.vagueHash();
                if (Arrays.equals(vagueHash, guessHash)) {
                    return nextPoint;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
