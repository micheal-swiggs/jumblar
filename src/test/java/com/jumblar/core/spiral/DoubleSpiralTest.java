package com.jumblar.core.spiral;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DoubleSpiralTest extends TestCase {

    public void testSpiralCoverage(){

        // LOAD COORDINATES
        Set<SpiralCoordinate> coordinates = new HashSet<>(1000000);
        int[][] spiralPoints = SingleSpiral.getPoints();

        for (int point1=0; point1<1000; point1++){
            for (int point2 = 0; point2<1000; point2++){
                    coordinates.add(new SpiralCoordinate(
                            spiralPoints[point1],
                            spiralPoints[point2]));
            }
        }

        DoubleSpiral doubleSpiral = new DoubleSpiral(spiralPoints[0], spiralPoints[0]);


        while (!coordinates.isEmpty()){
            int[][] nextItem = doubleSpiral.nextItem();
            SpiralCoordinate coord = new SpiralCoordinate(nextItem[0], nextItem[1]);
            if (!coordinates.contains(coord)){
                throw new RuntimeException("coordinate is missing");
            }
            coordinates.remove(coord);
        }

        System.out.println(coordinates.size());
    }

    static class SpiralCoordinate {

        final int[] point1, point2;

        SpiralCoordinate(int[] point1, int[] point2) {
            this.point1 = point1;
            this.point2 = point2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SpiralCoordinate that = (SpiralCoordinate) o;
            return Arrays.equals(point1, that.point1) &&
                    Arrays.equals(point2, that.point2);
        }

        @Override
        public int hashCode() {
            int result = Arrays.hashCode(point1);
            result = 31 * result + Arrays.hashCode(point2);
            return result;
        }
    }

}