// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.controllers;

import static com.jumblar.core.crypto.Algorithms.generateSalt;
import static com.jumblar.core.generators.VagueHashGenerator.base64VagueHash;
import static com.jumblar.core.generators.VagueHashGenerator.base64VagueHashDecode;
import static com.jumblar.core.utils.Regex.regexFindFirst;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import com.jumblar.core.crypto.SCryptDerivation;
import com.jumblar.core.crypto.SCryptDerivationThreePoint;
import com.jumblar.core.crypto.SCryptDerivationTwoPoint;
import com.jumblar.core.domain.*;
import com.jumblar.core.encodings.Base64;
import com.jumblar.core.network.PGPKeyRecord;
import com.jumblar.core.spiral.SpiralScan;
import com.jumblar.core.spiral.SpiralScanObserver;
import com.jumblar.core.spiral.SpiralScanThreePoint;
import com.jumblar.core.spiral.SpiralScanTwoPoint;

public class BaseController {

    public SimpleContainer createNewJumbleForThreePoints(String coord1, String coord2, String coord3, ScryptParams scryptParams) {
        int[] c1 = toCoord(coord1);
        int[] c2 = toCoord(coord2);
        int[] c3 = toCoord(coord3);
        byte[] salt = generateSalt(64);
        HashBaseThreePoint hashBase = new SCryptDerivationThreePoint(salt, scryptParams).hash(c1, c2, c3);
        byte[] vagueHash = hashBase.vagueHash();
        PointsReference pointsReference = new PointsReference(salt, vagueHash, scryptParams, 3);
        return new SimpleContainer(hashBase, pointsReference);
    }

    public SimpleContainer createNewJumble(String coord1,
                                           String coord2,
                                           ScryptParams scryptParams) {
        return createNewSimpleContainer(coord1, coord2, "", scryptParams);
    }

    public SimpleContainer createNewSimpleContainer(String coord1, String coord2, String password, ScryptParams scryptParams){

        int[] c1 = toCoord(coord1);
        int[] c2 = toCoord(coord2);
        byte[] salt = generateSalt(64);
        HashBaseTwoPoint hashBaseTwoPoint = new SCryptDerivationTwoPoint(password, salt, scryptParams).hash(c1, c2);
        byte[] vagueHash = hashBaseTwoPoint.vagueHash();
        PointsReference pointsReference = new PointsReference(salt, vagueHash, scryptParams, 2);
        return new SimpleContainer(hashBaseTwoPoint, pointsReference);
    }

    public HashBase computeHashBaseForThreePoints(PointsReference spf,
                                                  String coord1,
                                                  String coord2,
                                                  String coord3,
                                                  SpiralScanObserver observer) {
        int[] c1 = toCoord(coord1);
        int[] c2 = toCoord(coord2);
        int[] c3 = toCoord(coord3);
        SpiralScanThreePoint spiralScanThreePoint = new SpiralScanThreePoint(c1, c2, c3, spf.getSalt(), spf.getVagueHash(), spf.scryptParams);
        int[][] actualCoords = spiralScanThreePoint.attemptMatch(900000000, observer);
        if (actualCoords == null) {
            return null;
        }
        return new SCryptDerivationThreePoint(spf.getSalt(), spf.scryptParams)
                .hash(actualCoords[0], actualCoords[1], actualCoords[2]);

    }
    public HashBase computeHashBaseForTwoPoints(PointsReference spf, String coord1, String coord2) {
        int[] c1 = toCoord(coord1);
        int[] c2 = toCoord(coord2);
        SpiralScanTwoPoint ds = new SpiralScanTwoPoint(c1, c2, "", spf.getVagueHash(), spf.getSalt(), spf.scryptParams);
        int[][] actualCoordinates = ds.attemptMatch(90000000);
        if (actualCoordinates == null) return null;
        return new SCryptDerivationTwoPoint("", spf.getSalt(), spf.scryptParams)
                .hash(actualCoordinates[0], actualCoordinates[1]);
    }


