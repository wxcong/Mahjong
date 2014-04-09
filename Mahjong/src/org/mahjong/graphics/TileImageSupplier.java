package org.mahjong.graphics;

import org.mahjong.client.Tile;
import com.google.gwt.resources.client.ImageResource;

/**
 * A mapping from Tile to its ImageResource
 * @author Xiaocong Wang
 *
 */
public class TileImageSupplier {
	private final TileImages tileImages;
	
	public TileImageSupplier(TileImages tileImages) {
		this.tileImages = tileImages;
	}
	
	public ImageResource getResource(TileImage tileImage) {
	 switch(tileImage.kind) {
	  case POSITIVE_DOWN:
		  return getPositiveDown(tileImage.tile);
	  case POSITIVE_UP:
		  return getPositiveUp(tileImage.tile);
	  case POSITIVE_LEFT:
		  return getPositiveLeft(tileImage.tile);
	  case POSITIVE_RIGHT:
		  return getPositiveRight(tileImage.tile);
	  case NEGATIVE_VERTICAL:
		  return getNegativeVertical();
	  case NEGATIVE_HORIZONTAL:
		  return getNegativeHorizontal();
	  case NEGATIVE_PILE_VERTICAL:
		  return getNegativePileVertical();
	  case NEGATIVE_PILE_HORIZONTAL:
		  return getNegativePileHorizontal();
	  default:
		  throw new RuntimeException("Forgot kind=" + tileImage.kind);
	 }
	}
	
	public ImageResource getNegativeVertical() {
		return tileImages.unknownVertical();
	}
	
	public ImageResource getNegativeHorizontal() {
		return tileImages.unknownHorizontal();
	}
	
	public ImageResource getNegativePileVertical() {
		return tileImages.unknownVerticalPile();
	}
	
	public ImageResource getNegativePileHorizontal() {
		return tileImages.unknownHorizontalPile();
	}
	
	public ImageResource getPositiveDown(Tile tile) {
		if(tile.getDomain() == 0) {//bam
			switch (tile.getPost()) {
			case 1: return tileImages.b1d();
			case 2: return tileImages.b2d();
			case 3: return tileImages.b3d();
			case 4: return tileImages.b4d();
			case 5: return tileImages.b5d();
			case 6: return tileImages.b6d();
			case 7: return tileImages.b7d();
			case 8: return tileImages.b8d();
			case 9: return tileImages.b9d();
			default:
			    throw new RuntimeException("Forgot bamboo post:" + tile.getPost());
			}
		}else if(tile.getDomain() == 1) {//cha
			switch (tile.getPost()) {
			case 1: return tileImages.h1d();
			case 2: return tileImages.h2d();
			case 3: return tileImages.h3d();
			case 4: return tileImages.h4d();
			case 5: return tileImages.h5d();
			case 6: return tileImages.h6d();
			case 7: return tileImages.h7d();
			case 8: return tileImages.h8d();
			case 9: return tileImages.h9d();
			default:
			    throw new RuntimeException("Forgot character post:" + tile.getPost());
			}
		}else if(tile.getDomain() == 2) {//cir
			switch (tile.getPost()) {
			case 1: return tileImages.c1d();
			case 2: return tileImages.c2d();
			case 3: return tileImages.c3d();
			case 4: return tileImages.c4d();
			case 5: return tileImages.c5d();
			case 6: return tileImages.c6d();
			case 7: return tileImages.c7d();
			case 8: return tileImages.c8d();
			case 9: return tileImages.c9d();
			default:
			    throw new RuntimeException("Forgot circle post:" + tile.getPost());
			}
		}else if(tile.getDomain() == 3) {//dragon & wind
			switch (tile.getPre()) {
			case "Eas": return tileImages.wed();
			case "Nor": return tileImages.wnd();
			case "Wes": return tileImages.wwd();
			case "Sou": return tileImages.wsd();
			case "Red": return tileImages.drd();
			case "Gre": return tileImages.dgd();
			case "Whi": return tileImages.dwd();
			default:
			    throw new RuntimeException("Forgot dragon or wind pre:" + tile.getPre());
			}
		}else {
			throw new RuntimeException("Forgot domain:" + tile.getDomain());
		}
	}
	
	public ImageResource getPositiveUp(Tile tile) {
		if(tile.getDomain() == 0) {//bam
			switch (tile.getPost()) {
			case 1: return tileImages.b1u();
			case 2: return tileImages.b2u();
			case 3: return tileImages.b3u();
			case 4: return tileImages.b4u();
			case 5: return tileImages.b5u();
			case 6: return tileImages.b6u();
			case 7: return tileImages.b7u();
			case 8: return tileImages.b8u();
			case 9: return tileImages.b9u();
			default:
			    throw new RuntimeException("Forgot bamboo post:" + tile.getPost());
			}
		}else if(tile.getDomain() == 1) {//cha
			switch (tile.getPost()) {
			case 1: return tileImages.h1u();
			case 2: return tileImages.h2u();
			case 3: return tileImages.h3u();
			case 4: return tileImages.h4u();
			case 5: return tileImages.h5u();
			case 6: return tileImages.h6u();
			case 7: return tileImages.h7u();
			case 8: return tileImages.h8u();
			case 9: return tileImages.h9u();
			default:
			    throw new RuntimeException("Forgot character post:" + tile.getPost());
			}
		}else if(tile.getDomain() == 2) {//cir
			switch (tile.getPost()) {
			case 1: return tileImages.c1u();
			case 2: return tileImages.c2u();
			case 3: return tileImages.c3u();
			case 4: return tileImages.c4u();
			case 5: return tileImages.c5u();
			case 6: return tileImages.c6u();
			case 7: return tileImages.c7u();
			case 8: return tileImages.c8u();
			case 9: return tileImages.c9u();
			default:
			    throw new RuntimeException("Forgot circle post:" + tile.getPost());
			}
		}else if(tile.getDomain() == 3) {//dragon & wind
			switch (tile.getPre()) {
			case "Eas": return tileImages.weu();
			case "Nor": return tileImages.wnu();
			case "Wes": return tileImages.wwu();
			case "Sou": return tileImages.wsu();
			case "Red": return tileImages.dru();
			case "Gre": return tileImages.dgu();
			case "Whi": return tileImages.dwu();
			default:
			    throw new RuntimeException("Forgot dragon or wind pre:" + tile.getPre());
			}
		}else {
			throw new RuntimeException("Forgot domain:" + tile.getDomain());
		}
	}
	
