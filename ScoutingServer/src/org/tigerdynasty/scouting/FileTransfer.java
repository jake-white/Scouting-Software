package org.tigerdynasty.scouting;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class FileTransfer{
	Socket client;
	boolean queuedToRun;

	public FileTransfer(Socket client) {
		this.client = client;
	}

	public void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[8192];
		int len = 0;
		while ((len = in.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
		in.close();
		ServerMain.console.append("MOST LIKELY SUCCESSFUL!\n");
		queuedToRun = false;
	}

	public void queue() {
		queuedToRun = true;
		ServerMain.console.append("ATTEMPTING TO SEND TEXT FILE!\n");
		OutputStream out = null;
		InputStream in = null;
		try {
			in = new FileInputStream(RobotDatabase.allText);
		} catch (IOException e) {
			ServerMain.console.append("COULD NOT READ allText.txt!\n");
		}

		try {
			out = client.getOutputStream();
			copy(in, out);
		} catch (IOException e) {
			ServerMain.console.append("JUST QUIT TRYING TO SEND FILE.\n");
			RobotDatabase.fTArray.remove(this);
			return;
		}
	}
}
