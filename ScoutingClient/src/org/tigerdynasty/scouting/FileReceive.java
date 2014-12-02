package org.tigerdynasty.scouting;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class FileReceive implements Runnable {
	Socket server;

	public FileReceive(Socket server) {
		this.server = server;
	}

	public void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[8192];
		int len = 0;
		PrintWriter pW = new PrintWriter(ClientDatabase.allText);
		pW.write("");
		pW.close();
		while ((len = in.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
		out.close();
	}

	@Override
	public void run() {
		while (true) {
			try {
				InputStream in = server.getInputStream();
				ClientMain.console.append("CONNECTED TO FILESERVER.\n");
				OutputStream out = new FileOutputStream(ClientDatabase.allText);
				ClientMain.console.append("READING FILE.\n");
				copy(in, out);
				ClientMain.console.append("COMPLETED READING FILE.\n");
			} catch (Exception e) {

			}
		}
	}

}
