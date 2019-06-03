package Gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				MyFrame win = new MyFrame();
				win.setVisible(true);
				win.setSize(win.getMyImage().getWidth(), win.getMyImage().getHeight());
				win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				

			}
		});

	}

}
