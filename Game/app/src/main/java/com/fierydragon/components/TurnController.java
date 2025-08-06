package com.fierydragon.components;

/**
 * The TurnController class manages the turn order for players in the game.
 * 
 * @author Chang Yi Zhong
 * @author Enrico Tanvy
 */
public class TurnController {
    private int turnPlayerID;
    private int playerCount;


    /**
     * Constructs a TurnController object with a specified starting player ID and player count.
     *
     * @param startPlayerID the ID of the player who starts the game
     * @param playerCount the total number of players in the game
     */
    public TurnController(int startPlayerID, int playerCount) {
        this.turnPlayerID = startPlayerID;
        this.playerCount = playerCount;
    }

    /**
     * Constructs a TurnController object with a default starting player ID of 0.
     *
     * @param playerCount the total number of players in the game
     */
    public TurnController(int playerCount) {
        this(0, playerCount);
    }

    /**
     * Gets the ID of the player whose turn it is.
     *
     * @return the ID of the current turn player
     */
    public int getTurnPlayerID() {
        return this.turnPlayerID;
    }

    /**
     * Advances to the next player's turn.
     */
    public void nextTurn() {
        this.turnPlayerID = (this.turnPlayerID + 1) % this.playerCount;
    }

    /**
     * Resets the turn order to start with the first player.
     */
    public void reset() {
        this.turnPlayerID = 0;
    }
}
