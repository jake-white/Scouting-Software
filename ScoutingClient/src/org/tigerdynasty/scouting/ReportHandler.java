package org.tigerdynasty.scouting;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ReportHandler {

	public void newReport(JFrame frame, String currentTeam) {
		GridLayout grid = new GridLayout(2,5);
		String[] list = { "1", "2", "3", "4", "5" , "6" , "7" , "8", "9" , "10"};
		JComboBox team = new JComboBox(ClientDatabase.teamNum);
		JComboBox auton = new JComboBox(list);
		JComboBox shoot = new JComboBox(list);
		JComboBox assist = new JComboBox(list);
		JComboBox defend = new JComboBox(list);
		JLabel teamL = new JLabel("Team Number");
		JLabel autonL = new JLabel("Auton");
		JLabel shootL = new JLabel("Shooting");
		JLabel assistL = new JLabel("Assisting");
		JLabel defendL = new JLabel("Defense");
		JLabel commentL = new JLabel("Comment: ");
		JTextField comment = new JTextField();
		JPanel allComponents = new JPanel();
		allComponents.setLayout(grid);
		allComponents.add(teamL);
		allComponents.add(autonL);
		allComponents.add(shootL);
		allComponents.add(assistL);
		allComponents.add(defendL);
		allComponents.add(team);
		allComponents.add(auton);
		allComponents.add(shoot);
		allComponents.add(assist);
		allComponents.add(defend);
//		allComponents.add(commentL);
//		allComponents.add(comment);
		JOptionPane.showMessageDialog(null, allComponents, "Team Rating",
				JOptionPane.QUESTION_MESSAGE);
		int teamNum = Integer.parseInt(team.getSelectedItem().toString().trim());
		int autonNum = Integer.parseInt(auton.getSelectedItem().toString().trim());
		int shootNum = Integer.parseInt(shoot.getSelectedItem().toString().trim());
		int assistNum = Integer.parseInt(assist.getSelectedItem().toString().trim());
		int defendNum = Integer.parseInt(defend.getSelectedItem().toString().trim());
		// Integer[] array = {1,2,3,4,5,6,7,8,9,10};
		// JComboBox<Integer> combo = new JComboBox<Integer>(array);
		// JOptionPane.showInputDialog("Select shit", "New Report", combo);
		System.out.println(teamNum + " " + autonNum + " " + shootNum + " " + assistNum + " " + defendNum);
		String fullString = ("t" + teamNum + "u" + autonNum + "s" + shootNum + "a" + assistNum + "d" + defendNum);
		PrintWriter out;
        try {
			out = new PrintWriter(ClientMain.server.getOutputStream(), true);
			out.println(fullString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
