package main;

import java.io.IOException;

import javax.swing.JFrame;

public class Chess {
	
	static JFrame window = new JFrame();
	static Visual visual = new Visual();
	
	public static void main(String[] args) throws IOException {
		Chess.setup();
	}
	
	static void setup() throws IOException {
		window.dispose(); window = new JFrame();
		window.add(visual);
		window.addMouseListener(visual);
		window.addKeyListener(visual);
		
		Chessboard.setup();
		
		window.setSize(1000, 1037);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
	static String[] toString(Object[] arg) {
		String[] strs = new String[arg.length];
		for (int i = 0; i < arg.length; i++) {
			if (arg[i] == null) {
				strs[i] = null;
			} else {
				strs[i] = arg[i].toString();
			}
		}
		return strs;
	}
}