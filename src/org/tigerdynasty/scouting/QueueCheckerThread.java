package org.tigerdynasty.scouting;

public class QueueCheckerThread implements Runnable {
	public QueueCheckerThread() {

	}

	@Override
	public void run() {
		while (true) {
			if (!MapManager.bbQ.isEmpty()) {
				String fullLine;
				try {
					fullLine = MapManager.bbQ.take().toString();
					System.out.println(fullLine);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
