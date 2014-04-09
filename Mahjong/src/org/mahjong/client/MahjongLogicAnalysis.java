package org.mahjong.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList;

import org.mahjong.client.MahjongState;
import org.mahjong.client.Tile;

/**
 * This class is library used to analyze the mathematical logic for the mahjong game
 * such as is_hu_form, is_peng_form, is_gang_form, is_chi_form and so on.
 * 
 * @author Xiaocong Wang
 *
 */
public class MahjongLogicAnalysis {
	private static void check(boolean val, Object... debugArguments) {
        if (!val) {
          throw new RuntimeException("We have a hacker! debugArguments="
              + Arrays.toString(debugArguments));
        }
      }
	
	private static List<Tile> getTileListFromIndexList(List<Integer> indexList, List<Optional<Tile>> tiles) {
		List<Tile> tileList = new ArrayList<Tile>();
		for(int i=0;i<indexList.size();i++) {
			tileList.add(tiles.get(indexList.get(i)).get());
		}
		return tileList;
	}
	
	public static boolean canPengCheck(MahjongState mahjongState, Position position) {
		check(mahjongState.getCast().isPresent());
		Tile castTile = mahjongState.getTiles().get(mahjongState.getCast().get().getValue()).get();
		Tile specialTile = mahjongState.getTiles().get(mahjongState.getSpecialTile()).get();
		List<Tile> tilesInHand = getTileListFromIndexList(mahjongState.getOneOfFourTile(position), mahjongState.getTiles());
		return isPengFormed(castTile, specialTile, tilesInHand);
	}
	
	public static boolean isPengFormed(Tile castTile, Tile specialTile, List<Tile> tilesInHand) {
		int count = 0;
		for(Tile tile : tilesInHand) {
			if(isSpecialTile(tile, specialTile) || tile.equals(castTile)) {
				count ++;
			}
			if(count == 2) return true;
		}
		return false;
	}
	
	private static boolean isSpecialTile(Tile tile, Tile specialTile) {
		if(tile.equals(specialTile)) return true;
		if(tile.getDomain() != specialTile.getDomain()) return false;
		if(tile.getDomain() != 3){
			return (specialTile.getPost()+1)%9 == tile.getPost();
		}else{
			switch (specialTile.getPre()){
			case "Eas": return tile.getPre().equals("Sou"); 
			case "Nor": return tile.getPre().equals("Eas");
			case "Wes": return tile.getPre().equals("Nor");
			case "Sou": return tile.getPre().equals("Wes");
			case "Red": return tile.getPre().equals("Gre");
			case "Gre": return tile.getPre().equals("Whi");
			case "Whi": return tile.getPre().equals("Red");
			}
			return false;
		}
	}
		
	public static boolean canChiCheck(MahjongState mahjongState, Position position) {
		check(mahjongState.getCast().isPresent());
		Tile castTile = mahjongState.getTiles().get(mahjongState.getCast().get().getValue()).get();
		Tile specialTile = mahjongState.getTiles().get(mahjongState.getSpecialTile()).get();
		List<Tile> tilesInHand = getTileListFromIndexList(mahjongState.getOneOfFourTile(position), mahjongState.getTiles());
		return isChiFormed(castTile, specialTile, tilesInHand);
	}
	