    public SimpleContainer createNewPGPEntry(String username, String email, String personalInfo, String password, String coordinate,
                                             ScryptParams scryptParams){
        int xCoord, yCoord;
        String[] coords = coordinate.split(",");
        xCoord = (int) (new Double(coords[0]) * 1000000);
        yCoord = (int) (new Double(coords[1]) * 1000000);
        byte[] salt = generateSalt(64);
        String vagueHash = base64VagueHash(xCoord, yCoord, password, salt, scryptParams);
        String comment = vagueHashTag(vagueHash);
        comment += scryptTag(scryptParams);
        comment += creationTimeTag(null);
        comment += saltTag(salt);
        comment += nPointsTag(1);
        PGPKeyRecord gpgRecord = new PGPKeyRecord();
        boolean result = false;
        try {
            result = gpgRecord.uploadPGPRecord(username, email, personalInfo, comment);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (result) {
            PointsReference spf;
            try {
                spf = new PointsReference(
                        salt, base64VagueHashDecode(vagueHash),
                        scryptParams, 1);
                HashBase hb = new HashBase(new SCryptDerivation(xCoord, yCoord, password, salt,
                        scryptParams).hash());
                return new SimpleContainer(hb, spf);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return null;
    }

    static public HashBase computeHashBaseForSingleCoord(PointsReference spf, String password, String coordinate) {
        int[] coords = toCoord(coordinate);
        SpiralScan ss = new SpiralScan(coords[0], coords[1], password, spf.getVagueHash(), spf.getSalt(), spf.scryptParams);
        int[] actualCoordinates = ss.attemptMatch(2000);
        if (actualCoordinates == null) return null;
        try {
            return new HashBase(
                    new SCryptDerivation(actualCoordinates[0], actualCoordinates[1], password, spf.getSalt(), spf.scryptParams).hash());
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static PointsReference convertPGPComment(String username, String email, String personalInfo, String[] comment) throws IOException {
        byte[] vHash = base64VagueHashDecode(comment[0]);
        byte[] salt = Base64.decode(comment[2]);
        String[] scryptParamsVal = comment[3].split(",");
        int N = Integer.parseInt(scryptParamsVal[0]);
        int r = Integer.parseInt(scryptParamsVal[1]);
        int p = Integer.parseInt(scryptParamsVal[2]);
        int keyLength = Integer.parseInt(scryptParamsVal[3]);
        ScryptParams scryptParams = new ScryptParams(N, r, p, keyLength);
        PointsReference spf = new PointsReference(
                salt, vHash, scryptParams, 2);
        return spf;
    }

    public SimpleContainer computeHashBase(String username, String email, String personalInfo, String password, String gCoordinate1, String gCoordinate2) throws IOException {
        String[] oldestEntry = urlGetOldestPGPEntry(username, email, personalInfo, 2);
        PointsReference spf = convertPGPComment(username, email, personalInfo, oldestEntry);

        int[] coord1 = toCoord(gCoordinate1);
        int[] coord2 = toCoord(gCoordinate2);
        SpiralScanTwoPoint ss = new SpiralScanTwoPoint(coord1, coord2, password,
                spf.getVagueHash(), spf.getSalt(),spf.scryptParams);
        int[][] actualCoordinates = ss.attemptMatch(90000);
        if (actualCoordinates == null) return null;
        HashBaseTwoPoint hashBase = new SCryptDerivationTwoPoint(
                password, spf.getSalt(), spf.scryptParams).hashBase();
        return new SimpleContainer(hashBase, spf);

    }


    public SimpleContainer computeHashBase(String username, String email, String personalInfo, String password, String guessCoordinate) throws IOException {
        String[] oldestEntry = urlGetOldestPGPEntry(username, email, personalInfo, 1);
        byte[] vHash = base64VagueHashDecode(oldestEntry[0]);
        byte[] salt = Base64.decode(oldestEntry[2]);
        String[] scryptParamsVal = oldestEntry[3].split(",");
        int N = Integer.parseInt(scryptParamsVal[0]);
        int r = Integer.parseInt(scryptParamsVal[1]);
        int p = Integer.parseInt(scryptParamsVal[2]);
        int keyLength = Integer.parseInt(scryptParamsVal[3]);
        ScryptParams scryptParams = new ScryptParams(N, r, p, keyLength);
        int[] coords = toCoord(guessCoordinate);
        SpiralScan ss = new SpiralScan(coords[0], coords[1], password, vHash, salt, scryptParams);
        int[] actualCoordinates = ss.attemptMatch(2000);
        if (actualCoordinates == null) return null;
        PointsReference spf = new PointsReference(
                salt, vHash, scryptParams,
                1);
        HashBase hb;
        try {
            hb = new HashBase(new SCryptDerivation(actualCoordinates[0], actualCoordinates[1], password, salt,
                    scryptParams).hash());
            return new SimpleContainer(hb, spf);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    public String[] urlGetOldestPGPEntry(String username, String email, String personalInfo, int nPoints) {
        List<String[]> results = retrievePGPEntries(username, email, personalInfo);

        //Find the oldest entry
        String[] oldestEntry = null;
        for (String[] i : results) {
            if (Integer.parseInt(i[4]) != nPoints) continue;
            if (oldestEntry == null ||
                    Long.parseLong(i[1]) < Long.parseLong(oldestEntry[1])) {
                oldestEntry = i;
            }
        }
        return oldestEntry;
    }

    public static String[] parseEncodedComment(String comment) {
        String vHash = regexFindFirst(comment, "\\[VagueHash\\].*\\[/VagueHash\\]");
        if (vHash == null) return null;
        vHash = vHash.split("\\[/?VagueHash\\]")[1];
        String creationTime = regexFindFirst(comment, "\\[CreationTime\\].*\\[/CreationTime\\]");
        if (creationTime == null) return null;
        creationTime = creationTime.split("\\[/?CreationTime\\]")[1];
        String salt = regexFindFirst(comment, "\\[Salt\\].*\\[/Salt\\]");
        if (salt == null) return null;
        salt = salt.split("\\[/?Salt\\]")[1];
        String scryptParams = regexFindFirst(comment, "\\[SCrypt\\].*\\[/SCrypt\\]");
        if (scryptParams == null) return null;
        scryptParams = scryptParams.split("\\[/?SCrypt\\]")[1];
        String nPoints = regexFindFirst(comment, "\\[NPoints].*\\[/NPoints\\]");
        if (nPoints == null) nPoints = nPointsTag(1);
        nPoints = nPoints.split("\\[/?NPoints\\]")[1];
        return new String[]{vHash, creationTime, salt, scryptParams, nPoints};
    }

    List<String[]> retrievePGPEntries(String username, String email, String personalInfo) {
        PGPKeyRecord pgpRecord = new PGPKeyRecord();
        List<String> comments = pgpRecord.getPGPComments(username, email, personalInfo);
        List<String[]> results = new ArrayList<String[]>();
        for (String comment : comments) {
            String[] buf = parseEncodedComment(comment);
            if (buf != null) results.add(buf);
        }
        return results;
    }

    public String vagueHashTag(String vh) {
        return "[VagueHash]" + vh + "[/VagueHash]";
    }

    public String scryptTag(ScryptParams scryptParams) {
        return "[SCrypt]" + scryptParams.N + "," + scryptParams.r + "," + scryptParams.p + "," + scryptParams.keyLength + "[/SCrypt]";
    }

    public String creationTimeTag(Long t) {
        if (t == null) {
            t = System.currentTimeMillis();
        }
        return "[CreationTime]" + t + "[/CreationTime]";
    }

    public String saltTag(byte[] salt) {
        return "[Salt]" + Base64.encodeBytes(salt) + "[/Salt]";
    }

    public static String nPointsTag(int nPoints) {
        return "[NPoints]" + nPoints + "[/NPoints]";
    }

    static public int[] toCoord(String coord) {
        String[] coords = coord.split(",");
        return new int[]{
                (int) (new Double(coords[0]) * 1000000),
                (int) (new Double(coords[1]) * 1000000)
        };
    }

}
