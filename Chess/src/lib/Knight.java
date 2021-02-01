package lib;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import main.Chessboard;
import util.Location;

public final class Knight extends Chessman {
	
	@SuppressWarnings("serial")
	private static final HashMap<Color, String> IMAGES = new HashMap<>() {{put(Color.WHITE, "Graphics/Images/WhiteKnight.png"); put(Color.BLACK, "Graphics/Images/BlackKnight.png");}};
	
	public Knight(Color color, Location location, boolean original) throws IOException {
		super("Knight", color, location, ImageIO.read(new File(IMAGES.get(color))), 3, original);
	}
	
	int[] a = {-1, 1};
	int[] b = {-2, 2};

	public ArrayList<Location> moveableLocations(boolean removeChecks) {
		ArrayList<Location> locs = new ArrayList<>();
		int x = getLocation().getX();
		int y = getLocation().getY();
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				if (0 <= x + a[i] && x + a[i] <= 7 && 0 <= y + b[j] && y + b[j] <= 7) {
					Chessman chessman = Chessboard.getChessman(x + a[i], y + b[j]);
					if (chessman == null || !chessman.getColor().equals(getColor())) {
						locs.add(new Location(x + a[i], y + b[j]));
					}
				}
				if (0 <= x + b[i] && x + b[i] <= 7 && 0 <= y + a[j] && y + a[j] <= 7) {
					Chessman chessman = Chessboard.getChessman(x + b[i], y + a[j]);
					if (chessman == null || !chessman.getColor().equals(getColor())) {
						locs.add(new Location(x + b[i], y + a[j]));
					}
				}
			}
		}
		if (removeChecks) {return removeChecks(locs);}
		else {return locs;}
	}
	
}