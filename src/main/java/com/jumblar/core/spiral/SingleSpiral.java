// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.spiral;

/**
 * @author Micheal Swiggs
 * 
 * For spiraling around a central point. Spirals
 * in a grid rather than a disc.
 */
public class SingleSpiral {

	private static final int pointSize =  2000;
	private static final int[][] points = new int[pointSize][2];
	private static boolean pointsInit = false;
	
	public static void println (Object ob){
		System.out.println (""+ob);
	}
	public static int[][] getPoints(){
		if (!pointsInit){
			SingleSpiral ss = new SingleSpiral(0,0);
			for(int i=0; i<pointSize;i++	){
				int[] b = ss.nextItem();
				points[i][0] = b[0];
				points[i][1] = b[1];
			}
			pointsInit = true;
		}
		return points;
	}
	int actualX, actualY;
	int x,y; 
	boolean isHorizontal, isPositive;
	int nSteps, stepCount;
	
	boolean initialStepCalled;
	
	public SingleSpiral (int aX, int aY){
		x = 0; y = 0;
		actualX = aX;
		actualY = aY;
		isHorizontal = true;
		isPositive = true;
		nSteps = 1;
		stepCount = 1;
		initialStepCalled = false;
	}
	
	public int[] nextItem(){
		if (initialStepCalled){
			increment();
		} else {
			initialStepCalled = true;
		}
		return new int[]{actualX+x, actualY+y};
	}
	
	public void increment(){
		if (isHorizontal){
			if (isPositive){
				x++;
			} else {
				x--;
			}
			stepCount--;
			if (stepCount == 0){
				isHorizontal = false;
				stepCount = nSteps;
			}
		} else {
			if (isPositive){
				y++;
			} else {
				y--;
			}
			stepCount--;
			if (stepCount == 0){
				isHorizontal = true;
				isPositive = isPositive ? false : true;
				nSteps++;
				stepCount = nSteps;
			}
		}
	}
}
