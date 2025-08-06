package com.fierydragon.components.level.object;

import java.util.Map;

import com.fierydragon.components.enums.CardType;
import com.fierydragon.components.interfaces.ICardDelegate;
import com.fierydragon.core.framework.ResourceLoader;
import com.fierydragon.core.level.GameLevel;
import com.fierydragon.core.level.GameObject;

import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * The CardController class manages the behavior and animations of a card within the game.
 * 
 * @author Chang Yi Zhong
 * @author Lim Hung Xuan
 */
public abstract class CardController extends GameObject {
    static boolean transitionActive = false;

    private final String cardbackPath = "chit_back.png";

    private Circle cardShape;
    CardType cardType;
    int cardValue;

    public Image cardbackImage;
    public Image cardImage;
    public ImageView cardImageView;

    ICardDelegate delegate;

    boolean flipped = false;

    // ===============
    // Flip over anim attributes
    private RotateTransition rotate1;
    private RotateTransition rotate2;
    private PauseTransition flipOverPause;
    // ===============

    /**
     * Constructs a CardController object.
     * 
     * @param gameLevel the game level to which this card belongs
     * @param cardType the type of the card
     * @param cardValue the value of the card
     */
    public CardController(GameLevel gameLevel, CardType cardType, int cardValue) {
        super(gameLevel);

        this.cardType = cardType;
        this.cardValue = cardValue;

        this.cardbackImage = new Image(ResourceLoader.ASSETS.getPath() + this.cardbackPath);
        this.cardImage = new Image(ResourceLoader.ASSETS.getPath() + this.cardType.getResourcePath() + "chit" + "_" + Math.abs(this.cardValue) + ".png");

        this.cardImageView = new ImageView(this.cardbackImage);
        // TODO: comment when not used for testing purposes
        // this.cardImageView = new ImageView(this.cardImage);

        // ===============
        // Flip over anim setup
        this.rotate2 = new RotateTransition(Duration.seconds(0.15), this.cardImageView);
        this.rotate1 = new RotateTransition(Duration.seconds(0.15), this.cardImageView);

        this.rotate1.setAxis(new Point3D(0, 1, 0));
        this.rotate1.setFromAngle(0);
        this.rotate1.setToAngle(90);
        this.rotate1.setInterpolator(Interpolator.LINEAR);
        this.rotate1.setCycleCount(1);
        this.rotate1.setOnFinished(e -> this.cardHalfFlip());

        this.rotate2.setAxis(new Point3D(0, 1, 0));
        this.rotate2.setFromAngle(90);
        this.rotate2.setToAngle(0);
        this.rotate2.setInterpolator(Interpolator.LINEAR);
        this.rotate2.setCycleCount(1);
        this.rotate2.setOnFinished(e -> this.flipOverPause.play());
        // ==============

        // ===============
        // Pause then call delegate(otherwise if wrong card selected the player might not have enough time to view the card before flipbacking)
        this.flipOverPause = new PauseTransition(Duration.seconds(0.2));
        this.flipOverPause.setOnFinished(e -> this.cardFlipped());
        // ==============
    }

    /**
     * Sets the bounds of the card using a Circle shape.
     * 
     * @param cardShape the Circle representing the card's bounds
     */
    public void setBounds(Circle cardShape) {
        this.objectRoot.getChildren().clear();

        this.cardShape = cardShape;
        
        this.cardImageView.setX(this.cardShape.getCenterX() - this.cardShape.getRadius());
        this.cardImageView.setY(this.cardShape.getCenterY() - this.cardShape.getRadius());
        this.cardImageView.setFitWidth(this.cardShape.getRadius() * 2);
        this.cardImageView.setFitHeight(this.cardShape.getRadius() * 2);

        this.cardImageView.setOnMouseClicked((e) -> handleCardClicked(e));

        this.objectRoot.getChildren().add(this.cardImageView);
    }

    /**
     * Sets the delegate that will handle card actions.
     * 
     * @param delegate the delegate to handle card actions
     */
    public void setDelegate(ICardDelegate delegate) {
        this.delegate = delegate;
    }

    /**
     * Handles the card click event.
     * 
     * @param e the mouse event triggered by clicking the card
     */
    private void handleCardClicked(MouseEvent e) {
        if(e.getButton() == MouseButton.PRIMARY && !this.flipped && !CardController.transitionActive) {
            if(!CardController.transitionActive) {
                CardController.transitionActive = true;
                this.rotate1.play();
            }
        }
    }

    /**
     * Flips the card back to its back side.
     */
    public void flipBack() {
        this.cardImageView.setImage(this.cardbackImage);
        this.flipped = false;
    }

    public void flipOver() {
        this.cardImageView.setImage(this.cardImage);
        this.flipped = true;
    }

    /**
     * Performs half of the flip animation, switching the card's image.
     */
    public void cardHalfFlip() {
        if(!this.flipped) {
            this.cardImageView.setImage(this.cardImage);
            this.flipped = true;
        } else {
            this.cardImageView.setImage(this.cardbackImage);
            this.flipped = false;
        }

        this.rotate2.play();
    }

    /**
     * Handles the card flip event and notifies the delegate if the card is flipped.
     */
    public abstract void cardFlipped() ;


    /**
     * Saves the card's state into the provided properties map.
     *
     * @param properties the map to populate with the card's state
     * @param prefix a prefix to prepend to property keys (used for nested objects)
     */
    @Override
    public void invokeSave(Map<String, String> properties, String prefix) {
        properties.put(prefix + ".cardType", this.cardType.toString());
        properties.put(prefix + ".cardValue", String.valueOf(this.cardValue));
        properties.put(prefix + ".flipped", String.valueOf(this.flipped));
    }
}
