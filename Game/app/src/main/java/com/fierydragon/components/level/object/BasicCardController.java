package com.fierydragon.components.level.object;

import com.fierydragon.components.enums.CardType;
import com.fierydragon.core.level.GameLevel;



/**
 * Subclasses of Card Controller that represent the basic chit card
 *
 * @author Enrico Tanvy
 */

public class BasicCardController extends CardController{

    /**
     * Constructs a CardController object.
     *
     * @param gameLevel the game level to which this card belongs
     * @param cardType  the type of the card
     * @param cardValue the value of the card
     */
    public BasicCardController(GameLevel gameLevel, CardType cardType, int cardValue) {
        super(gameLevel, cardType, cardValue);
    }


    /**
     * Handles the card flip event and notifies the delegate if the card is flipped.
     */
    public void cardFlipped() {
        if(this.flipped) {
            this.delegate.handleCardAction(this.cardType, this.cardValue);
        }
        CardController.transitionActive = false;
    }
}
