package org.tigerdynasty.scouting;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class RobotDatabase {
	public final static int teamAmount = 64;
	public static String[] teamNum = new String[teamAmount], teamName = new String[teamAmount];
	public static int[] overAllRating = new int[teamAmount];
	static ArrayList<Integer>[] shoot = (ArrayList<Integer>[])new ArrayList[teamAmount];
	public static ArrayList<Integer>[] defend = (ArrayList<Integer>[])new ArrayList[teamAmount];
	public static ArrayList<Integer>[] assist = (ArrayList<Integer>[])new ArrayList[teamAmount];
	public static ArrayList<Integer>[] auton = (ArrayList<Integer>[])new ArrayList[teamAmount];
	public static int[] avgShoot = new int[teamAmount];
	public static int[] avgDefend = new int[teamAmount];
	public static int[] avgAssist = new int[teamAmount];
	public static int[] avgAuton = new int[teamAmount];
	public static LinkedBlockingQueue<String> bbQ = new LinkedBlockingQueue<String>();
}
