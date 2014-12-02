package org.tigerdynasty.scouting;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class QueueCheckerThread implements Runnable {
	public QueueCheckerThread() {
		// GEORGE HERE
		// SYNTAX OF STUFF SENT TO SERVER/SHOULD BE READ FROM TEXT FILE:
		// <Team name> <auton|shooting|assist|defend> <rating out of 10>
		// <optional comments, may include spaces>
		// or
		// <Team name> <code 1: 0-> 0 ball auton, 1-> 1 ball auton, 2 -> 2 ball
		// auton, 3 -> truss> <code 2: times made,0,1,or2>
	}

	@Override
	public void run() {
		while (true) {
			Scanner scan = null;
			if (!RobotDatabase.bbQ.isEmpty()) {
				String fullLine;
				try {
					scan = new Scanner(RobotDatabase.allText);
					fullLine = RobotDatabase.bbQ.take().toString();
					System.out.println(fullLine);
					ServerMain.console.append("Received string from user: "
							+ fullLine);
					updateTextFiles(fullLine);
					for (int line = 0; scan.hasNextLine(); ++line) {
						fullLine = scan.nextLine();
						String parsedComment;
						for (int i = 0; i < RobotDatabase.teamAmount; ++i) {
							System.out.println(fullLine);
							if (!fullLine.equals("") && fullLine.substring(0, 5).equals(
									"t" + RobotDatabase.teamNum[i])) {
								System.out.println(RobotDatabase.teamNum[i]);
								if (fullLine.contains("c")) {
									ServerMain.console
											.append("Comment detected.");
									parsedComment = fullLine.substring(1);
									RobotDatabase.comment[i].add(parsedComment);
								} else {
									ServerMain.console.append("Computing...");
									int s = fullLine.indexOf("s");
									int parsedS = Integer.parseInt(fullLine
											.substring(s + 1, s + 2).trim());
									int u = fullLine.indexOf("u");
									int parsedAu = Integer.parseInt(fullLine
											.substring(u + 1, u + 2).trim());
									int a = fullLine.indexOf("a");
									int parsedAs = Integer.parseInt(fullLine
											.substring(a + 1, a + 2).trim());
									int d = fullLine.indexOf("d");
									int parsedD = Integer.parseInt(fullLine
											.substring(d + 1, d + 2).trim());
									ServerMain.console
											.append("Info is about: Team "
													+ RobotDatabase.teamNum[i]
													+ "\n");
									RobotDatabase.auton[i].add(parsedAu);
									TeamCalculations.calcAuton();
									RobotDatabase.shoot[i].add(parsedS);
									TeamCalculations.calcShoot();
									RobotDatabase.assist[i].add(parsedAs);
									TeamCalculations.calcAssist();
									RobotDatabase.defend[i].add(parsedD);
									TeamCalculations.calcDefend();
								}
							}
						}
					}
				} catch (InterruptedException | FileNotFoundException e) {
				}
			}
		}
	}

	public void updateTextFiles(String fullLine) {
		BufferedWriter line = null;
		try {
			line = new BufferedWriter(new FileWriter(RobotDatabase.allText,
					true));
			line.newLine();
			line.append(fullLine);
			line.close();
		} catch (Exception e) {

		}

		for (int c = 0; c < RobotDatabase.fTArray.size(); ++c) {
			RobotDatabase.fTArray.get(c).queue();
			System.out.println("QUEUED" + c);
		}

	}

}
