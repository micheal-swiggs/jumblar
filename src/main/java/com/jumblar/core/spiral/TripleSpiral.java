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
	ArrayBlockingQueue<Integer[]> queue = new ArrayBlockingQueue<Integer[]>(10);
	class TripleSpiralThread implements Runnable{

		public void run() {
			try{
				for (int i=0; i<1500; i++){
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
					e.printStackTrace();
					throw new RuntimeException (e);
				}
			}
		}
		
		private void putInQueue(int i, int j, int k) throws InterruptedException{
			queue.put(new Integer[]{i,j,k});
		}
		
	}
	
	/*
	public static void println(int[][] item){
		if(item == null){
			println("null");
			return;
		}
		String b = item[0][0]+","+item[0][1]+"  "+item[1][0]+","+item[1][1]+"  "+item[2][0]+","+item[2][1];
		println (b);
	}
	public static void println (int x, int y, int z){
		cnt++;
		String b = x+","+y+","+z;
		hSet.add(b);
		println (b);
	}
	
	public static void println (Object ob){
		System.out.println (""+ob);
	}
	public static HashSet<String> hSet;
	public static int cnt;
	public static void main1(String[] args){
		cnt = 0;
		hSet = new HashSet<String>();
		for (int i=0; i<7; i++){
			int limit = i;
			int x = limit, y = 0, z = 0;
			while(y != limit || z != limit){
				println (x,y,z);
				println (z,x,y);
				println (y,z,x);
				
				if (y != z && x != y){ //Reflectable
					println (z,y,x);
					println (y,x,z);
					println (x,z,y);
				}
				
				if (y == z){
					y += 1;
					z = 0;
				} else {
					z += 1;
				}
			}
			println (x,y,z);
		}
		println (cnt);
		println (hSet.size());
	}
	
	public static void main(String[] args) throws Exception{
		int[] point1 = new int[]{0,0};
		int[] point2 = new int[]{0,0};
		int[] point3 = new int[]{0,0};
		TripleSpiral ts = new TripleSpiral (point1, point2, point3);
		for (int i=0; i<100; i++){
			println(ts.nextItem());
		}
		ts.finished();
	}
	*/
	
}
