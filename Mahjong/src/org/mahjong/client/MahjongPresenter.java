package org.mahjong.client;

import java.util.List;
import java.util.ArrayList;

import org.game_api.GameApi.*;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;


public class MahjongPresenter {

	/**
	 * All Messages
	 * @author Xiaocong Wang
	 *
	 */
	public enum MahjongMessage {
		INVISIBLE_, 
		
		INI_CONTINUE_, 
		
		NO_MOVE_,
		
		AUTO_HU_CHECK_,
		AUTO_PENG_CHECK_,
		AUTO_CHI_CHECK_,
		
		WAIT_HU_CHOICE_,
		WAIT_PENG_CHOICE_,
		WAIT_CHI_CHOICE_,
		
		//for test
		TEST_,
	}
	
	/**
	 * Interface for view
	 * @author Xiaocong Wang
	 *
	 */
	public interface View {
		public void setPresenter(MahjongPresenter mahjongPresenter);
		
		public void setViewerState(
				int tileNumberOfEast,
				int tileNumberOfNorth,
				int tileNumberOfWest,
				int tileNumberOfSouth,
				List<Integer> wallNumberOfEast,
				List<Integer> wallNumberOfNorth,
				List<Integer> wallNumberOfWest,
				List<Integer> wallNumberOfSouth,
				List<Tile> chiTilesOfEast,
		        List<Tile> pengTilesOfEast,
		        List<Tile> gangTilesOfEast,
		        List<Tile> chiTilesOfNorth,
                List<Tile> pengTilesOfNorth,
                List<Tile> gangTilesOfNorth,
		        List<Tile> chiTilesOfWest,
                List<Tile> pengTilesOfWest,
                List<Tile> gangTilesOfWest,
		        List<Tile> chiTilesOfSouth,
                List<Tile> pengTilesOfSouth,
                List<Tile> gangTilesOfSouth,
                Tile specialTile,
                List<Tile> castTilesOfEast,
                List<Tile> castTilesOfNorth,
                List<Tile> castTilesOfWest,
                List<Tile> castTilesOfSouth,
                MahjongMessage mahjongMessage);
		
		public void setPlayerState(
				int tileNumberOfOpponent1, 
				List<Tile> chiTilesOfOpponent1,
				List<Tile> pengTilesOfOpponent1,
				List<Tile> gangTilesOfOpponent1,
				List<Tile> castTilesOfOpponent1,
				List<Integer> wallNumberOfOpponent1, 
				int tileNumberOfOpponent2, 
				List<Tile> chiTilesOfOpponent2,
				List<Tile> pengTilesOfOpponent2,
				List<Tile> gangTilesOfOpponent2,
				List<Tile> castTilesOfOpponent2,
				List<Integer> wallNumberOfOpponent2, 
				int tileNumberOfOpponent3,
				List<Tile> chiTilesOfOpponent3,
				List<Tile> pengTilesOfOpponent3,
				List<Tile> gangTilesOfOpponent3,
				List<Tile> castTilesOfOpponent3,
				List<Integer> wallNumberOfOpponent3, 
				List<Tile> myTiles,
				List<Tile> myChiTiles,
				List<Tile> myPengTiles,
				List<Tile> myGangTiles,
				List<Tile> myCastTiles,
 				List<Integer> myWallNumber,
				Tile specialTile,
				List<Integer> myTilesIndexes,
				MahjongMessage mahjongMessage);
		
		public void chooseCastTile(List<Tile> selectedCastTile, List<Tile> remainingTiles,
				List<Integer> selectedCastTileIndex, List<Integer> remainingTileIndexes);
		
		public void choosePengTiles(List<Tile> selectedPengTiles, List<Tile> remainingTiles,
				List<Integer> selectedPengTileIndexes, List<Integer> remainingTileIndexes);
		
		public void chooseChiTiles(List<Tile> selectedChiTiles, List<Tile> remainingTiles,
				List<Integer> selectedChiTileIndexes, List<Integer> remainingTileIndexes);
		
		//for test
		public void testButton1(String str);
		public void testButton2();
		public void testButton3();
		public void testButton4();
		public void testButton5();
	}
	
