package com.fierydragon.core.framework;

import com.fierydragon.core.level.GameLevel;

/**
 * The base class for different game modes in the Fiery Dragons game.
 * 
 * This class provides a template for initializing a game mode,
 * handling win conditions, and managing game levels.
 * Subclasses must implement the abstract method handleWin to define
 * specific win handling behavior.
 * 
 * @author Chang Yi Zhong
 * @author Lim Hung Xuan
 */
public abstract class GameModeBase {
    protected GameManager gameManager;
    protected GameLevel gameLevel;

    /**
     * Initializes the game mode
     * 
     * Runs when the game manager starts the game
     * 
     * @param gameManager the GameManager instance managing the game
     */
    public void initGame(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Handle winning
     * 
     * Subclasses should implement this method to define the behavior when a game is won.
     */
    public abstract void handleWin();

    public abstract void handlePause();
    
    /**
     * Get game level
     * 
     * @return Game level the current game level being played
     */
    public GameLevel getGameLevel() {
        return this.gameLevel;
    }
}
