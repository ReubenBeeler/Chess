package lib;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import main.Chessboard;
import util.Location;

public final class Bishop extends Chessman {
	
	@SuppressWarnings("serial")
	private static final HashMap<Color, String> IMAGES = new HashMap<>() {{put(Color.WHITE, "Graphics/Images/WhiteBishop.png"); put(Color.BLACK, "Graphics/Images/BlackBishop.png");}};
	
	public Bishop(Color color, Location location, boolean original) throws IOException {
		super("Bishop", color, location, ImageIO.read(new File(IMAGES.get(color))), 3, original);
	}

	public ArrayList<Location> moveableLocations(boolean removeChecks) {//, boolean considerCastles) {
		ArrayList<Location> locs = new ArrayList<>();
		int x = getLocation().getX();
		int y = getLocation().getY();
		// Look Northwest
		boolean blocked = false;
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