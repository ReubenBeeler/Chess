package main;

import java.util.ArrayList;
import java.util.Stack;

import lib.Chessman;

public final class History {
	
	private History () {}
	
	private static Stack<Chessman[][]> chessboard_backups = new Stack<>();
	private static Stack<ArrayList<Chessman>> removed_backups = new Stack<>();
	
	private static int redos = 0;
	private static Stack<Chessman[][]> chessboard_redos = new Stack<>();
	private static Stack<ArrayList<Chessman>> removed_redos= new Stack<>();
	
	static void undo() {
		chessboard_redos.add(Chessboard.deepClone(Chessboard.chessboard));
		removed_redos.add(new ArrayList<>(Chessboard.removed));
		redos++;
		
		Chessboard.chessboard = chessboard_backups.pop();
		Chessboard.removed = removed_backups.pop();


		Chess.visual.selectedChessman = null;
		Chessboard.turn--;
		Chess.visual.repaint();
	}
	
	static void redo() {
		chessboard_backups.add(Chessboard.deepClone(Chessboard.chessboard));
		removed_backups.add(new ArrayList<>(Chessboard.removed));
		
		Chessboard.chessboard = chessboard_redos.pop();
		Chessboard.removed = removed_redos.pop();
		redos--;
		
		Chess.visual.selectedChessman = null;
		Chessboard.turn++;
		Chess.visual.repaint();
	}
	
	static void next() {
		chessboard_backups.add(Chessboard.deepClone(Chessboard.chessboard));
		removed_backups.add(new ArrayList<>(Chessboard.removed));
		
		chessboard_redos.clear();
		removed_redos.clear();
		redos = 0;
	}
	
	public static int getRedos() {return redos;}
}