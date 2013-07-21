// Copyright (C) 2013 - Micheal F Swiggs.  All rights reserved.
package com.jumblar.core.spiral;

import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SpiralTest extends TestCase{

	public SpiralTest(String testName){
		super(testName);
	}
	
	public static Test suite(){
		return new TestSuite (SpiralTest.class);
	}
	
	public void testDoubleSpiral() throws Exception{
		int[] point1 = new int[]{0,0};
		int[] point2 = new int[]{0,0};
		HashSet<String> hSet = new HashSet<String>();
		
		DoubleSpiral ds = new DoubleSpiral(point1, point2);
		int amt = 2000;
		for (int i=0; i<amt; i++){
			int[][] item = ds.nextItem();
			hSet.add(str(item));
		}
		assertEquals(hSet.size(), amt);
	}
	
	public static String str(int[][] item){
		return item[0][0]+","+item[0][1]+"  "+item[1][0]+","+item[1][1];
	}
	
	
	public static void println(int[][] item){
		if(item == null){
			println("null");
			return;
		}
		String b = str(item);
		println (b);
	}
	
	public static void println(Object ob){
		System.out.println(""+ob);
	}
}
