package org.tigerdynasty.scouting;

import java.io.File;

public class ClientDatabase {
	public static  int teamAmount = 40;
	public static String[] teamNum = new String[teamAmount], teamName = new String[teamAmount];
	public static int[] overAllRating = new int[teamAmount];
	public static JMenuManager[] team = new JMenuManager[ClientDatabase.teamAmount];
	public static int[] avgAuton = new int[teamAmount];
	public static int[] avgShoot = new int[teamAmount];
	public static int[] avgAssist = new int[teamAmount];
	public static int[] avgDefend = new int[teamAmount];
	public static int selected = 0;
	public static File allText = new File("resources/allText.txt");
}
