package org.tigerdynasty.scouting;

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
			if (!RobotDatabase.bbQ.isEmpty()) {
				String fullLine;
				try {
					fullLine = RobotDatabase.bbQ.take().toString();
					System.out.println(fullLine);
					ServerMain.console.append("Received string from user: "
							+ fullLine + "\n");
					String parsedComment;
					for (int i = 1; i < RobotDatabase.teamAmount; ++i) {
						if (fullLine.substring(0, 4).contains(
								RobotDatabase.teamNum[i])) {
							ServerMain.console.append("Computing...");
							int colon = fullLine.indexOf(":");
							int parsedNumber = Integer.parseInt(fullLine.substring(
									colon + 1).trim());
							ServerMain.console.append("Info is about: Team " + fullLine.substring(0,4) + "\n");
							System.out.println("parsedNumber = " + parsedNumber);
							if (fullLine.contains("shooting")) {
								RobotDatabase.shoot[i].add(parsedNumber);
								TeamCalculations.calcShoot();
							} else if (fullLine.contains("assist")) {
								RobotDatabase.assist[i].add(parsedNumber);
								TeamCalculations.calcAssist();
							} else if (fullLine.contains("defend")) {
								RobotDatabase.defend[i].add(parsedNumber);
								TeamCalculations.calcDefend();
							} else if (fullLine.contains("auton"))

							{
								RobotDatabase.auton[i].add(parsedNumber);
								TeamCalculations.calcAuton();
							}
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
