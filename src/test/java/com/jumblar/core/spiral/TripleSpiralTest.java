package com.jumblar.core.spiral;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TripleSpiralTest extends TestCase {

    public void testSpiralCoverage() throws InterruptedException {

        // LOAD COORDINATES
        Set<SpiralCoordinate> coordinates = new HashSet<>(1000000);
        int[][] spiralPoints = SingleSpiral.getPoints();

        for (int point1=0; point1<200; point1++){
            for (int point2 = 0; point2<200; point2++){
                for (int point3 = 0; point3<200; point3++){
                    coordinates.add(new SpiralCoordinate(
                            spiralPoints[point1],
                            spiralPoints[point2],
                            spiralPoints[point3]));
                }
            }
        }

        TripleSpiral tripleSpiral = new TripleSpiral(spiralPoints[0], spiralPoints[0], spiralPoints[0]);

        while (!coordinates.isEmpty()){
            int[][] nextItem = tripleSpiral.nextItem();
            SpiralCoordinate coord = new SpiralCoordinate(nextItem[0], nextItem[1], nextItem[2]);
            if (!coordinates.contains(coord)){
                throw new RuntimeException("coordinate is missing");
            }
            coordinates.remove(coord);
        }

        System.out.println(coordinates.size());
    }

    static class SpiralCoordinate {
        final int[] point1, point2, point3;

        SpiralCoordinate(int[] point1, int[] point2, int[] point3) {
            this.point1 = point1;
            this.point2 = point2;
            this.point3 = point3;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SpiralCoordinate that = (SpiralCoordinate) o;
            return Arrays.equals(point1, that.point1) &&
                    Arrays.equals(point2, that.point2) &&
                    Arrays.equals(point3, that.point3);
        }

        @Override
        public int hashCode() {
            int result = Arrays.hashCode(point1);
            result = 31 * result + Arrays.hashCode(point2);
            result = 31 * result + Arrays.hashCode(point3);
            return result;
        }
    }
}