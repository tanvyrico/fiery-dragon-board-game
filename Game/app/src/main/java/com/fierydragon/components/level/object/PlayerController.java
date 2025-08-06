package com.fierydragon.components.level.object;

import java.util.Map;

import com.fierydragon.components.enums.CardType;
import com.fierydragon.core.level.GameLevel;
import com.fierydragon.core.level.GameObject;

import javafx.scene.shape.Circle;

/**
 * The PlayerController class manages the player's position and state within the game level.
 * 
 * @author Chang Yi Zhong
 * @author Lim Hung Xuan
 */
public class PlayerController extends GameObject {
    private int caveTileID;
    private int currentTileID;
    private CardType currentTileType;
    private int tilesLeft;
    private int currentCaveID;

    private Circle playerCharacter;

    /**
     * Constructs a PlayerController object.
     *
     * @param gameLevel the game level to which the player belongs
     * @param caveType the type of the starting cave
     * @param caveTileID the ID of the starting cave tile
     * @param totalTiles the total number of tiles the player can move through
     */
    public PlayerController(GameLevel gameLevel, CardType caveType, int caveTileID, int totalTiles,int currentCaveID) {
        super(gameLevel);
        this.tilesLeft = totalTiles + 1;
        this.caveTileID = caveTileID;
        this.currentCaveID = currentCaveID;
        this.currentTileID = 0;
        this.currentTileType = caveType;
    }

    /**
     * Sets the current tile ID the player is on.
     *
     * @param tileID the ID of the current tile
     */
    public void setCurrentTileID(int tileID) {
        this.currentTileID = tileID;
    }

    /**
     * Gets the current tile ID the player is on.
     *
     * @return the ID of the current tile
     */
    public int getCurrentTileID() {
        return this.currentTileID;
    }

    /**
     * Gets the cave tile ID where the player starts.
     *
     * @return the ID of the cave tile
     */
    public int getCaveTileID() {
        return this.caveTileID;
    }

    public void setCaveTileID(int caveTileID){this.caveTileID = caveTileID;}

    public int getCurrentCaveID(){return  this.currentCaveID;}

    public void setCurrentCaveID(int currentCaveID){this.currentCaveID = currentCaveID;}

    /**
     * Sets the type of the current tile the player is on.
     *
     * @param tileType the type of the current tile
     */
    public void setCurrentTileType(CardType tileType) {
        this.currentTileType = tileType;
    }

    /**
     * Gets the type of the current tile the player is on.
     *
     * @return the type of the current tile
     */
    public CardType getCurrentTileType() {
        return this.currentTileType;
    }

    /**
     * Sets the number of tiles left for the player to move through.
     *
     * @param tilesLeft the number of tiles left
     */
    public void setTilesLeft(int tilesLeft) {
        this.tilesLeft = tilesLeft;
    }

    /**
     * Gets the number of tiles left for the player to move through.
     *
     * @return the number of tiles left
     */
    public int getTilesLeft() {
        return this.tilesLeft;
    }

    /**
     * Gets the Circle representing the player's character.
     *
     * @return the Circle representing the player
     */
    public Circle getPlayerCharacter() {
        return this.playerCharacter;
    }

    /**
     * Sets the bounds of the player's character using a Circle shape.
     *
     * @param playerCharacter the Circle representing the player's character
     */
    @Override
    public void setBounds(Circle playerCharacter) {
        this.objectRoot.getChildren().clear();

        this.playerCharacter = playerCharacter;

        this.objectRoot.getChildren().add(this.playerCharacter);
    }

    @Override
    public void invokeSave(Map<String, String> properties, String prefix) {
        properties.put(prefix + ".caveTileID", String.valueOf(this.caveTileID));
        properties.put(prefix + ".currentTileID", String.valueOf(this.currentTileID));
        properties.put(prefix + ".currentTileType", this.currentTileType.toString());
        properties.put(prefix + ".tilesLeft", String.valueOf(this.tilesLeft));
    }
}
