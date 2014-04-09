package org.mahjong.graphics;

import java.util.Arrays;

import org.mahjong.client.Tile;

public final class TileImage {
	/**
	 * @author Xiaocong Wang
	 */
	enum TileImageKind {
		POSITIVE_DOWN,
		POSITIVE_UP,
		POSITIVE_LEFT,
		POSITIVE_RIGHT,
		NEGATIVE_VERTICAL,
		NEGATIVE_HORIZONTAL,
		NEGATIVE_PILE_VERTICAL,
		NEGATIVE_PILE_HORIZONTAL,
	}
	
	/**
	 * Used to get the tiles with different types
	 * @author Xiaocong Wang
	 *
	 */
	public static class Factory {
		public static TileImage getPositiveDown(Tile tile) {
			return new TileImage(TileImageKind.POSITIVE_DOWN, tile);
		}
		public static TileImage getPositiveUp(Tile tile) {
			return new TileImage(TileImageKind.POSITIVE_UP, tile);
		}
		public static TileImage getPositiveLeft(Tile tile) {
			return new TileImage(TileImageKind.POSITIVE_LEFT, tile);
		}
		public static TileImage getPositiveRight(Tile tile) {
			return new TileImage(TileImageKind.POSITIVE_RIGHT, tile);
		}
		public static TileImage getNegativeVertical() {
			return new TileImage(TileImageKind.NEGATIVE_VERTICAL, null);
		}
		public static TileImage getNegativeHorizontal() {
			return new TileImage(TileImageKind.NEGATIVE_HORIZONTAL, null);
		}
		public static TileImage getNegativePileVertical() {
			return new TileImage(TileImageKind.NEGATIVE_PILE_VERTICAL, null);
		}
		public static TileImage getNegativePileHorizontal() {
			return new TileImage(TileImageKind.NEGATIVE_PILE_HORIZONTAL, null);
		}	
	}
	
	public final TileImageKind kind;
	public final Tile tile;
	
	private TileImage(TileImageKind kind, Tile tile) {
		this.kind = kind;
		this.tile = tile;
	}
	
	public Object getId() {
		return Arrays.asList(kind, tile);
	}
	
	private String tile2str(Tile tile) {
		int domain = tile.getDomain();
		if(domain == 0) {//bamboo
			return "bamboo_" + tile.getPost();
		}else if(domain == 1) {//character
			return "character_" + tile.getPost();
		}else if(domain == 2) {//circle
			return "circle_" + tile.getPost();
		}else if(domain == 3) {//bonus
			switch (tile.getPre()) {
			case "Eas": return "wind_east";
			case "Nor": return "wind_north";
			case "Wes": return "wind_west";
			case "Sou": return "wind_south";
			case "Red": return "dragon_red";
			case "Whi": return "dragon_white";
			case "Gre": return "dragon_green";
		    default:
		    	return "Error0!";
			}
		}else {
			return "Error1!";
		}
	}
	
	/**
	 * Convert a tile to the image address
	 * That is to return the address of the image based on the info given by kind and tile
	 */
	@Override
	public String toString() {
		switch (kind) {
		  case POSITIVE_DOWN:
			  return "tiles/" + tile2str(tile) + "_d" + ".png";
		  case POSITIVE_UP:
			  return "tiles/" + tile2str(tile) + "_u" + ".png";
		  case POSITIVE_LEFT:
			  return "tiles/" + tile2str(tile) + "_l" + ".png";
		  case POSITIVE_RIGHT:
			  return "tiles/" + tile2str(tile) + "_r" + ".png";
		  case NEGATIVE_VERTICAL:
			  return "tiles/unknown1.png";
		  case NEGATIVE_HORIZONTAL:
			  return "tiles/unknown2.png";
		  case NEGATIVE_PILE_VERTICAL:
			  return "tiles/unknown1_pile";
		  case NEGATIVE_PILE_HORIZONTAL:
			  return "tiles/unknown2_pile";
		  default:
			  return "Forgot kind=" + kind;
		}
	}
}