	private final MahjongLogic mahjongLogic = new MahjongLogic();
    private final View view;
	private final Container container;
	private final String INI_CONTINUE = "iniContinue";	
	private Optional<Position> myPosition;
	private MahjongState mahjongState;
	private List<Optional<Tile>> tiles;
	private List<Tile> selectedCastTile;
	private List<Integer> selectedCastTileIndex;
	private List<Tile> selectedPengTiles;
	private List<Integer> selectedPengTileIndexes;
	private List<Tile> selectedChiTiles;
	private List<Integer> selectedChiTileIndexes;
	
	public MahjongPresenter(View view, Container container) {
		this.view = view;
		this.container = container;
		view.setPresenter(this);
	}
	
	public void updateUI(UpdateUI updateUI) {
		
		List<String> playerIds = updateUI.getPlayerIds();
		String yourPlayerId = updateUI.getYourPlayerId();
		int yourPlayerIndex = updateUI.getPlayerIndex(yourPlayerId);
		selectedCastTile = Lists.newArrayList();
		selectedCastTileIndex = Lists.newArrayList();
		selectedPengTiles = Lists.newArrayList();
		selectedPengTileIndexes = Lists.newArrayList();
		selectedChiTiles = Lists.newArrayList();
		selectedChiTileIndexes = Lists.newArrayList();
		myPosition = yourPlayerIndex == 0 ? Optional.of(Position.E)
			: yourPlayerIndex == 1 ? Optional.of(Position.N)
			: yourPlayerIndex == 2 ? Optional.of(Position.W)
			: yourPlayerIndex == 3 ? Optional.of(Position.S) : Optional.<Position>absent();
		Position myP = null;
		Position opponent1 = null;
		Position opponent2 = null;
		Position opponent3 = null;
		if(myPosition.isPresent()) {
		myP = myPosition.get();
	    opponent1 = myP.getNextTurnOfPosition();
	    opponent2 = opponent1.getNextTurnOfPosition();
		opponent3 = opponent2.getNextTurnOfPosition();	
		}

	   if(updateUI.getState().isEmpty()) {
		   if(myPosition.isPresent() && myPosition.get().isEast()) {
			   sendInitialMoveOne(playerIds);
		   }  
		   return;
	   }
	  
	   Position turnOfPosition = null;
	   for(Operation operation : updateUI.getLastMove()) {
		   if(operation instanceof SetTurn) {
			   turnOfPosition = Position.values()[playerIds.indexOf(((SetTurn) operation).getPlayerId())];
		   }
	   }
	   
	   if(updateUI.isViewer() && updateUI.getState().containsKey(INI_CONTINUE)) {
		   view.setViewerState(
				   0,
				   0,
				   0,
				   0,
				   mahjongLogic.getIndicesInRange(0, 33),
				   mahjongLogic.getIndicesInRange(34, 67),
				   mahjongLogic.getIndicesInRange(68, 101),
				   mahjongLogic.getIndicesInRange(102, 135),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   null,
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   MahjongMessage.INVISIBLE_);
		   return;
	   }
	   if(updateUI.isAiPlayer() && updateUI.getState().containsKey(INI_CONTINUE)) {
		   return;
	   }
	   
	   if(updateUI.getState().containsKey(INI_CONTINUE)) {
		   view.setPlayerState(
				   0,
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   mahjongLogic.getIndicesInRange(34, 67),
				   0,
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   mahjongLogic.getIndicesInRange(68, 101),
				   0,
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   mahjongLogic.getIndicesInRange(102, 135),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   ImmutableList.<Tile>of(),
				   mahjongLogic.getIndicesInRange(0, 33),
				   null,
				   ImmutableList.<Integer>of(),
				   MahjongMessage.INI_CONTINUE_);   
		   if(myPosition.isPresent() && myPosition.get().isEast()) {
			   sendInitialMoveTwo(playerIds);
		   }
		   return;
    }
	   
	   mahjongState = mahjongLogic.gameApiStateToMahjongState(updateUI.getState(), turnOfPosition, playerIds);
	   tiles = mahjongState.getTiles();

	   if(updateUI.isViewer()) {	
		   view.setViewerState(
				   mahjongState.getE().size(),
				   mahjongState.getN().size(),
				   mahjongState.getW().size(),
				   mahjongState.getS().size(),
				   mahjongState.getWallEast(),
				   mahjongState.getWallNorth(),
				   mahjongState.getWallWest(),
				   mahjongState.getWallSouth(),
				   getTileListFromIndexList(mahjongState.getChiByE()),
				   getTileListFromIndexList(mahjongState.getPengByE()),
				   getTileListFromIndexList(mahjongState.getGangByE()),
				   getTileListFromIndexList(mahjongState.getChiByN()),
				   getTileListFromIndexList(mahjongState.getPengByN()),
				   getTileListFromIndexList(mahjongState.getGangByN()),
				   getTileListFromIndexList(mahjongState.getChiByW()),
				   getTileListFromIndexList(mahjongState.getPengByW()),
				   getTileListFromIndexList(mahjongState.getGangByW()),
				   getTileListFromIndexList(mahjongState.getChiByS()),
				   getTileListFromIndexList(mahjongState.getPengByS()),
				   getTileListFromIndexList(mahjongState.getGangByS()),
				   mahjongState.getTiles().get(mahjongState.getSpecialTile()).get(),
				   getTileListFromIndexList(mahjongState.getCastByE()),
				   getTileListFromIndexList(mahjongState.getCastByN()),
				   getTileListFromIndexList(mahjongState.getCastByW()),
				   getTileListFromIndexList(mahjongState.getCastByS()),
				   MahjongMessage.INVISIBLE_);
		   return;
	   }
	   if(updateUI.isAiPlayer()) {
		   return;
	   }
	   
	   MahjongMessage mahjongMessage = getMahjongMessage();
	    
	   view.setPlayerState(
			   mahjongState.getOneOfFourTile(opponent1).size(),
			   getTileListFromIndexList(mahjongState.getOneOfFourChi(opponent1)),
			   getTileListFromIndexList(mahjongState.getOneOfFourPeng(opponent1)),
			   getTileListFromIndexList(mahjongState.getOneOfFourGang(opponent1)),
			   getTileListFromIndexList(mahjongState.getOneOfFourCast(opponent1)),
			   mahjongState.getOneOfFourWall(opponent1),
			   mahjongState.getOneOfFourTile(opponent2).size(),
			   getTileListFromIndexList(mahjongState.getOneOfFourChi(opponent2)),
			   getTileListFromIndexList(mahjongState.getOneOfFourPeng(opponent2)),
			   getTileListFromIndexList(mahjongState.getOneOfFourGang(opponent2)),
			   getTileListFromIndexList(mahjongState.getOneOfFourCast(opponent2)),
			   mahjongState.getOneOfFourWall(opponent2),
			   mahjongState.getOneOfFourTile(opponent3).size(),
			   getTileListFromIndexList(mahjongState.getOneOfFourChi(opponent3)),
			   getTileListFromIndexList(mahjongState.getOneOfFourPeng(opponent3)),
			   getTileListFromIndexList(mahjongState.getOneOfFourGang(opponent3)),
			   getTileListFromIndexList(mahjongState.getOneOfFourCast(opponent3)),
			   mahjongState.getOneOfFourWall(opponent3),
			   getTileListFromIndexList(mahjongState.getOneOfFourTile(myP)),
			   getTileListFromIndexList(mahjongState.getOneOfFourChi(myP)),
			   getTileListFromIndexList(mahjongState.getOneOfFourPeng(myP)),
			   getTileListFromIndexList(mahjongState.getOneOfFourGang(myP)),
			   getTileListFromIndexList(mahjongState.getOneOfFourCast(myP)),
			   mahjongState.getOneOfFourWall(myP),
			   mahjongState.getTiles().get(mahjongState.getSpecialTile()).get(),
			   mahjongState.getOneOfFourTile(myP),
			   mahjongMessage);
	   
	   if(mahjongMessage.equals(MahjongMessage.AUTO_CHI_CHECK_)
		 || mahjongMessage.equals(MahjongMessage.AUTO_HU_CHECK_)
		 || mahjongMessage.equals(MahjongMessage.AUTO_PENG_CHECK_)
		 || mahjongMessage.equals(MahjongMessage.WAIT_CHI_CHOICE_)
		 || mahjongMessage.equals(MahjongMessage.WAIT_HU_CHOICE_)
		 || mahjongMessage.equals(MahjongMessage.WAIT_PENG_CHOICE_)) {
		   return;
	   }
	   
	   if(isMyTurn()) {
		   if(mahjongState.isChi()) {
			   checkIfChiMove();
		   }else if(mahjongState.isPeng()) {
			   checkIfPengMove();
		   }else if(mahjongState.isHu()) {
			   checkIfHuMove();
		   }else if(canDelcareHuMove()) {
			   declareHuMove();
		   }else if(canDeclarePengMove()) {
			   choosePengTiles();
		   }else if(canDeclareChiMove()) {
			   chooseChiTiles();
		   }else if(mahjongState.getOneOfFourTile(myP).size()%3 != 2) {
			   declareFetch();
		   }else{
			   chooseCastTile();
		   }
	   }
	}
	
