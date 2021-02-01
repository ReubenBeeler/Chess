package util;

public final class Location {
	
	private int[] coords;
	
	public Location(int x, int y) {
		this.coords = new int[] {x, y};
	}
	
	public Location(Location location) {
		this.coords = new int[] {location.getX(), location.getY()};
	}
	
	public int[] getCoords() {return coords;}
	public int getX() {return coords[0];}
	public int getY() {return coords[1];}
	
	@Override
	public String toString() {
		return (new String[] {"a", "b", "c", "d", "e", "f", "g", "h"} [getCoords()[0]] + (getCoords()[1] + 1));
	}
}