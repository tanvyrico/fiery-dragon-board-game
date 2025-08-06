package com.fierydragon.components.level.object;

import com.fierydragon.components.enums.CardType;
import com.fierydragon.core.level.GameLevel;


/**
 * The CardFactory class provides a factory method to create instances of CardController
 * based on the specified card type and value.
 */
public class CardFactory {


    /**
     * Creates a CardController based on the given cardType and cardValue.
     *
     * @param gameLevel the game level associated with the card
     * @param cardType the type of the card
     * @param cardValue the value of the card
     * @return a {@code CardController} corresponding to the specified card type and value
     */
    public static CardController createCardController(GameLevel gameLevel, CardType cardType, int cardValue ) {
        switch (cardType) {
            case SWAP:
                return new SpecialCardController(gameLevel,cardType,cardValue);
            default:
                return new BasicCardController(gameLevel,cardType,cardValue);
        }
    }
}
