package org.tigerdynasty.scouting;

import java.util.ArrayList;

public class TeamCalculations {
	static int totalAmount = 0;

	public static void calcShoot() {
		for (int i = 0; i < RobotDatabase.teamAmount; ++i) { // unfinished

			for (int a = 0; a < RobotDatabase.shoot[i].size(); ++a) {
				totalAmount += RobotDatabase.shoot[i].get(a);
			}
			if (RobotDatabase.shoot[i].size() > 0) {
				int avgS = (int) Math.round(totalAmount
						/ RobotDatabase.shoot[i].size());
				RobotDatabase.avgShoot[i] = avgS;
			}
		}
	}

	public static void calcDefend() {
		for (int i = 0; i < RobotDatabase.teamAmount; ++i) {

			for (int a = 0; a < RobotDatabase.defend[i].size(); ++a) {
				totalAmount += RobotDatabase.defend[i].get(a);
			}
			if (RobotDatabase.defend[i].size() > 0) {
				int avgD = (int) Math.round(totalAmount
						/ RobotDatabase.defend[i].size());
				RobotDatabase.avgDefend[i] = avgD;
			}
		}
	}

	public static void calcAssist() {
		for (int i = 0; i < RobotDatabase.teamAmount; ++i) {
			for (int a = 0; a < RobotDatabase.assist[i].size(); ++a) {
				totalAmount += RobotDatabase.assist[i].get(a);
			}
			if (RobotDatabase.assist[i].size() > 0) {
				int avgAs = (int) Math.round(totalAmount
						/ RobotDatabase.assist[i].size());
				RobotDatabase.avgAssist[i] = avgAs;
			}
		}
	}

	public static void calcAuton() {
		for (int i = 0; i < RobotDatabase.teamAmount; ++i) {
			for (int a = 0; a < RobotDatabase.auton[i].size(); ++a) {
				totalAmount += RobotDatabase.auton[i].get(a);
			}
			if (RobotDatabase.auton[i].size() > 0) {
				int avgAu = (int) Math.round(totalAmount
						/ RobotDatabase.auton[i].size());
				RobotDatabase.avgAuton[i] = avgAu;
			}
		}
	}

	public static void init() {
		for (int i = 0; i < RobotDatabase.teamAmount; ++i) {
			RobotDatabase.shoot[i] = new ArrayList<Integer>();
			RobotDatabase.defend[i] = new ArrayList<Integer>();
			RobotDatabase.assist[i] = new ArrayList<Integer>();
			RobotDatabase.auton[i] = new ArrayList<Integer>();
		}
	}
}
