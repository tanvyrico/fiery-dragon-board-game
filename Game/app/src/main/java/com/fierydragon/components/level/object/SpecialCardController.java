package com.fierydragon.components.level.object;

import com.fierydragon.components.enums.CardType;
import com.fierydragon.core.level.GameLevel;


/**
 * Subclasses of Card Controller that represent the chit card that swap with nearest player when clicked
 *
 * @author Enrico Tanvy
 * @author Lim Hung Xuan
 */
public class SpecialCardController extends CardController{

    /**
     * Constructs a CardController object.
     *
     * @param gameLevel the game level to which this card belongs
     * @param cardType  the type of the card
     * @param cardValue the value of the card
     */
    public SpecialCardController(GameLevel gameLevel, CardType cardType, int cardValue) {
        super(gameLevel, cardType, cardValue);
    }

    /**
     * Handles the card flip event and notifies the delegate if the card is flipped.
     */
    @Override
    public void cardFlipped() {
        if(this.flipped) {
            this.delegate.handleSwapCardAction();
        }
        CardController.transitionActive = false;
    }
}
