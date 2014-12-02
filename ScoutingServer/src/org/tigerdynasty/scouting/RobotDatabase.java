package org.tigerdynasty.scouting;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

public class RobotDatabase {
	public final static int teamAmount = 40;
	public static String[] teamNum = new String[teamAmount],
			teamName = new String[teamAmount];
	public static int[] overAllRating = new int[teamAmount];
	static ArrayList<Integer>[] shoot = (ArrayList<Integer>[]) new ArrayList[teamAmount];
	public static ArrayList<Integer>[] defend = (ArrayList<Integer>[]) new ArrayList[teamAmount];
	public static ArrayList<Integer>[] assist = (ArrayList<Integer>[]) new ArrayList[teamAmount];
	public static ArrayList<Integer>[] auton = (ArrayList<Integer>[]) new ArrayList[teamAmount];
	public static ArrayList<String>[] comment = (ArrayList<String>[]) new ArrayList[teamAmount];
	public static int[] avgShoot = new int[teamAmount];
	public static int[] avgDefend = new int[teamAmount];
	public static int[] avgAssist = new int[teamAmount];
	public static int[] avgAuton = new int[teamAmount];
	public static LinkedBlockingQueue<String> bbQ = new LinkedBlockingQueue<String>();
	public static File allText = new File("resources/allText.txt");
	public static boolean queuedToRun;
	public static ThreadGroup tG = new ThreadGroup("Threads");
	public static ArrayList<FileTransfer> fTArray = new ArrayList<FileTransfer>();
}
