package lib;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import main.Chessboard;
import util.Location;

public final class Queen extends Chessman {
	
	@SuppressWarnings("serial")
	private static final HashMap<Color, String> IMAGES = new HashMap<>() {{put(Color.WHITE, "Graphics/Images/WhiteQueen.png"); put(Color.BLACK, "Graphics/Images/BlackQueen.png");}};
	
	public Queen(Color color, Location location, boolean original) throws IOException {
		super("Queen", color, location, ImageIO.read(new File(IMAGES.get(color))), 9, original);
	}

	public ArrayList<Location> moveableLocations(boolean removeChecks) {
		ArrayList<Location> locs = new ArrayList<>();
		// Look West
		boolean blocked = false;
		for (int x = getLocation().getX() - 1; x >= 0 && !blocked; x--) {
			Chessman chessman = Chessboard.getChessman(x, getLocation().getY());
			if (chessman == null) {
				locs.add(new Location(x, getLocation().getY()));
			} else if (chessman.getColor().equals(getColor())) {
				blocked = true;
			} else {
				locs.add(new Location(x, getLocation().getY()));
				blocked = true;
			}
		}
		// Look East
		blocked = false;
		for (int x = getLocation().getX() + 1; x < 8 && !blocked; x++) {
			Chessman chessman = Chessboard.getChessman(x, getLocation().getY());
			if (chessman == null) {
				locs.add(new Location(x, getLocation().getY()));
			} else if (chessman.getColor().equals(getColor())) {
				blocked = true;
			} else {
				locs.add(new Location(x, getLocation().getY()));
				blocked = true;
			}
		}
		//Look North
		blocked = false;
		for (int y = getLocation().getY() + 1; y < 8 && !blocked; y++) {
			Chessman chessman = Chessboard.getChessman(getLocation().getX(), y);
			if (chessman == null) {
				locs.add(new Location(getLocation().getX(), y));
			} else if (chessman.getColor().equals(getColor())) {
				blocked = true;
			} else {
				locs.add(new Location(getLocation().getX(), y));
				blocked = true;
			}
		}
		// Look South
		blocked = false;
		for (int y = getLocation().getY() - 1; y >= 0 && !blocked; y--) {
			Chessman chessman = Chessboard.getChessman(getLocation().getX(), y);
			if (chessman == null) {
				locs.add(new Location(getLocation().getX(), y));
			} else if (chessman.getColor().equals(getColor())) {
				blocked = true;
			} else {
				locs.add(new Location(getLocation().getX(), y));
				blocked = true;
			}
		}
		
		int x = getLocation().getX();
		int y = getLocation().getY();
		// Look Northwest
		blocked = false;
		for (int i = 1; i <= ((x < 7 - y) ? x : 7 - y) && !blocked; i++) {
			Chessman chessman = Chessboard.getChessman(x - i, y + i);
			if (chessman == null) {
				locs.add(new Location(x - i, y + i));
			} else if (chessman.getColor().equals(getColor())) {
				blocked = true;
			} else {
				locs.add(new Location(x - i, y + i));
				blocked = true;
			}
		}
		// Look Northeast
		blocked = false;
		for (int i = 1; i <= ((7 - x < 7 - y) ? 7 - x : 7 - y) && !blocked; i++) {
			Chessman chessman = Chessboard.getChessman(x + i, y + i);
			if (chessman == null) {
				locs.add(new Location(x + i, y + i));
			} else if (chessman.getColor().equals(getColor())) {
				blocked = true;
			} else {
				locs.add(new Location(x + i, y + i));
				blocked = true;
			}
		}
		// Look Southeast
		blocked = false;
		for (int i = 1; i <= ((7 - x < y) ? 7 - x : y) && !blocked; i++) {
			Chessman chessman = Chessboard.getChessman(x + i, y - i);
			if (chessman == null) {
				locs.add(new Location(x + i, y - i));
			} else if (chessman.getColor().equals(getColor())) {
				blocked = true;
			} else {
				locs.add(new Location(x + i, y - i));
				blocked = true;
			}
		}
		// Look Southwest
		blocked = false;
		for (int i = 1; i <= ((x < y) ? x : y) && !blocked; i++) {
			Chessman chessman = Chessboard.getChessman(x - i, y - i);
			if (chessman == null) {
				locs.add(new Location(x - i, y - i));
			} else if (chessman.getColor().equals(getColor())) {
				blocked = true;
			} else {
				locs.add(new Location(x - i, y - i));
				blocked = true;
			}
		}
		if (removeChecks) {return removeChecks(locs);}
		else {return locs;}
	}
	
}