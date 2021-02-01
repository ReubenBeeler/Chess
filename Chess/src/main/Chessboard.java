package main;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import lib.Bishop;
import lib.Chessman;
import lib.King;
import lib.Knight;
import lib.Pawn;
import lib.Queen;
import lib.Rook;
import util.Location;

public class Chessboard {

	public static Chessman[][] chessboard = new Chessman[8][8];
	static ArrayList<Chessman> removed = new ArrayList<>();

	static int turn = 0;

	static void setup() throws IOException {
		place(new Rook(Color.BLACK, new Location(0, 7), true));
		place(new Knight(Color.BLACK, new Location(1, 7), true));
		place(new Bishop(Color.BLACK, new Location(2, 7), true));
		place(new Queen(Color.BLACK, new Location(3, 7), true));
		place(new King(Color.BLACK, new Location(4, 7), true));
		place(new Bishop(Color.BLACK, new Location(5, 7), true));
		place(new Knight(Color.BLACK, new Location(6, 7), true));
		place(new Rook(Color.BLACK, new Location(7, 7), true));

		for (int x = 0; x < 8; x++) {
			place(new Pawn(Color.BLACK, new Location(x, 6), true));
		}

		for (int x = 0; x < 8; x++) {
			place(new Pawn(Color.WHITE, new Location(x, 1), true));
		}

		place(new Rook(Color.WHITE, new Location(0, 0), true));
		place(new Knight(Color.WHITE, new Location(1, 0), true));
		place(new Bishop(Color.WHITE, new Location(2, 0), true));
		place(new Queen(Color.WHITE, new Location(3, 0), true));
		place(new King(Color.WHITE, new Location(4, 0), true));
		place(new Bishop(Color.WHITE, new Location(5, 0), true));
		place(new Knight(Color.WHITE, new Location(6, 0), true));
		place(new Rook(Color.WHITE, new Location(7, 0), true));
	}

	static void place(Chessman chessman) {
		chessboard[chessman.getLocation().getY()][chessman.getLocation().getX()] = chessman;
	}

	static void move(Chessman chessman, Location location) {
		chessboard[chessman.getLocation().getY()][chessman.getLocation().getX()] = null;
		removed.add(getChessman(location));
		chessman.setLocation(location);
		chessman.hasMoved = true;
		place(chessman);
	}

	public static Chessman getChessman(Location location) {
		return chessboard[location.getY()][location.getX()];
	}

	public static Chessman getChessman(int x, int y) {
		return chessboard[y][x];
	}

	public static Chessman[][] deepClone(Chessman[][] chessboard) {
		Chessman[][] newChessboard = new Chessman[8][8];
		try {
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (chessboard[y][x] != null) {
						newChessboard[y][x] = Chessboard.getChessman(x, y).deepClone();
						newChessboard[y][x].setLocation(new Location(newChessboard[y][x].getLocation()));
					}
				}
			}
		} catch (CloneNotSupportedException e) {
			System.out.println("CloneNotSupportedException in History.deepChessboardClone()");
			return null;
		}
		return newChessboard;
	}

	// @param in Chessman.moveableLocations(boolean removeChecks) specifically for check(?) methods
	//     removeChecks false to avoid infinite looping
	static ArrayList<King> check() {
		return check(chessboard);
	}
	
	public static ArrayList<King> check(Chessman[][] chessboard) {
		ArrayList<King> allKings = new ArrayList<>();
		ArrayList<King> checkedKings = new ArrayList<>();
		for (Chessman[] row : chessboard) {
			for (Chessman chessman : row) {
				if (chessman != null && chessman instanceof King) { allKings.add((King) chessman); }
			}
		}
		for (King king : allKings) {
			for (Chessman[] row : chessboard) {
				for (Chessman chessman : row) {
					if (chessman != null) {
						if (!king.getColor().equals(chessman.getColor())) {
							if (new ArrayList<String>(Arrays.asList(Chess.toString(chessman.moveableLocations(false).toArray()))).contains(king.getLocation().toString())) {
								checkedKings.add(king);
							}
						}
					}
				}
			}
		}
		return checkedKings;
	}

	static ArrayList<King> checkmate() {
		ArrayList<King> checkedKings = check();
		ArrayList<King> checkmatedKings = new ArrayList<>();
		for (King king : checkedKings) {
			if (king.moveableLocations(true).isEmpty()) {
				for (Chessman[] row : chessboard) {
					for (Chessman chessman : row) {
						if (chessman == null) { continue; }
						if (chessman.getColor().equals(king.getColor()) && chessman.moveableLocations(true).isEmpty()) {
							checkmatedKings.add(king);
						}
					}
				}
			}
		}
		return checkmatedKings;
	}

	static boolean stalemate() {
		for (Chessman[] row : chessboard) {
			for (Chessman chessman : row) {
				if (chessman != null && chessman.getColor() == ((turn % 2 == 0) ? Color.WHITE : Color.BLACK) && !chessman.moveableLocations(true).isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static Location[] castle(King king) {
		if (king.hasMoved || check().contains(king)) return new Location[] {};
		ArrayList<Location> castleLocations = new ArrayList<>();
		int y = king.getLocation().getY();
		for (Chessman[] row : chessboard) {
			for (Chessman chessman : row) {
				if (chessman != null && chessman instanceof Rook && chessman.getColor().equals(king.getColor()) && !chessman.hasMoved) {
					boolean bad = false;
					if (chessman.getLocation().getX() == 0) {
						for (int x = 1; x <= 3; x++) {
							Location location = new Location(x, y);
							if (getChessman(location) != null || inEnemyRange(king.getColor(), location)) bad = true;
						}
						if (!bad) castleLocations.add(new Location(2, y));
					} else {
						for (int x = 5; x <= 6; x++) {
							Location location = new Location(x, y);
							if (getChessman(location) != null || inEnemyRange(king.getColor(), location)) bad = true;
						}
						if (!bad) castleLocations.add(new Location(6, y));
					}
				}
			}
		}
		return (Location[]) Arrays.copyOf(castleLocations.toArray(), castleLocations.size(), Location[].class);
	}
	
	private static boolean inEnemyRange(Color color, Location location) {
		for (Chessman[] row : chessboard) {
			for (Chessman chessman : row) {
				if (chessman == null || chessman.getColor().equals(color)) continue;
				if (new ArrayList<>(Arrays.asList(Chess.toString(chessman.moveableLocations(false).toArray()))).contains(location.toString())) return true;
			}
		}
		return false;
	}
}