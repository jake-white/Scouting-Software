package org.tigerdynasty.scouting;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class ClientMain implements ActionListener {
	ReportHandler RH = new ReportHandler();
	public JFrame frame;
	public JPanel panel, scrollPanel;
	public Container contain;
	public JScrollPane scrollPaneLeft;
	static ClientMain CM = new ClientMain();
	Timer timer = new Timer(15, new timer());
	public JTextArea text;
	public static JTextArea console;
	static Socket server;
	public String hostName = "localhost";
	public int portNumber = 5010;
	public int mouseX, mouseY;
	BoxLayout BL;
	JRadioButton radio = new JRadioButton();
	JButton newReport = new JButton("New Report");
	JButton newComment = new JButton("New Comment");

	public static void main(String[] args) {
		CM.init();
	}

	public void init() {
		newReport.addActionListener(this);
		newComment.addActionListener(this);
		frame = new JFrame("Server Client");
		panel = new JPanel();
		text = new JTextArea();
		text.setText("Team info goes here");
		console = new JTextArea();
		Dimension screenSize = new Dimension(Toolkit.getDefaultToolkit()
				.getScreenSize());
		int width = ((int) Math.round(screenSize.getWidth() / 3));
		int height = ((int) Math.round(screenSize.getHeight() / 2));
		JPanel buttonPanel = new JPanel();
		Dimension d = new Dimension(width, height);
		Dimension button = new Dimension(
				(int) Math.round(screenSize.getWidth() / 10),
				(int) Math.round(screenSize.getHeight() / 20));
		newReport.setMinimumSize(button);
		newComment.setMinimumSize(button);
		newReport.setMaximumSize(button);
		newComment.setMaximumSize(button);
		text.setPreferredSize(d);
		console.setPreferredSize(d);
		text.setEditable(false);
		console.setEditable(false);
		scrollPanel = new JPanel();
		contain = frame.getContentPane();
		contain.add(panel);
		JScrollPane consolePane = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneLeft = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneLeft.setPreferredSize(new Dimension((int) Math.round(d
				.getWidth() * .9), (int) Math.round(d.getHeight())));
		consolePane.getViewport().add(console);
		consolePane.setPreferredSize(console.getPreferredSize());
		consolePane.setVisible(true);
		

		panel.add(scrollPaneLeft);
		panel.add(text);
		panel.add(consolePane);
		panel.add(buttonPanel);
		BoxLayout BL = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
		buttonPanel.setLayout(BL);
		buttonPanel.add(newReport);
		buttonPanel.add(newComment);
		scrollPaneLeft.getViewport().add(scrollPanel);
		scrollPaneLeft.setAutoscrolls(true);
		scrollPaneLeft.setVisible(true);
		BL = new BoxLayout(scrollPanel, BoxLayout.Y_AXIS);
		scrollPanel.setLayout(BL);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		scrollPaneLeft.setVisible(true);
		scrollPanel.setVisible(true);
		frame.setVisible(true);
		Random rand = new Random();
		for (int i = 0; i < ClientDatabase.teamAmount; ++i) {
			ClientDatabase.team[i] = new JMenuManager("Team "
					+ ClientDatabase.teamNum[i] + " "
					+ ClientDatabase.teamName[i]);
			ClientDatabase.team[i].setNumber(i, ClientDatabase.teamNum[i]);
			scrollPanel.add(ClientDatabase.team[i]);
			ClientDatabase.team[i].addActionListener(this);
		}
		timer.start();
		readClientDatabase();
		startConnecting();
	}

	public void readClientDatabase() {
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
		for (int i = 0; i < ClientDatabase.teamAmount; ++i) {
			if (scan.hasNextLine() && numScan.hasNextLine()) {
				ClientDatabase.teamName[i] = scan.nextLine();
				ClientDatabase.teamNum[i] = numScan.nextLine();
			} else {
				ClientDatabase.teamName[i] = "Unknown";
				ClientDatabase.teamNum[i] = "0";
			}
		}
	}

	public void startConnecting() {
		hostName = JOptionPane.showInputDialog("Enter an IP", hostName);
		try {
			server = new Socket(hostName, portNumber);
			console.append("Connected to " + server.getInetAddress() + ":"
					+ server.getPort());
		} catch (IOException e) {
			console.append("Failed to connect to " + hostName + ":"
					+ portNumber);
			return;
		}
		FileReceive fC = new FileReceive(server);
		Thread f = new Thread(fC);
		f.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < ClientDatabase.teamAmount; ++i) {
			if (e.getSource() == ClientDatabase.team[i]) {
				ClientDatabase.selected = i;
			}
		}
		if (e.getSource() == newReport) {
			RH.newReport(frame, ClientDatabase.teamNum[ClientDatabase.selected]);
		}

	}

	private class timer implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			List<JMenuManager> list = new ArrayList(ClientDatabase.teamAmount);
			for (int i = 0; i < ClientDatabase.teamAmount; ++i) {
				list.add(ClientDatabase.team[i]);
				Collections.sort(list, new Comparator<JMenuManager>() {
					@Override
					public int compare(JMenuManager p1, JMenuManager p2) {
						return ClientDatabase.overAllRating[p1.i]
								- ClientDatabase.overAllRating[p2.i];
					}
				});
			}
			Collections.reverse(list);
			for (int i = 0; i < ClientDatabase.teamAmount; ++i) {
				scrollPanel.remove(ClientDatabase.team[i]);
			}
			int counter = 1;
			for (int i = 0; i < ClientDatabase.teamAmount; ++i) {
				ClientDatabase.team[i].setText("Team "
						+ ClientDatabase.teamNum[i] + " "
						+ ClientDatabase.teamName[i] + ", Rating: "
						+ ClientDatabase.overAllRating[i]);
				scrollPanel.add(list.get(i));
			}
			mouseX = MouseInfo.getPointerInfo().getLocation().x - frame.getX();
			// RYUSUKE WAS HERE //
			mouseY = MouseInfo.getPointerInfo().getLocation().y - frame.getY();
			for (int i = 0; i < ClientDatabase.teamAmount; ++i) {
				if (ClientDatabase.selected == i) {
					text.setText("Info about team " + ClientDatabase.teamNum[i]
							+ ":\n" + "Stats: \nOverall Rating: "
							+ ClientDatabase.overAllRating[i] + "\nShooting: "
							+ ClientDatabase.avgShoot[i] + "\nDefending: "
							+ ClientDatabase.avgDefend[i] + "\nAssisting: "
							+ ClientDatabase.avgAssist[i] + "\nAuton: "
							+ ClientDatabase.avgAuton[i]);
				}
			}
			scrollPanel.repaint();
			frame.repaint();
			scrollPaneLeft.repaint();
			for (int i = 0; i < ClientDatabase.teamAmount; ++i) {
				ClientDatabase.team[i].repaint();
			}

		}
	}

}
