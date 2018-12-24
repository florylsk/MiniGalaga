package edu.uc3m.principal;

public class Conf {
	public static final int WIDTH = 17;
	public static final int HEIGHT = 22;

	public static final int DIR_N = 0;
	public static final int DIR_NNE = 1;
	public static final int DIR_NE = 2;
	public static final int DIR_ENE = 3;
	public static final int DIR_E = 4;
	public static final int DIR_ESE = 5;
	public static final int DIR_SE = 6;
	public static final int DIR_SSE = 7;
	public static final int DIR_S = 8;
	public static final int DIR_SSW = 9;
	public static final int DIR_SW = 10;
	public static final int DIR_WSW = 11;
	public static final int DIR_W = 12;
	public static final int DIR_WNW = 13;
	public static final int DIR_NW = 14;
	public static final int DIR_NNW = 15;
	public final static int[][] Moves = { 
	 { 0, -4 }, // DIR_N      0 
	 { 1, -4 },  // DIR_NNE   1
	 { 3, -3 },  // DIR_NE    2
	 { 4, -1 },  // DIR_ENE   3 
	 { 4,  0 },  // DIR_E     4
	 { 4,  1 },  // DIR_ESE   5
	 { 3,  3 },  // DIR_SE    6
	 { 1,  4 },  // DIR_SSE   7 
	 { 0,  4 },  // DIR_S     8 
	 { -1, 4 },  // DIR_SSW   9
	 { -3, 3 },  // DIR_SW    10
	 { -4, 1 },  // DIR_WSW   11
	 { -4, 0 },  // DIR_W     12
	 { -4,-1 },  // DIR_WNW   13
	 { -3,-3 },  // DIR_NW    14
	 { -1,-4 },  // DIR_NNW   15
	
	 };
}
