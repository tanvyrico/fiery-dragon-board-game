package com.fierydragon.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import com.fierydragon.components.UI.GameWinUI;
import com.fierydragon.components.UI.MainMenuUI;
import com.fierydragon.components.UI.PauseMenuUI;
import com.fierydragon.components.enums.CardType;
import com.fierydragon.components.level.GameBoard;
import com.fierydragon.core.framework.GameModeBase;
import com.fierydragon.core.framework.StageController;

import javafx.util.Pair;
/**
 * The BasicGameMode class represents a basic game mode for the Fiery Dragon game, managing the game setup, gameplay, and win condition.
 * 
 * @author Chang Yi Zhong
 * @author Enrico Tanvy
 */
public class BasicGameMode extends GameModeBase {
    private GameWinUI gameWinUI;
    private MainMenuUI mainMenuUI;
    private PauseMenuUI pauseMenuUI;

    private int playerCount;
    private int volcanoCardNum;

    private final ArrayList<CardType> tileTypes = new ArrayList<CardType>(Arrays.asList(
        CardType.SALAMANDER,
        CardType.BABYDRAGON,
        CardType.BAT,
        CardType.SPIDER
    ));
    private final ArrayList<CardType> caveTypes = new ArrayList<CardType>(Arrays.asList(
        CardType.SALAMANDER,
        CardType.BABYDRAGON,
        CardType.BAT,
        CardType.SPIDER
    ));
    private final ArrayList<Pair<CardType, Integer>> dragonCardValues = new ArrayList<Pair<CardType, Integer>>(Arrays.asList(
        new Pair<CardType, Integer>(CardType.PIRATE_DRAGON, -1),
        new Pair<CardType, Integer>(CardType.PIRATE_DRAGON, -1),
        new Pair<CardType, Integer>(CardType.PIRATE_DRAGON, -2),
        new Pair<CardType, Integer>(CardType.PIRATE_DRAGON, -2),
        new Pair<CardType, Integer>(CardType.BAT, 1),
        new Pair<CardType, Integer>(CardType.BAT, 2),
        new Pair<CardType, Integer>(CardType.BAT, 3),
        new Pair<CardType, Integer>(CardType.BABYDRAGON, 1),
        new Pair<CardType, Integer>(CardType.BABYDRAGON, 2),
        new Pair<CardType, Integer>(CardType.BABYDRAGON, 3),
        new Pair<CardType, Integer>(CardType.SALAMANDER, 1),
        new Pair<CardType, Integer>(CardType.SALAMANDER, 2),
        new Pair<CardType, Integer>(CardType.SALAMANDER, 3),
        new Pair<CardType, Integer>(CardType.SPIDER, 1),
        new Pair<CardType, Integer>(CardType.SPIDER, 2),
        new Pair<CardType, Integer>(CardType.SPIDER, 3),
        new Pair<CardType, Integer>(CardType.SWAP, 1),
        new Pair<CardType, Integer>(CardType.SWAP, 1)
    ));

    private TurnController turnController;


    /**
     * Constructs a BasicGameMode object.
     *
     * @param mainMenuUI the main menu UI to return to
     * @param playerCount the number of players in the game
     * @param volcanoCardNum the number of volcano cards in the game
     */
    public BasicGameMode(MainMenuUI mainMenuUI, int playerCount, int volcanoCardNum) {
        this.mainMenuUI = mainMenuUI;
        this.pauseMenuUI = new PauseMenuUI(this.mainMenuUI);
        this.playerCount = playerCount;
        this.turnController = new TurnController(playerCount);
        this.volcanoCardNum = volcanoCardNum;

        ArrayList<CardType> caveList = new ArrayList<CardType>();
        Collections.shuffle(this.caveTypes);
        for(int i = 0; i < this.playerCount; i++) {
            caveList.add(this.caveTypes.get(i));
        }

        this.gameLevel = new GameBoard(this, this.turnController, caveList, this.tileTypes, this.volcanoCardNum, this.dragonCardValues);
        this.gameWinUI = new GameWinUI(this, this.gameManager, this.turnController, this.mainMenuUI);
    }

    public BasicGameMode(MainMenuUI mainMenuUI, Map<String, String> properties) {
        this.mainMenuUI = mainMenuUI;
        this.pauseMenuUI = new PauseMenuUI(this.mainMenuUI);
        this.playerCount = Integer.parseInt(properties.get("playerCount"));

        int startingPlayer = Integer.parseInt(properties.get("currentPlayer")) - 1;
        this.turnController = new TurnController(startingPlayer, playerCount);

        this.gameLevel = new GameBoard(this, this.turnController, properties);
        this.gameWinUI = new GameWinUI(this, this.gameManager, this.turnController, this.mainMenuUI);
    }

    /**
     * Handles the win condition by displaying the game win UI.
     */
    @Override
    public void handleWin() {
        StageController.INSTANCE.drawSceneHolder(this.gameWinUI);
    }

    @Override
    public void handlePause() {
        this.pauseMenuUI.setPreviousSceneHolder(this.gameLevel);
        this.pauseMenuUI.setSaveTarget(this.gameLevel);
        StageController.INSTANCE.drawSceneHolder(this.pauseMenuUI);
    }
}