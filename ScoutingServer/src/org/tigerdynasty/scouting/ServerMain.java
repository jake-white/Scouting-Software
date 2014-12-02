package org.tigerdynasty.scouting;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class ServerMain implements ActionListener {
	static ServerMain SM = new ServerMain();
	JFrame frame;
	JPanel panel, scrollPanel;
	Container contain;
	PrintWriter pW;
	BufferedReader bR;
	ServerSocket Ss;
	Socket client;
	InputManager input = new InputManager();
	TeamCalculations tC;
	FileTransfer fT;
	int i = 0;

	BoxLayout BL;
	JScrollPane scrollPaneLeft;
	JMenuManager[] team = new JMenuManager[RobotDatabase.teamAmount];
	static JTextArea text = new JTextArea();
	static JTextArea console = new JTextArea();
	public final int portNumber = 5010;
	public static int selected;
	public Timer mainUIThread = new Timer(15, new mainUIThread());
	public int mouseX = 0, mouseY = 0;

	public static void main(String[] args) {
		SM.init();

	}

	public void init() {
		readRobotDatabase();
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();

		client = new Socket();
		frame = new JFrame("Scouting Server");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();
		contain = frame.getContentPane();
		contain.add(panel);
		scrollPanel = new JPanel();
		BL = new BoxLayout(scrollPanel, BoxLayout.Y_AXIS);
		scrollPanel.setLayout(BL);
		JScrollPane consolePane = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneLeft = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneLeft.setPreferredSize(new Dimension(500, 500));
		scrollPaneLeft.setAutoscrolls(true);
		scrollPaneLeft.setVisible(true);
		Dimension menuSize = new Dimension(500, 30);
		Random rand = new Random();
		for (int i = 0; i < RobotDatabase.teamAmount; ++i) {
			team[i] = new JMenuManager("Team" + i);
			team[i].setText("Team " + RobotDatabase.teamNum[i] + "Rating: "
					+ RobotDatabase.overAllRating[i]);
			team[i].setNumber(i, RobotDatabase.teamNum[i]);
			team[i].setPreferredSize(menuSize);
			team[i].setMinimumSize(team[i].getPreferredSize());
			team[i].setMaximumSize(team[i].getPreferredSize());
			team[i].addActionListener(this);
			scrollPanel.add(team[i]);
		}
		scrollPaneLeft.getViewport().add(scrollPanel);
		Dimension d = new Dimension(400, 500);
		text.setPreferredSize(d);
		text.setVisible(true);
		text.setText("Select a team to view their stats.\n");
		text.setEditable(false);
		console.setPreferredSize(d);
		console.setVisible(true);
		console.append("Attempting to start server... \n");
		console.setEditable(false);
		consolePane.getViewport().add(console);
		consolePane.setPreferredSize(console.getPreferredSize());
		consolePane.setVisible(true);
		panel.add(scrollPaneLeft);
		panel.add(text);
		panel.add(consolePane);
		frame.setVisible(true);
		try {
			Ss = new ServerSocket(5010);
			console.append("Started server on port " + portNumber + "!\n");
			console.append("Listening for new client connections now...\n");
		} catch (IOException e) {
			console.append("Failed to start server on port " + portNumber
					+ "\n");
		}
		TeamCalculations.init();
		mainUIThread.start();
		QueueCheckerThread queue = new QueueCheckerThread();
		Thread q = new Thread(queue);
		q.start();
		constantChecker();
	}

	public void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[8192];
		int len = 0;
		while ((len = in.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
	}

	public void constantChecker() {

		while (true) {
			try {
				client = Ss.accept();
				console.append("NEW CONNECTION AT "
						+ client.getLocalSocketAddress() + "\n");
			} catch (IOException e) {
			}

			ClientConnectionQueue c = new ClientConnectionQueue(client);
			Thread t = new Thread(c);
			t.start();
			fT= new FileTransfer(client);
			RobotDatabase.fTArray.add(fT);
			System.out.println(RobotDatabase.fTArray.toString());
			fT.queue();
		}
	}

	public void readRobotDatabase() {
		Scanner scan = null, numScan = null;
		try {
			scan = new Scanner(new File("resources/teamNames.txt"));
			console.append("Read from file /resources/teamNames.txt successfully!\n");
		} catch (FileNotFoundException e) {
			console.append("Failed to read from file /resources/teamNames.txt...\n");
		}
		try {
			numScan = new Scanner(new File("resources/teamNums.txt"));
			console.append("Read from file /resources/teamNums.txt successfully!\n");
		} catch (FileNotFoundException e) {
			console.append("Failed to read from file /resources/teamNums.txt...\n");
		}
		for (int i = 0; i < RobotDatabase.teamAmount; ++i) {
			if (scan.hasNextLine() && numScan.hasNextLine()) {
				RobotDatabase.teamName[i] = scan.nextLine();
				RobotDatabase.teamNum[i] = numScan.nextLine();
			} else {
				RobotDatabase.teamName[i] = "Unknown";
				RobotDatabase.teamNum[i] = "0";
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		for (int i = 0; i < RobotDatabase.teamAmount; ++i) {
			if (arg0.getSource() == team[i]) {
				selected = i;
			}
		}

	}

	private class mainUIThread implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<JMenuManager> list = new ArrayList(
					RobotDatabase.teamAmount);
			for (int i = 0; i < RobotDatabase.teamAmount; ++i) {
				list.add(team[i]);
				Collections.sort(list, new Comparator<JMenuManager>() {
					@Override
					public int compare(JMenuManager p1, JMenuManager p2) {
						return RobotDatabase.overAllRating[p1.i]
								- RobotDatabase.overAllRating[p2.i];
					}
				});
			}
			Collections.reverse(list);
			for (int i = 0; i < RobotDatabase.teamAmount; ++i) {
				scrollPanel.remove(team[i]);
			}
			int counter = 1;
			for (int i = 0; i < RobotDatabase.teamAmount; ++i) {
				team[i].setText("Team " + RobotDatabase.teamNum[i] + " "
						+ RobotDatabase.teamName[i] + ", Rating: "
						+ RobotDatabase.overAllRating[i]);
				scrollPanel.add(list.get(i));
			}
			mouseX = MouseInfo.getPointerInfo().getLocation().x - frame.getX();
			mouseY = MouseInfo.getPointerInfo().getLocation().y - frame.getY();
			for (int i = 0; i < RobotDatabase.teamAmount; ++i) {
				if (selected == i) {
					text.setText("Info about team " + RobotDatabase.teamNum[i]
							+ ":\n" + "Stats: \nOverall Rating: "
							+ RobotDatabase.overAllRating[i] + "\nShooting: "
							+ RobotDatabase.avgShoot[i] + "\nDefending: "
							+ RobotDatabase.avgDefend[i] + "\nAssisting: "
							+ RobotDatabase.avgAssist[i] + "\nAuton: "
							+ RobotDatabase.avgAuton[i]);
				}
			}

			// for (int i = 0; i < RobotDatabase.teamAmount; ++i) {
			// if (team[i].contains(mouseX, mouseY)) {
			// selected = i;
			// }
			// }
			scrollPanel.repaint();
			frame.repaint();
			scrollPaneLeft.repaint();
			for (int i = 0; i < RobotDatabase.teamAmount; ++i) {
				team[i].repaint();
			}
		}
	}
}
