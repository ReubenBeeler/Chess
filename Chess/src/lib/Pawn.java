package lib;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import main.Chessboard;
import util.Location;

public final class Pawn extends Chessman {
	
	@SuppressWarnings("serial")
	private static final HashMap<Color, Integer> DIRECTIONS = new HashMap<>() {{put(Color.WHITE, 1); put(Color.BLACK, -1);}};
	
	@SuppressWarnings("serial")
	private static final HashMap<Color, String> IMAGES = new HashMap<>() {{put(Color.WHITE, "Graphics/Images/WhitePawn.png"); put(Color.BLACK, "Graphics/Images/BlackPawn.png");}};
	
	public Pawn(Color color, Location location, boolean original) throws IOException {
		super("Pawn", color, location, ImageIO.read(new File(IMAGES.get(color))), 1, original);
	}
	
	public ArrayList<Location> moveableLocations(boolean removeChecks) {
		ArrayList<Location> locs = new ArrayList<>();
		int x = getLocation().getX();
		int y = getLocation().getY();
		int direction = DIRECTIONS.get(getColor());
		if (0 <= y + direction && y + direction <= 7) {
			if (Chessboard.getChessman(x, y + direction) == null) {
				locs.add(new Location(x, y + direction));
				if (!hasMoved && 0 <= y + (2 * direction) && y + (2 * direction) <= 7 && Chessboard.getChessman(x, y + (2 * direction)) == null) {
					locs.add(new Location(x, y + (2 * direction)));
				}
			}
			if (x != 0) {
				Chessman chessman = Chessboard.getChessman(x - 1, y + direction);
				if (chessman != null && !chessman.getColor().equals(getColor())) {
					locs.add(new Location(x - 1, y + direction));
				}
			}
			if (x != 7) {
				Chessman chessman = Chessboard.getChessman(x + 1, y + direction);
				if (chessman != null && !chessman.getColor().equals(getColor())) {
					locs.add(new Location(x + 1, y + direction));
				}
			}
			// "En passant"
		}
		if (removeChecks) {return removeChecks(locs);}
		else {return locs;}
	}
	
}