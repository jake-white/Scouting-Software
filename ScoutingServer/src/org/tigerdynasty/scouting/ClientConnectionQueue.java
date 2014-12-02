package org.tigerdynasty.scouting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Set;

public class ClientConnectionQueue implements Runnable {
	Socket client;
	BufferedReader bR;

	public ClientConnectionQueue(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		while (true) {
			try {
				bR = new BufferedReader(new InputStreamReader(
						client.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			String line;
			try {

				line = bR.readLine();
				RobotDatabase.bbQ.put(line + "\n");

			} catch (IOException | InterruptedException e) {
				ServerMain.console.append("CLIENT DISCONNECTED!\n");
				return;
			}
		}

	}
}
