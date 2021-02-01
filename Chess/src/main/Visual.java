package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;

import lib.Chessman;
import lib.King;
import util.Location;

@SuppressWarnings("serial")
public class Visual extends JPanel implements MouseListener, KeyListener {
	
	protected int[] lefts = new int[8];
	protected int[] tops = new int[8];
	Chessman selectedChessman;
	ArrayList<King> checked = new ArrayList<>();
	
	@Override
	protected void paintComponent(Graphics g) {
		Color[] colors = {new Color(95, 55, 5), new Color(235, 235, 200)};
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				lefts[x] = x * getWidth() / 8;
				tops[y] = (7 - y) * getHeight() / 8;
				g.setColor(colors[(x + y) % 2]);
				g.fillRect(lefts[x], tops[y], ((x + 1) * getWidth() / 8) - lefts[x], ((8 - y) * getHeight() / 8) - tops[y]);
				
			}
		}
		for (King king : checked) {
			if (king != selectedChessman) {
				int x = king.getLocation().getX();
				int y = king.getLocation().getY();
				g.setColor(new Color(255, 100, 100));
				g.fillRect(lefts[x], tops[y], ((x + 1) * getWidth() / 8) - lefts[x], ((8 - y) * getHeight() / 8) - tops[y]);
			}
		}
		if (selectedChessman != null) {
			g.setColor(new Color(100, 100, 255, 127));
			for (Location location : selectedChessman.moveableLocations(true)) {
				int x = location.getX();
				int y = location.getY();
				g.fillRect(lefts[x], tops[y], ((x + 1) * getWidth() / 8) - lefts[x], ((8 - y) * getHeight() / 8) - tops[y]);
			}
			int x = selectedChessman.getLocation().getX();
			int y = selectedChessman.getLocation().getY();
			g.setColor(new Color(100, 100, 255));
			g.fillRect(lefts[x], tops[y], ((x + 1) * getWidth() / 8) - lefts[x], ((8 - y) * getHeight() / 8) - tops[y]);
		}
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (Chessboard.chessboard[y][x] != null) {
					g.drawImage(Chessboard.getChessman(x, y).getImage(), lefts[x], tops[y], ((x + 1) * getWidth() / 8) - lefts[x], ((8 - y) * getHeight() / 8) - tops[y], this);
				}
			}
		}
	}
	
	void update() {
		checked = Chessboard.checkmate();
		if (!checked.isEmpty()) {System.out.println("CHECKMATE");}
		else if (Chessboard.stalemate()) {System.out.println("STALEMATE");}
		checked = Chessboard.check();
		repaint();
	}
	
	private Location p2L(int xP, int yP) {
		yP -= 37; // JFrame bar at top is 37 pixels
		
		int xB = -1;
		int yB = -1;
		for (int x = 7; x >= 0; x--) {
			if (xP > lefts[x]) {
				xB = x;
				break;
			}
		}
		for (int y = 0; y < 8; y++) {
			if (yP > tops[y]) {
				yB = y;
				break;
			}
		}
		return new Location(xB, yB);
	}

	private Color[] colors = {Color.WHITE, Color.BLACK};
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getY() <= 37) {return;}
		Location location = p2L(e.getX(), e.getY());
		Chessman clickedChessman = Chessboard.getChessman(location);
		if (selectedChessman == null) {
			if (clickedChessman == null) {return;}
			if (clickedChessman.getColor().equals(colors[Chessboard.turn % 2])) {
				selectedChessman = clickedChessman;
				repaint();
			}
		} else {
			ArrayList<String> moveableLocationsToString = new ArrayList<>();
			for (Location moveableLocation : selectedChessman.moveableLocations(true)) {
				moveableLocationsToString.add(moveableLocation.toString());
			}
			if (moveableLocationsToString.contains(location.toString())){
				History.next();
				if (selectedChessman instanceof King && Arrays.asList(Chess.toString(Chessboard.castle((King) selectedChessman))).contains(location.toString())) {
					try {
						int y = selectedChessman.getLocation().getY();
						if (location.getX() == 2) {
							Chessboard.move(Chessboard.getChessman(0, y), new Location(3, y));
						} else {
							Chessboard.move(Chessboard.getChessman(7, y), new Location(5, y));
						}
					} catch (NullPointerException npe) {
						System.out.println(selectedChessman.toString() + " castles without rook.");
						npe.printStackTrace();
					}
				}
				Chessboard.move(selectedChessman, location);
				selectedChessman = null;
				Chessboard.turn++;
				update();
			} else {
				if (clickedChessman != null && !clickedChessman.toString().equals(selectedChessman.toString()) && clickedChessman.getColor().equals(selectedChessman.getColor())) {
					selectedChessman = clickedChessman;
				} else {
					selectedChessman = null;
				}
				repaint();
			} 
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if ((keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_KP_LEFT) && Chessboard.turn != 0) {History.undo(); update();}
		if ((keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_KP_RIGHT) && History.getRedos() != 0) {History.redo(); update();}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
}