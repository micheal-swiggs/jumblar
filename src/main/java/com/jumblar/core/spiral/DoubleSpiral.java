// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.spiral;

public class DoubleSpiral {
	
	
	int[] point1;
	int[] point2;
	int[][] points;
	int limit = 0;
	int j = 0;
	int[][] buf1 = null;
	int[][] buf2 = null;
	
	public DoubleSpiral (int[] point1, int[] point2){
		this.point1 = point1;
		this.point2 = point2;
		this.points =  SingleSpiral.getPoints();
	}
	
	public int[][] nextItem(){
		int[][] item = nextItemWrapper();
		return new int[][]{
				{item[0][0] + point1[0], item[0][1] + point1[1]},
				{item[1][0] + point2[0], item[1][1] + point2[1]}
		};
	}
	
	public int[][] nextItemWrapper(){
		if(buf1 == null && buf2 == null){
			j++;
			if(j<limit){
				buf1 = new int[][]{ points[limit], points[j]};
				buf2 = new int[][]{ points[j], points[limit]};
			} else {
				buf1 = new int[][]{ points[limit], points[limit]};
				limit++;
				j=-1;
			}
		}
		int[][] result;
		if (buf1 != null){
			result = buf1;
			buf1 = null;
		} else {
			result = buf2;
			buf2 = null;
		}
		return result;
	}
	
	/**
	public static void main(String[] args){
		
		for (int limit = 0; limit<7;limit++){
			for (int j=0;j<limit;j++){
				println (limit+","+j);
				println (j+","+limit);
			}
			println (limit+","+limit);
		}
	}
	
	public static int[][] getPoints(){
		int[][] result = new int[10][2];
		for (int i=0;i< result.length; i++){
			result[i][0] = i;
			result[i][1] = i;
		}
		return result;
	}
	
	public static void main1(){
		int[] point1 = new int[]{0,0};
		int[] point2 = new int[]{0,0};
		
		DoubleSpiral ds = new DoubleSpiral(point1, point2);
		for(int i=0;i<20;i++){
			int[][] item = ds.nextItem();
			String b = item[0][0]+","+item[0][1]+"  "+item[1][0]+","+item[1][1];
			println (b);
		}
	}
	
	**/
}