	public ImageResource getPositiveLeft(Tile tile) {
		if(tile.getDomain() == 0) {//bam
			switch (tile.getPost()) {
			case 1: return tileImages.b1l();
			case 2: return tileImages.b2l();
			case 3: return tileImages.b3l();
			case 4: return tileImages.b4l();
			case 5: return tileImages.b5l();
			case 6: return tileImages.b6l();
			case 7: return tileImages.b7l();
			case 8: return tileImages.b8l();
			case 9: return tileImages.b9l();
			default:
			    throw new RuntimeException("Forgot bamboo post:" + tile.getPost());
			}
		}else if(tile.getDomain() == 1) {//cha
			switch (tile.getPost()) {
			case 1: return tileImages.h1l();
			case 2: return tileImages.h2l();
			case 3: return tileImages.h3l();
			case 4: return tileImages.h4l();
			case 5: return tileImages.h5l();
			case 6: return tileImages.h6l();
			case 7: return tileImages.h7l();
			case 8: return tileImages.h8l();
			case 9: return tileImages.h9l();
			default:
			    throw new RuntimeException("Forgot character post:" + tile.getPost());
			}
		}else if(tile.getDomain() == 2) {//cir
			switch (tile.getPost()) {
			case 1: return tileImages.c1l();
			case 2: return tileImages.c2l();
			case 3: return tileImages.c3l();
			case 4: return tileImages.c4l();
			case 5: return tileImages.c5l();
			case 6: return tileImages.c6l();
			case 7: return tileImages.c7l();
			case 8: return tileImages.c8l();
			case 9: return tileImages.c9l();
			default:
			    throw new RuntimeException("Forgot circle post:" + tile.getPost());
			}
		}else if(tile.getDomain() == 3) {//dragon & wind
			switch (tile.getPre()) {
			case "Eas": return tileImages.wel();
			case "Nor": return tileImages.wnl();
			case "Wes": return tileImages.wwl();
			case "Sou": return tileImages.wsl();
			case "Red": return tileImages.drl();
			case "Gre": return tileImages.dgl();
			case "Whi": return tileImages.dwl();
			default:
			    throw new RuntimeException("Forgot dragon or wind pre:" + tile.getPre());
			}
		}else {
			throw new RuntimeException("Forgot domain:" + tile.getDomain());
		}
	}
	
	public ImageResource getPositiveRight(Tile tile) {
		if(tile.getDomain() == 0) {//bam
			switch (tile.getPost()) {
			case 1: return tileImages.b1r();
			case 2: return tileImages.b2r();
			case 3: return tileImages.b3r();
			case 4: return tileImages.b4r();
			case 5: return tileImages.b5r();
			case 6: return tileImages.b6r();
			case 7: return tileImages.b7r();
			case 8: return tileImages.b8r();
			case 9: return tileImages.b9r();
			default:
			    throw new RuntimeException("Forgot bamboo post:" + tile.getPost());
			}
		}else if(tile.getDomain() == 1) {//cha
			switch (tile.getPost()) {
			case 1: return tileImages.h1r();
			case 2: return tileImages.h2r();
			case 3: return tileImages.h3r();
			case 4: return tileImages.h4r();
			case 5: return tileImages.h5r();
			case 6: return tileImages.h6r();
			case 7: return tileImages.h7r();
			case 8: return tileImages.h8r();
			case 9: return tileImages.h9r();
			default:
			    throw new RuntimeException("Forgot character post:" + tile.getPost());
			}
		}else if(tile.getDomain() == 2) {//cir
			switch (tile.getPost()) {
			case 1: return tileImages.c1r();
			case 2: return tileImages.c2r();
			case 3: return tileImages.c3r();
			case 4: return tileImages.c4r();
			case 5: return tileImages.c5r();
			case 6: return tileImages.c6r();
			case 7: return tileImages.c7r();
			case 8: return tileImages.c8r();
			case 9: return tileImages.c9r();
			default:
			    throw new RuntimeException("Forgot circle post:" + tile.getPost());
			}
		}else if(tile.getDomain() == 3) {//dragon & wind
			switch (tile.getPre()) {
			case "Eas": return tileImages.wer();
			case "Nor": return tileImages.wnr();
			case "Wes": return tileImages.wwr();
			case "Sou": return tileImages.wsr();
			case "Red": return tileImages.drr();
			case "Gre": return tileImages.dgr();
			case "Whi": return tileImages.dwr();
			default:
			    throw new RuntimeException("Forgot dragon or wind pre:" + tile.getPre());
			}
		}else {
			throw new RuntimeException("Forgot domain:" + tile.getDomain());
		}
	}
}
