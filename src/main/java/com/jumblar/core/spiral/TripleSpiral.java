// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.spiral;

import java.util.concurrent.ArrayBlockingQueue;

public class TripleSpiral {
	
	int[] point1;
	int[] point2;
	int[] point3;	
	int[][] points;
	Thread spiralThread;
	
	public TripleSpiral (int[] point1, int[] point2, int[] point3){
		this.point1 = point1;
		this.point2 = point2;
		this.point3 = point3;
		this.points = SingleSpiral.getPoints();
		spiralThread = new Thread( new TripleSpiralThread());
		spiralThread.start();
	}
	
	public int[][] nextItem() throws InterruptedException{
		Integer[] item = queue.take();
		return new int[][]{
				{points[item[0]][0] + point1[0], points[item[0]][1] + point1[1]},
				{points[item[1]][0] + point2[0], points[item[1]][1] + point2[1]},
				{points[item[2]][0] + point3[0], points[item[2]][1] + point3[1]}
		};
	}
	
	/**
	 * Should be called once a match is found.
	 */
	public void finished(){
		expectInterupt = true;
		spiralThread.interrupt();
	}
	
	boolean expectInterupt = false;
	ArrayBlockingQueue<Integer[]> queue = new ArrayBlockingQueue<>(10);
	class TripleSpiralThread implements Runnable{

		public void run() {
			try{
				for (int i=0; i<3000; i++){
					int limit = i;
					int x = limit, y = 0, z = 0;
					while(y != limit || z != limit){
						putInQueue (x,y,z);
						putInQueue (z,x,y);
						putInQueue (y,z,x);

						if (y != z && x != y){ //Reflectable
							putInQueue (z,y,x);
							putInQueue (y,x,z);
							putInQueue (x,z,y);
						}
						
						if (y == z){
							y += 1;
							z = 0;
						} else {
							z += 1;
						}
					}
					putInQueue (x,y,z);
				}			
			} catch (InterruptedException e){
				if(!expectInterupt){
					throw new RuntimeException (e);
				}
			}
		}
		
		private void putInQueue(int i, int j, int k) throws InterruptedException{
			queue.put(new Integer[]{i,j,k});
		}
		
	}
}