	public static boolean isChiFormed(Tile castTile, Tile specialTile, List<Tile> tilesInHand) {
		int stNum = 0;
		if(castTile.getDomain() == 3) {
			return false;
		}
		for(Tile tile : tilesInHand) {
			if(isSpecialTile(tile, specialTile)) {
				++ stNum;
			}
		}
		if(stNum >= 2) return true;
		List<Integer> tileNumWithSameDomain = Lists.newArrayList();
		for(Tile tile : tilesInHand) {
			if(tile.getDomain() == castTile.getDomain() && !isSpecialTile(tile, specialTile)) {
				tileNumWithSameDomain.add(tile.getPost());
			}
		}
		int length = tileNumWithSameDomain.size();
		int castTileNum = castTile.getPost();
		if(stNum == 1) {
			for(Integer i : tileNumWithSameDomain) {
				if(i+1 == castTileNum || i-1 == castTileNum) {
					return true;
				}
			}
			return false;
		}
	
		for(int i = 0;i<length;i++) {
			for(int j = i+1;j<length;j++) {
				if(isChiMeld(tileNumWithSameDomain.get(i),
						tileNumWithSameDomain.get(j), castTileNum)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isChiMeld(int one, int two, int three) {
		List<Integer> chiMeld = ImmutableList.<Integer>of(one, two, three);
		Collections.sort(chiMeld);
		int diff1 = chiMeld.get(1) - chiMeld.get(0);
		int diff2 = chiMeld.get(2) - chiMeld.get(1);
		if(diff1==1 && diff2==1) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean canHuCheck(MahjongState mahjongState, Position position) {
		Tile extraTile = null;
		if(mahjongState.getOneOfFourTile(position).size()%3 != 2) {
			extraTile = mahjongState.getTiles().get(mahjongState.getCast().get().getValue()).get();
		}
		
		Tile specialTile = mahjongState.getTiles().get(mahjongState.getSpecialTile()).get();
		
		List<Tile> tilesInHand = getTileListFromIndexList(mahjongState.getOneOfFourTile(position), mahjongState.getTiles());
		
		if(extraTile != null)
		tilesInHand.add(extraTile);
		
		boolean res = isHuFormed(specialTile, tilesInHand);
		return res;
	}
	
	public static boolean isHuFormed(Tile specialTile, List<Tile> tilesInHand) {
		List<Tile> tileNotSpecial = Lists.newArrayList();
		int numSpecial = 0;
		for(Tile tile : tilesInHand) {
			if(!isSpecialTile(tile, specialTile)) {
				tileNotSpecial.add(tile);
			}else {
				++ numSpecial;
			}
		}
		
		Map<Tile,Integer> map = new HashMap<Tile,Integer>();
		for(Tile tile : tileNotSpecial) {
			if(!map.containsKey(tile)) {
				map.put(tile, 1);
			}else {
				map.put(tile, map.get(tile) + 1);
			}
		}
		
		Set<Map.Entry<Tile, Integer>> set = map.entrySet();
		for(Map.Entry<Tile, Integer> entry : set) {
			int sn = numSpecial;
			List<Tile> tileNotSpecialEye = Lists.newArrayList();
			Map<Tile, Integer> tmpMap = new HashMap<Tile, Integer>(map);
			Set<Map.Entry<Tile, Integer>> tmpSet;
			if(entry.getValue() >= 2) {
				tmpMap.put(entry.getKey(), entry.getValue()-2);
				tmpSet = tmpMap.entrySet();
				for(Map.Entry<Tile, Integer> tmpEntry : tmpSet) {
					for(int n=0;n<tmpEntry.getValue();n++) {
						tileNotSpecialEye.add(new Tile(tmpEntry.getKey()));
					}
				}
			}else {
				if(numSpecial >= 1) {
					tmpMap.put(entry.getKey(), entry.getValue()-1);
					tmpSet = tmpMap.entrySet();
					for(Map.Entry<Tile, Integer> tmpEntry : tmpSet) {
						for(int l=0;l<tmpEntry.getValue();l++) {
							tileNotSpecialEye.add(new Tile(tmpEntry.getKey()));
						}
					}
					sn = sn - 1;
				}else {
					continue;
				}
			}
			
			if(isAllMeldOfChiPeng(sn, tileNotSpecialEye)) {
				return true;
			}
			else {
				continue; 
			}
		}
		return false;
	}
	
	
	public static boolean isAllMeldOfChiPeng(int sn, List<Tile> tiles) {
		
		int num = sn + tiles.size();
		check(num%3 == 0);
		Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
		String pre = null;
		for(Tile tile : tiles) {
			if(tile.getDomain() == 0) {
				pre = "bamboo";
			}else if(tile.getDomain() == 1) {
				pre = "character";
			}else if(tile.getDomain() == 2) {
				pre = "circle";
			}else {
				if(tile.getPre() == "Eas") pre = "east";
				if(tile.getPre() == "Nor") pre = "north";
				if(tile.getPre() == "Wes") pre = "west";
				if(tile.getPre() == "Sou") pre = "south";
				if(tile.getPre() == "Red") pre = "red";
			    if(tile.getPre() == "Whi") pre = "white";
				if(tile.getPre() == "Gre") pre = "green";
			}
			addTile(map, pre, tile);
		}
		Set<Map.Entry<String, List<Integer>>> set = map.entrySet();
		for(Map.Entry<String, List<Integer>> entry : set) {
			int entrySize = entry.getValue().size();
			int need = entrySize%3==0 ? 0 : (3 - entrySize%3);
			sn = sn - need;
		}
		if(sn < 0) {
			return false;
		}
		int count = 0;
		for(Map.Entry<String, List<Integer>> entry : set) {
			List<Integer> sameTypeTiles = entry.getValue();
			count = count + numOfSpecialTileNeeded(sameTypeTiles);
		}
		if(count <= sn) {
			return true;
		}else {
			return false;
		}
	}
	
	public static int numOfSpecialTileNeeded(List<Integer> sameTypeTiles) {
		if(sameTypeTiles.get(0) == 0) {
			return 0;
		}
		int numOfZero = 3 - sameTypeTiles.size()%3;
		if(isValidMeldList(sameTypeTiles, numOfZero)) {
			return 0;
		}else {
			if(isValidMeldList(sameTypeTiles, numOfZero+3)) {
				return 3;
			}else {
				return 6;
			}
		}
	}
	
	public static boolean isValidMeldList (List<Integer> listWithoutZero, int numOfZero) {
		if(listWithoutZero.size() == 0) {
			return true;
		}
		Collections.sort(listWithoutZero);
		boolean b1 = false;
		boolean b2 = false;
		boolean b3 = false;
		boolean b4 = false;
		int standard = listWithoutZero.get(0);
		
		List<Integer> a1 = new ArrayList<Integer>();
		int num1 = numOfZero;
		int count1 = 0;
		for(int i=1;i<listWithoutZero.size();i++) {
			if(standard == listWithoutZero.get(i) && count1 < 2) {
				count1 ++;
			}else {
				a1.add(listWithoutZero.get(i));
			}
		}
		if((2 - count1) > numOfZero) {
			b1 = false;
		}else {
			num1 = num1 - (2 - count1);
			b1 = isValidMeldList(a1, num1);
		}
			
		if(standard-1>=1 && standard+1<=9) {
			List<Integer> a2 = new ArrayList<Integer>();
			int num2 = numOfZero;
			int count2 = 0;
			int count3 = 0;
			for(int i=1;i<listWithoutZero.size();i++) {
				if(standard-1==listWithoutZero.get(i) && count2 == 0) {
					count2 ++;
				}else if(standard+1==listWithoutZero.get(i) && count3 == 0) {
					count3 ++;
				}else {
					a2.add(listWithoutZero.get(i));
				}
			}
			if(2 -(count2+count3) > numOfZero) {
				b2 = false;
			}else {
				num2 = num2 - (2 - (count2+count3));
				b2 = isValidMeldList(a2, num2);
			}
		}else {
			b2 = false;
		}
		
		if(standard+2<=9) {
			List<Integer> a3 = new ArrayList<Integer>();
			int num3 = numOfZero;
			int count4 = 0;
			int count5 = 0;
			for(int i=1;i<listWithoutZero.size();i++) {
				if(standard+1==listWithoutZero.get(i) && count4 == 0) {
					count4 ++;
				}else if(standard+2==listWithoutZero.get(i) && count5 == 0) {
					count5 ++;
				}else {
					a3.add(listWithoutZero.get(i));
				}
			}
			if(2 -(count4+count5) > numOfZero) {
				b3 = false;
			}else {
				num3 = num3 - (2 - (count4+count5));
				b3 = isValidMeldList(a3, num3);
			}
		}else {
			b3 = false;
		}
		
		if(standard-2>=1) {
			List<Integer> a4 = new ArrayList<Integer>();
			int num4 = numOfZero;
			int count6 = 0;
			int count7 = 0;
			for(int i=1;i<listWithoutZero.size();i++) {
				if(standard-1==listWithoutZero.get(i) && count6 == 0) {
					count6 ++;
				}else if(standard-2==listWithoutZero.get(i) && count7 == 0) {
					count7 ++;
				}else {
					a4.add(listWithoutZero.get(i));
				}
			}
			if(2 -(count6+count7) > numOfZero) {
				b4 = false;
			}else {
				num4 = num4 - (2 - (count6+count7));
				b4 = isValidMeldList(a4, num4);
			}
		}else {
			b4 = false;
		}		
		return b1 || b2 || b3 || b4;
	}
	
	public static boolean isValidMeld(List<Integer> meld) {
		int one = meld.get(0);
		int two = meld.get(1);
		int three = meld.get(2);
		if((one == 0 && two == 0) || (one == 0 && three == 0) ||
				(two == 0 && three == 0)) {
			return true;
		}
		if(one == 0) {
			return Math.abs(two-three) <= 2;
		}
		if(two == 0) {
			return Math.abs(one-three) <= 2;
		}
		if(three == 0) {
			return Math.abs(one-two) <= 2;
		}
		if(one == two && two == three) {
			return true;
		}
		Collections.sort(meld);
		if(two == one + 1 && three == two + 1) {
			return true;
		}
		return false;
	}
	
	public static void addTile(Map<String, List<Integer>> map, String name, Tile tile) {
		List<Integer> tmp;
		if(map.containsKey(name)) {
			tmp = map.get(name);
		}else {
			tmp = Lists.newArrayList();
		}
		tmp.add(tile.getPost());
		map.put(name, tmp);
	}
}
