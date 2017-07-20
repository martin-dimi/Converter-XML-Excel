package application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import xmlToExcel_withTree.Converter;

public class GUI extends JPanel {

	private static final long serialVersionUID = 1L;

	JButton button_Convert;
	JButton button_SelectFile;
	JButton button_Destination;

	File file;
	File destination;

	JLabel l1;
	JLabel l2;
	JLabel status;

	GUI() {
		this.setLayout(new BorderLayout());
		createButtons();

		// North
		JPanel north = new JPanel(new BorderLayout());
		JLabel header = new JLabel("CONVERTER");
		header.setFont(new Font("Britannic Bold", Font.BOLD, 40));
		header.setHorizontalAlignment(SwingConstants.LEFT);
		north.setBackground(Color.LIGHT_GRAY);
		north.add(header);
		this.add(north, BorderLayout.NORTH);
		//

		// West
		JPanel west = new JPanel(new GridLayout(6, 1, 0, 10));
		west.setBorder(new EmptyBorder(40, 10, 0, 10));
		west.setBackground(Color.GRAY);
		west.add(button_SelectFile);
		west.add(button_Destination);
		this.add(west, BorderLayout.WEST);
		//

		// Center
		JPanel center = new JPanel(new BorderLayout());
		JPanel center_Button = new JPanel();
		JPanel center_Text = new JPanel(new GridLayout(6, 1, 0, 10));
			
			// Dealing with label's text
			l1 = new JLabel("1. Select file");
			l2 = new JLabel("2. Select destination");
			JLabel l3 = new JLabel("3. Convert file");
			status = new JLabel("");
			l1.setForeground(Color.WHITE);
			l2.setForeground(Color.WHITE);
			l3.setForeground(Color.WHITE);
			status.setForeground(Color.WHITE);
			l1.setFont(new Font("Britannic", Font.PLAIN, 25));
			l2.setFont(new Font("Britannic", Font.PLAIN, 25));
			l3.setFont(new Font("Britannic", Font.PLAIN, 25));
			status.setFont(new Font("Serif", Font.PLAIN, 20));
			l1.setHorizontalAlignment(SwingConstants.CENTER);
			l2.setHorizontalAlignment(SwingConstants.CENTER);
			l3.setHorizontalAlignment(SwingConstants.CENTER);
			status.setHorizontalAlignment(SwingConstants.CENTER);
			//

		center.setBackground(Color.DARK_GRAY);
		center_Text.setBackground(Color.DARK_GRAY);

		center_Text.setBorder(new EmptyBorder(30, 0, 0, 0));
		center_Text.add(l1);
		center_Text.add(l2);
		center_Text.add(l3);
		center_Text.add(new JLabel(""));
		center_Text.add(status);

		center_Button.setBorder(new EmptyBorder(0,0,40,0));
		center_Button.setBackground(Color.DARK_GRAY);
		center_Button.add(button_Convert);

		center.add(center_Button, BorderLayout.SOUTH);
		center.add(center_Text, BorderLayout.CENTER);
		this.add(center);
		//
	}

	private void createButtons() {
		button_Convert = new JButton("Convert me");
		button_Convert.setFont(new Font("Britannic", Font.BOLD, 18));
		button_Convert.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (file != null && destination != null) {
					String extension = getFileExtension(file);
					switch (extension) {	//switch or if
					case "xml":
						Converter.writeToExcel(file, destination);
						break;
					case "xlsx":
						Converter.writeToXML(file, destination);
						break;
					}
					if(Converter.completed) {
						status.setText("File converted");
						status.setForeground(Color.GREEN);
						l1.setForeground(Color.WHITE);
						l2.setForeground(Color.WHITE);
						l1.setText("1. Select file");
						l2.setText("2. Select destination");
					}
					else {
						status.setText("File NOT converted, ERROR");
						status.setForeground(Color.red);
					}
				} else {
					if (file == null)
						l1.setForeground(Color.red);
					if (destination == null)
						l2.setForeground(Color.red);
				}
			}
		});

		button_SelectFile = new JButton("File");
		button_SelectFile.setFont(new Font("Britannic", Font.BOLD, 20));
		button_SelectFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int result = chooser.showOpenDialog(button_Convert);

				if (result == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile();
					l1.setText("File selected");
					l1.setForeground(Color.green);
					destination = file.getParentFile();

					String extension = getFileExtension(file);
					switch (extension) {
					case "xml":
						status.setText("XML ==> EXCEL");
						break;
					case "xlsx":
						status.setText("EXCEL ==> XML");
						break;
					}
				}
			}
		});

		button_Destination = new JButton("Destination");
		button_Destination.setFont(new Font("Britannic", Font.BOLD, 20));
		button_Destination.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = chooser.showOpenDialog(button_Convert);

				if (result == JFileChooser.APPROVE_OPTION) {
					destination = chooser.getSelectedFile();
					l2.setText("Destination selected");
					l2.setForeground(Color.green);
				}
			}

		});
	}

	private String getFileExtension(File file) {
		String fileExtension = "";
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != 0)
			fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
		return fileExtension;
	}

}
