package org.tigerdynasty.scouting;

import java.awt.Container;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ServerMain {
	static ServerMain SM = new ServerMain();
	JFrame frame;
	JPanel panel;
	Container contain;
	PrintWriter pW;
	BufferedReader bR;
	ServerSocket Ss;
	Socket client;

	public static void main(String[] args) {
		SM.init();

	}

	public void init() {
		Dimension screenDim = new Dimension(700, 600);
		try {
			Ss = new ServerSocket(5010);
			System.out.println("Started server on 5010!");
		} catch (IOException e) {
		}
		client = new Socket();
		frame = new JFrame("Scouting Server");
		frame.setSize(screenDim);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();
		contain = frame.getContentPane();
		contain.add(panel);
		frame.setVisible(true);
		constantChecker();
	}

	public void constantChecker() {
		
		while (true) {
			try {
				client = Ss.accept();
				System.out.println("NEW CONNECTION!");
			} catch (IOException e) {
			}

			ClientConnectionQueue c = new ClientConnectionQueue(client);
			Thread t = new Thread(c);
			t.start();
		}
	}
}
