package application;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Main {

	public final static int X = 600;
	public final static int Y = 400;

	public static void main(String[] args) {

		GUI gui = new GUI();
		JFrame window = new JFrame("Converter");
		window.setSize(X, Y);
		//window.setResizable(false);
		window.setMinimumSize(new Dimension(X - 50, Y - 25));
		window.getContentPane().add(gui);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