	/**
	 * Called by view only when view receives the message AUTO_HU_CHECK_
	 */
	public void autoHuCheck() {
		check(isMyTurn() && mahjongState.isHuCheckStatus());
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn("-1"));
		boolean tmp =  MahjongLogicAnalysis.canHuCheck(mahjongState, myPosition.get());
		operations.add(new Set("huIsAllowed", tmp));
		container.sendMakeMove(mahjongLogic.declareAutoHuCheckMove(mahjongState, operations));
	}
	
	/**
	 * Called by view only when view receives the message AUTO_PENG_CHECK_
	 */
	public void autoPengCheck() {
		check(isMyTurn() && mahjongState.isPengCheckStatus());
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn("-1"));
		operations.add(new Set("pengIsAllowed", MahjongLogicAnalysis.canPengCheck(mahjongState, myPosition.get())));
		container.sendMakeMove(mahjongLogic.declareAutoPengCheckMove(mahjongState, operations));
	}
	
	/**
	 * Called by view only when view receives the message AUTO_CHI_CHECK_
	 */
	public void autoChiCheck() {
		check(isMyTurn() && mahjongState.isChiCheckStatus());
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn("-1"));
		operations.add(new Set("chiIsAllowed", MahjongLogicAnalysis.canChiCheck(mahjongState, myPosition.get())));
		container.sendMakeMove(mahjongLogic.declareAutoChiCheckMove(mahjongState, operations));
	}
	
	/**
	 * Called by view only when view receives the message WAIT_HU_CHOICE_
	 * @param choice
	 */
	public void waitForHuChoice(boolean choice) {
		check(isMyTurn() && mahjongState.isHuCheckStatus());
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn("-1"));
		operations.add(new Set("choiceForHu", choice));
		container.sendMakeMove(mahjongLogic.waitForHuChoice(mahjongState, operations));
	}
	
	/**
	 * Called by view only when view receives the message WAIT_PENG_CHOICE_
	 * @param choice
	 */
	public void waitForPengChoice(boolean choice) {
		check(isMyTurn() && mahjongState.isPengCheckStatus());
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn("-1"));
		operations.add(new Set("choiceForPeng", choice));
		container.sendMakeMove(mahjongLogic.waitForPengChoice(mahjongState, operations));
	}
	
	/**
	 * Called by view only when view receives the message WAIT_CHI_CHOICE_
	 * @param choice
	 */
	public void waitForChiChoice(boolean choice) {
		check(isMyTurn() && mahjongState.isChiCheckStatus());
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn("-1"));
		operations.add(new Set("choiceForChi", choice));
		container.sendMakeMove(mahjongLogic.waitForChiChoice(mahjongState, operations));
	}
	
	//Just a helper method
	public MahjongState getMahjongState() {
		return mahjongState;
	}
	
	/**
	 * Let the view to choose the peng tiles
	 */
	private void choosePengTiles() {
		view.choosePengTiles(selectedPengTiles, mahjongLogic.subtract_(getMyTiles(), selectedPengTiles),
			selectedPengTileIndexes, mahjongLogic.subtract_(mahjongState.getOneOfFourTile(myPosition.get()), selectedPengTileIndexes));
	}
	
	/**
	 * Let the view to choose the chi tiles
	 */
	private void chooseChiTiles() {
		view.chooseChiTiles(selectedChiTiles, mahjongLogic.subtract_(getMyTiles(), selectedChiTiles),
			selectedChiTileIndexes, mahjongLogic.subtract_(mahjongState.getOneOfFourTile(myPosition.get()), selectedChiTileIndexes));
	}
	
	/**
	 * Let the view to choose the cast tile
	 */
	private void chooseCastTile() {
		view.chooseCastTile(selectedCastTile, mahjongLogic.subtract_(getMyTiles(), selectedCastTile),
				selectedCastTileIndex, mahjongLogic.subtract_(mahjongState.getOneOfFourTile(myPosition.get()), selectedCastTileIndex));
	}
	
	public List<Tile> getMyTiles() {
		List<Tile> myTiles = Lists.newArrayList();
		ImmutableList<Optional<Tile>> tiles = mahjongState.getTiles();
		for(Integer tileIndex : mahjongState.getOneOfFourTile(myPosition.get())) {
			myTiles.add(tiles.get(tileIndex).get());
		}
		return myTiles;
	}
	
	/**
	 * View will invoke this method when the player wants to choose the peng Tiles
	 * @param pengTile
	 * @param pengTileNum
	 */
	public void pengTilesSelected(Tile pengTile, int pengTileNum) {
		check(isMyTurn() && !mahjongState.isPeng());
		if(selectedPengTiles.size()<2){
			selectedPengTiles.add(pengTile);
			selectedPengTileIndexes.add(pengTileNum);
		}
		choosePengTiles();
	}
	
	/**
	 * View will invoke this method when the player wants to choose the chi tiles
	 * @param chiTile
	 * @param chiTileNum
	 */
	public void chiTilesSelected(Tile chiTile, int chiTileNum) {
		check(isMyTurn() && !mahjongState.isChi());
		if(selectedChiTiles.size()<2) {
			selectedChiTiles.add(chiTile);
			selectedChiTileIndexes.add(chiTileNum);
		}	
		chooseChiTiles();
	}
	
	/**
	 * View will invoke this method when the player wants to choose the cast Tile
	 * @param castTile
	 * @param castTileNum
	 */
	public void castTileSelected(Tile castTile, int castTileNum) {
		check(isMyTurn());
		if(selectedCastTile.size()==0) {
			selectedCastTile.add(castTile);
			selectedCastTileIndex.add(castTileNum);
		}
		chooseCastTile();
	}
	
	/**
	 * Finish the peng Tiles choose
	 */
	public void finishedSelectingPengTiles() {
		check(isMyTurn() && selectedPengTiles.size()==2);
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn("-1"));
		operations.add(new Set("isPeng","yes"));
		operations.add(new Set("peng",ImmutableList.of(selectedPengTileIndexes.get(0),selectedPengTileIndexes.get(1))));
		container.sendMakeMove(mahjongLogic.declarePengMove(mahjongState, operations));
	}
	
	/**
	 * Finish the chi Tiles choose
	 */
	public void finishedSelectingChiTiles() {
		check(isMyTurn() && selectedChiTiles.size()==2);
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn("-1"));
		operations.add(new Set("isChi", "yes"));
		operations.add(new Set("chi", ImmutableList.of(selectedChiTileIndexes.get(0),selectedChiTileIndexes.get(1))));
		container.sendMakeMove(mahjongLogic.declareChiMove(mahjongState, operations));
	}
	
	/**
	 * Finish the cast Tile choose
	 */
	public void finishedSelectingCastTile() {
		check(isMyTurn());
		List<Operation> operations = Lists.newArrayList();
		operations.add(new SetTurn("-1"));
		operations.add(new Set("cast", selectedCastTileIndex.get(0)));
		container.sendMakeMove(mahjongLogic.doCastMove(mahjongState, operations));
	}
	
	/**
	 * Send the fetch declare move
	 */
	private void declareFetch() {
		container.sendMakeMove(mahjongLogic.declareFetchMove(mahjongState));
	}
	
	/**
	 * Send the chi check move
	 */
	private void checkIfChiMove() {
		container.sendMakeMove(mahjongLogic.checkIfChiMove(mahjongState));
	}
	
	/**
	 * Send the peng check move
	 */
	private void checkIfPengMove() {
		container.sendMakeMove(mahjongLogic.checkIfPengMove(mahjongState));
	}
	
	/**
	 * Send the hu check move
	 */
	private void checkIfHuMove() {
		container.sendMakeMove(mahjongLogic.checkIfHuMove(mahjongState));
	}
	
	/**
	 * Send the hu declare move
	 */
	private void declareHuMove() {
		container.sendMakeMove(mahjongLogic.declareHuMove(mahjongState));
	}
	
	/**
	 * helper method:List<Integer> -> List<Tile>
	 * @param indexList
	 * @return List<Tile>
	 */
	public List<Tile> getTileListFromIndexList(List<Integer> indexList) {
		List<Tile> tileList = new ArrayList<Tile>();
		for(int i=0;i<indexList.size();i++) {
			tileList.add(tiles.get(indexList.get(i)).get());
		}
		return tileList;
	}
	
	/**
	 * return true if the next move is produced by me or false
	 * @return
	 */
	private boolean isMyTurn() {
		return myPosition.isPresent() && myPosition.get() == mahjongState.getTurn();
	}
	
	private void sendInitialMoveOne(List<String> playerIds){
		container.sendMakeMove(mahjongLogic.getInitialMoveOne(playerIds));
	}
	
	private void sendInitialMoveTwo(List<String> playerIds){
		container.sendMakeMove(mahjongLogic.getInitialMoveTwo(playerIds));
	}
	
	/**
	 * Can return the corresponding message based on some information
	 * @return
	 */
	private MahjongMessage getMahjongMessage() {
		if(canDeclareAutoHuCheckMove()) {
			return MahjongMessage.AUTO_HU_CHECK_;
		}else if(canWaitForHuChoice()) {
			return MahjongMessage.WAIT_HU_CHOICE_;
		}else if(canDeclareAutoPengCheckMove()) {
			return MahjongMessage.AUTO_PENG_CHECK_;
		}else if(canWaitForPengChoice()) {
			return MahjongMessage.WAIT_PENG_CHOICE_;
		}else if(canDeclareAutoChiCheckMove()) {
			return MahjongMessage.AUTO_CHI_CHECK_;
		}else if(canWaitForChiChoice()) {
			return MahjongMessage.WAIT_CHI_CHOICE_;
		}else{
			return MahjongMessage.NO_MOVE_;
		}
	}

	private boolean canDeclareAutoHuCheckMove() {
		return isMyTurn()
		    && mahjongState.isHuCheckStatus()
		    && mahjongState.isHuIsAllowed() == -1;
	}
	
	private boolean canWaitForHuChoice() {
		return isMyTurn()
			&& mahjongState.isHuCheckStatus()
			&& mahjongState.isHuIsAllowed() == 1;
	}
	
	private boolean canDelcareHuMove() {
		return isMyTurn()
			&& !mahjongState.isHuCheckStatus()
			&& mahjongState.isHuStatus()
			&& !mahjongState.isHu();
	}
	
	private boolean canDeclareAutoPengCheckMove() {
		return isMyTurn()
			&& mahjongState.isPengCheckStatus()
			&& mahjongState.isPengIsAllowed() == -1;
	}
	
	private boolean canWaitForPengChoice() {
		return isMyTurn()
			&& mahjongState.isPengCheckStatus()
			&& mahjongState.isPengIsAllowed() == 1;
	}
	
	private boolean canDeclarePengMove() {
		return isMyTurn()
			&& !mahjongState.isPengCheckStatus() 
			&& mahjongState.isPengStatus()
			&& !mahjongState.isPeng();
	}

	private boolean canDeclareAutoChiCheckMove() {
		return isMyTurn()
			&& mahjongState.isChiCheckStatus()
			&& mahjongState.isChiIsAllowed() == -1;
	}
	
	private boolean canWaitForChiChoice() {
		return isMyTurn()
			&& mahjongState.isChiCheckStatus()
			&& mahjongState.isChiIsAllowed() == 1;
 	}
	
	private boolean canDeclareChiMove() {
		return isMyTurn()
			&& !mahjongState.isChiCheckStatus()
			&& mahjongState.isChiStatus()
			&& !mahjongState.isChi();
	}

	//Check out the exceptions
	private void check(boolean val) {
		if(!val) {
			throw new IllegalArgumentException();
		}
	}
}
