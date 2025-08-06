package com.fierydragon.components.level.object;

import java.util.ArrayList;
import java.util.Map;

import com.fierydragon.components.enums.CardType;
import com.fierydragon.core.interfaces.ISaveable;


/**
 * VolcanoCard represents a card in the game that holds a list of tile types.
 * It implements the ISaveable interface, allowing its state to be saved.
 *
 * @author : Lim Hung Xuan
 */
public class VolcanoCard implements ISaveable {
    private ArrayList<CardType> tileTypes;


    /**
     * Constructs a VolcanoCard object with the specified list of tile types.
     *
     * @param tileTypes the list of tile types associated with this card
     */
    public VolcanoCard(ArrayList<CardType> tileTypes) {
        this.tileTypes = tileTypes;
    }



    /**
     * Returns the list of tile types associated with this card.
     *
     * @return the list of tile types
     */
    public ArrayList<CardType> getTileTypes() {
        return this.tileTypes;
    }



    /**
     * Returns the list of tile types associated with this card.
     *
     * @return the list of tile types
     */
    public CardType getTileType(int index) {
        if(index < 0 || index >= this.tileTypes.size()) {
            return null;
        }
        return this.tileTypes.get(index);
    }


    /**
     * Saves the state of this VolcanoCard into the provided properties map.
     *
     * @param properties the map to populate with the card's state
     * @param prefix a prefix to prepend to property keys (used for nested objects)
     */
    @Override
    public void invokeSave(Map<String, String> properties, String prefix) {
        for(int i = 0; i < this.tileTypes.size(); i++) {
            String key = prefix + ".tile" + (i + 1);
            properties.put(key, this.tileTypes.get(i).toString());
        }
    }
}
