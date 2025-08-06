package com.fierydragon.core.framework;

/**
 * Game manager class for all task related to starting a game through GameMode
 * 
 * @author Chang Yi Zhong
 */
public class GameManager {
    private GameModeBase gameMode;

    /**
     * Starts a given GameMode
     * 
     * @param initGameMode
     */
    public void startGame(GameModeBase initGameMode) {
        this.gameMode = initGameMode;
        this.gameMode.initGame(this);
        StageController.INSTANCE.drawSceneHolder(this.gameMode.getGameLevel());
    }
}
