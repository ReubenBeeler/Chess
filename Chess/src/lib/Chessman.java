package lib;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import main.Chessboard;
import util.Location;

public abstract class Chessman implements Cloneable {
	
	@SuppressWarnings("serial")
	private HashMap<Color, String> colorToString = new HashMap<>() {{put(Color.WHITE, "White"); put(Color.BLACK, "Black");}};
	private String scolor;
	public boolean hasMoved;
	
	private String name;
	private Color color;
	private Location location;
	private BufferedImage image;
	public final int POINTS;
	
	public Chessman (String name, Color color, Location location, BufferedImage image, int points, boolean original) {
		this.name = name;
		this.color = color;
		this.location = location;
		this.image = image;
		this.POINTS = points;
		
		this.scolor = colorToString.get(color);
		this.hasMoved = !original;
	}
	
	public void setLocation(Location location) {this.location = location;}
	
	public String getName() {return name;}
	public Color getColor() {return color;}
	public Location getLocation() {return location;}
	public BufferedImage getImage() {return image;}
	
	abstract public ArrayList<Location> moveableLocations(boolean removeChecks);//, boolean considerCastles);
	
	protected ArrayList<Location> removeChecks(ArrayList<Location> moveableLocations) {
		if (moveableLocations == null) {return null;}
		ArrayList<Location> clean = new ArrayList<>();
		Chessman[][] originalChessboard = Chessboard.deepClone(Chessboard.chessboard);
		for (Location moveableLocation : moveableLocations) {
			Location location = this.getLocation();
			Chessboard.chessboard[location.getY()][location.getX()] = null;
			Chessman removed = Chessboard.chessboard[moveableLocation.getY()][moveableLocation.getX()];
			this.setLocation(moveableLocation);
			Chessboard.chessboard[moveableLocation.getY()][moveableLocation.getX()] = this;
			ArrayList<King> kings = Chessboard.check(Chessboard.chessboard);
			boolean cleanLocation = true;
			for (King king : kings) {
				if (king.getColor().equals(this.getColor())) {cleanLocation = false;}
			}
			if (cleanLocation) {clean.add(moveableLocation);}
			Chessboard.chessboard[moveableLocation.getY()][moveableLocation.getX()] = removed;
			this.setLocation(location);
			Chessboard.chessboard[location.getY()][location.getX()] = this;
			Chessboard.chessboard = Chessboard.deepClone(originalChessboard);
		}
		return clean;
	}
	
	@Override
	public String toString() {
		return scolor + " " + getName() + " at " + getLocation();
	}
	
	public Chessman deepClone() throws CloneNotSupportedException {
		return this.getClass().cast(this.clone());
	}
}