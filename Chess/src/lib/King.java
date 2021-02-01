package lib;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import main.Chessboard;
import util.Location;

public final class King extends Chessman {
	
	@SuppressWarnings("serial")
	private static final HashMap<Color, String> IMAGES = new HashMap<>() {{put(Color.WHITE, "Graphics/Images/WhiteKing.png"); put(Color.BLACK, "Graphics/Images/BlackKing.png");}};
	
	public King(Color color, Location location, boolean original) throws IOException {
		super("King", color, location, ImageIO.read(new File(IMAGES.get(color))), 0, original);
	}
	
	public ArrayList<Location> moveableLocations(boolean removeChecks) {//, boolean considerCastles) {
		ArrayList<Location> locs = new ArrayList<>();
		int x = getLocation().getX();
		int y = getLocation().getY();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if ((!(x + i < 0 || x + i > 7 || y + j < 0 || y + j > 7) && !(i == 0 && j == 0))) {
					Chessman chessman = Chessboard.getChessman(x + i, y + j);
					if (chessman == null || !chessman.getColor().equals(getColor())) {
						locs.add(new Location(x + i, y + j));
					}
				}
			}
		}
		if (removeChecks) {
			for (Location location : Chessboard.castle(this)) {
				locs.add(location);
			}
			return removeChecks(locs);}
		else {return locs;}
	}
}