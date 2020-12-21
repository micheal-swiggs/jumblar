package com.jumblar.core.domain;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;


public class PointsReferenceSerializerTest extends TestCase {

    @Test
    public void testSerialization(){
        PointsReference pointsReference = new PointsReference(new byte[]{1,2}, new byte[]{3,4,5},
                new ScryptParams(1, 10, 100, 1000),
                1);
        String serialized = PointsReferenceSerializer.serialise(pointsReference);
        PointsReference deserialized = PointsReferenceSerializer.deserialize(serialized);

        assertArrayEquals(pointsReference.salt, deserialized.salt);
        assertArrayEquals(pointsReference.vagueHash, deserialized.vagueHash);
        assertEquals(pointsReference.nPoints, deserialized.nPoints);
        assertEquals(pointsReference.scryptParams, deserialized.scryptParams);
    }

}