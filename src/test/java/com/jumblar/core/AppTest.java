package com.jumblar.core;

import java.util.Arrays;

import com.jumblar.core.domain.*;
import com.jumblar.core.spiral.SpiralScanObserver;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.jumblar.core.controllers.BaseController;
import com.jumblar.core.controllers.PhraseController;
import com.jumblar.core.crypto.WeakSymmetricEncryption;
import com.jumblar.core.encodings.Base64;
import com.jumblar.core.network.PGPKeyRecord;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

/**
 * Unit test for simple App.
 */
public class AppTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testSymmetricEncryption() throws Exception{
    	WeakSymmetricEncryption wse = new WeakSymmetricEncryption();
    	String password = "password";
    	byte[] msg = "This is a test message".getBytes("UTF-8");
    	assertNotNull(wse.encrypt(password, msg));
    }

    public void testCommentDecryption() throws Exception
    {
    	String username = "testing-user";
    	String email = "testing-user@special.domain";
    	String personalInfo = "";
    	String password = "password";
    	String coordinate = "48.858404,2.293571"; //Corner of eiffel tower.
        //Comment copied from key server lookup.
        //String comment = "b2s9dXzAbwVlAsE.xeHfBsdwnaIrRTxBdtIInSL9bEUwnKxa6m9zVsw4DTDb2UKlFoRy6HPAOVa02Yq2Ytkc 1Tpdidck3K6iEE9Mb0pZOfu4g6gltlVv1LUmWarQgEjdVu2XibCXyuc9hhtjDTpaYoIgzBl9.ta4Pxd8gloHJBA4sw.VkhGZNJWoMpaSRdVT9.2Kgxap9S.AKWnYyp.qqummDA3gDS8DFe7Bb5uss1ux9YPASwGj5kCTOcxnz.xcxrzU56wDLK iKcg7i14DdK  e  TH6ipvSXopf3L90";
        String comment = "EzM8EBpzVhTRO3whk7wuHtnfecShbI0AIQiP2cvV8tdOCaZCvJNEdiEoP95NxyWYpa8il3pZ2JA i8vsodl2DY3xbwJiLHLTd2e6IZ4a.lbEFF4rNhbQF7e25AQih5vA.HiXIId.lYhZNOyXMSGYsW RSns0 J24z5pQMkjxeW9.f4u7bN0OVNfTav5M4gQM8XuW7O1487O0uCk1dbsts1qZom5skDJztt3Gw7wn5pZuFaVAfin5zOz5W9NEvrMGjELh3jVqS9bV5XrGOS 3.w";
        Object something = PGPKeyRecord.decodeComment(username, email, personalInfo, comment);
        System.out.println(something);
        String[] ss = BaseController.parseEncodedComment((String)something);
        System.out.println(ss);
        PointsReference spf = BaseController.convertPGPComment(username, email, personalInfo, ss);
        System.out.println(spf);

        HashBase hb = BaseController.computeHashBaseForSingleCoord(spf, password, coordinate );
        System.out.println(hb);

        System.out.println(PhraseController.generatePhrase(hb, "prefix", 15));
    }

    public void testBaseController() throws Exception{
    	//if(true) throw new RuntimeException("Test Ignored");
    	String username = "testing-user";
    	String email = "testing-user@special.domain";
    	String personalInfo = "";
    	String password = "password";
    	String coordinate = "48.858404,2.293571"; //Corner of eiffel tower.
		ScryptParams scryptParams = new ScryptParams(1024, 8, 1, 64);
		/** The following is for user registration. */
    	BaseController bc = new BaseController();
    	SimpleContainer simpleContainer = bc.createNewPGPEntry(username, email, personalInfo, password, coordinate,
    			scryptParams);
    	assertNotNull (simpleContainer);

    	HashBase hb = BaseController.computeHashBaseForSingleCoord(simpleContainer.getPointsReference(),
    			password, coordinate);
    	assertTrue(Arrays.equals(simpleContainer.getHashBase().getBytes(),
    			hb.getBytes()));


    	/** The following is for retrieving a user pgp entry */
    	String actualHashBase = "5Oi4fog15fLJq++SslcZPRFtl1fe1sP800/r8sFmB6LpFmPUK2M+hWP1sOuKVGrosVujsGYQRIr/XNTt/Adrpg==";
    	String guessCoordinate = "48.858405,2.293577";
    	SimpleContainer sj = bc.computeHashBase(username, email, personalInfo, password, guessCoordinate);
    	String guessHashBase = (Base64.encodeBytes(sj.getHashBase().getBytes()));
    	assertEquals (actualHashBase, guessHashBase);
    }

    public void testCreateTwoPointSimpleContainer(){

		String coordinate1 = "48.858404,2.293571"; //Corner of eiffel tower.
		String coordinate2 = "-25.343269153592498, 131.02171142195186"; // edge of uluru.
		BaseController baseController = new BaseController();
		SimpleContainer newJumble = baseController.createNewJumble(coordinate1, coordinate2, new ScryptParams(2, 8, 1, 64));

		byte[] originalHashBase = newJumble.getHashBase().getBytes();
		PointsReference pointsReference = newJumble.getPointsReference();

		String serialisedPointsReference = PointsReferenceSerializer.serialise(pointsReference);

		String guess1 = "48.85840929275292, 2.293549542239457"; //Corner of eiffel tower.
		String guess2 = "-25.343266153592498, 131.02171542195186"; // edge of uluru.

		PointsReference deserialisedPointsReference = PointsReferenceSerializer.deserialize(serialisedPointsReference);
		HashBase estimatedHashBase = baseController.computeHashBaseForTwoPoints(deserialisedPointsReference, guess1, guess2);

		assertArrayEquals(estimatedHashBase.getBytes(), originalHashBase);
	}

	public void testCreateThreePointSimpleContainer(){

		String coordinate1 = "48.858404,2.293571"; //Corner of eiffel tower.
		String coordinate2 = "-25.343269153592498, 131.02171142195186"; // edge of uluru.
		String coordinate3 = "60.40360442674755, 18.173187289504973"; // Forsmarks kärnkraftverk
		BaseController baseController = new BaseController();
		SimpleContainer newJumble = baseController.createNewJumbleForThreePoints(coordinate1, coordinate2, coordinate3, new ScryptParams(2, 1, 1, 64));

		byte[] originalHashBase = newJumble.getHashBase().getBytes();
		PointsReference pointsReference = newJumble.getPointsReference();

		String serialisedPointsReference = PointsReferenceSerializer.serialise(pointsReference);

		//String guess1 = "48.858404,2.293571"; //Corner of eiffel tower.
		String guess1 = "48.858400890987504, 2.293582733234067"; //Corner of eiffel tower. actualRounds = 141420761

		String guess2 = "-25.343269153592498, 131.02171142195186"; // edge of uluru.
		//String guess2 = "-25.343271001803014, 131.02170771004674"; // edge of uluru. actualRounds = 195113

		String guess3 = "60.40360442674755, 18.173187289504973"; // Forsmarks kärnkraftverk
		//String guess3 = "60.40359544017526, 18.173186963654818"; // Forsmarks kärnkraftverk actualRounds = 26463594

		PointsReference deserialisedPointsReference = PointsReferenceSerializer.deserialize(serialisedPointsReference);
		HashBase estimatedHashBase = baseController.computeHashBaseForThreePoints(deserialisedPointsReference, guess1, guess2, guess3, new SpiralScanObserver());

		assertArrayEquals(estimatedHashBase.getBytes(),originalHashBase);

	}

}
