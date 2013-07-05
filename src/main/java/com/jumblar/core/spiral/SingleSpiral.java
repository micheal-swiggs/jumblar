// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.spiral;

public class SingleSpiral {

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
