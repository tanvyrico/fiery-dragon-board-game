package com.fierydragon.components.interfaces;

import com.fierydragon.components.enums.CardType;

/**
 * Card delegate interface
 * 
 * @author Chang Yi Zhong
 */
public interface ICardDelegate {
    /**
     * Handle card action by delegate class
     * 
     * @param cardType
     * @param cardValue
     */
    public void handleCardAction(CardType cardType, int cardValue);

    /**
     * Handle swapping card action by delegate class
     *
     * @param cardType
     * @param cardValue
     */
    public void handleSwapCardAction();
}
