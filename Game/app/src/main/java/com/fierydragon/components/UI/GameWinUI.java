package com.fierydragon.components.UI;

import com.fierydragon.components.BaseUI;
import com.fierydragon.components.TurnController;
import com.fierydragon.core.framework.GameManager;
import com.fierydragon.core.framework.GameModeBase;
import com.fierydragon.core.framework.ResourceLoader;
import com.fierydragon.core.framework.StageController;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
/**
 * The GameWinUI class represents the user interface displayed when a player wins the game.
 * It includes options to play again or return to the main menu.
 * 
 * @author Chang Yi Zhong
 * @author Enrico Tanvy
 */
public class GameWinUI extends BaseUI {
    private GameModeBase gameMode;
    private GameManager gameManager;
    private TurnController turnController;
    private MainMenuUI mainMenuUI;

    private ImageView backgroundView;

    /**
     * Constructs a GameWinUI object.
     *
     * @param gameMode the game mode being played
     * @param gameManager the game manager controlling the game
     * @param turnController the turn controller managing player turns
     * @param mainMenuUI the main menu UI to return to
     */
    public GameWinUI(GameModeBase gameMode, GameManager gameManager, TurnController turnController, MainMenuUI mainMenuUI) {
        this.UIName = "GameWin";

        this.gameMode = gameMode;
        this.gameManager = gameManager;
        this.turnController = turnController;
        this.mainMenuUI = mainMenuUI;

        String backgroundPath = ResourceLoader.ASSETS.getPath() + "background.png";
        this.backgroundView = new ImageView(new Image(backgroundPath));
    }

    /**
     * Draws the GameWinUI elements on the screen, including the background, winner banner, and buttons.
     */
    @Override
    public void draw() {
        this.UIRoot.getChildren().clear();

        this.backgroundView.setX(0);
        this.backgroundView.setY(0);
        this.backgroundView.setFitWidth(this.UIScene.getWidth());
        this.backgroundView.setFitHeight(this.UIScene.getHeight());
        this.backgroundView.setOpacity(0.5);

        this.UIRoot.getChildren().add(this.backgroundView);

        // ===============
        // Winner Banner
        double bannerHeight = 0.35 * this.UIScene.getHeight();
        double bannerY = 0.3 * (this.UIScene.getHeight() - bannerHeight);

        Rectangle banner = new Rectangle(0, bannerY, this.UIScene.getWidth(), bannerHeight);
        banner.setFill(Color.web("#eb9b54"));

        Text bannerText = new Text(this.getWinnerString());
        bannerText.setFont(new Font(0.5 * banner.getHeight()));
        bannerText.setFill(Color.AZURE);
        bannerText.setStroke(Color.web("#eb9b54").darker());
        bannerText.setStrokeWidth(0.007 * banner.getHeight());
        

        double bannerTextX = 0.5 * (this.UIScene.getWidth() - bannerText.getLayoutBounds().getWidth());
        double bannerTextY = bannerY + 0.5 * (bannerHeight - bannerText.getLayoutBounds().getHeight());
        bannerText.relocate(bannerTextX, bannerTextY);

        this.UIRoot.getChildren().add(banner);
        this.UIRoot.getChildren().add(bannerText);
        // ===============

        // ===============
        // Banner buttons constraints
        double buttonHeight = 0.3 * bannerHeight;
        double minButtonWidth = 0.18 * this.UIScene.getWidth();
        double buttonSpacing = 1.2 * buttonHeight;

        // ===============
        // Reset button
        double resetButtonY = 1.05 * (bannerY + bannerHeight);
        // double resetButtonWidth = 1.8 * resetButtonHeight;
        // double resetButtonX = 0.5 * (this.UIScene.getWidth() - resetButtonWidth);

        Text resetText = new Text("PLAY AGAIN");
        resetText.setFont(new Font(0.4 * buttonHeight));
        resetText.setFill(Color.WHITESMOKE);

        double resetTextX = 0.5 * (this.UIScene.getWidth() - resetText.getLayoutBounds().getWidth());
        double resetTextY = resetButtonY + 0.5 * (buttonHeight - resetText.getLayoutBounds().getHeight());
        resetText.relocate(resetTextX, resetTextY);

        double resetButtonWidth = Math.max(minButtonWidth, resetText.getLayoutBounds().getWidth() * 1.2);
        double resetButtonX = 0.5 * (this.UIScene.getWidth() - resetButtonWidth);

        Rectangle resetButton = new Rectangle(resetButtonX, resetButtonY, resetButtonWidth, buttonHeight);
        resetButton.setArcHeight(buttonHeight);
        resetButton.setArcWidth(buttonHeight);
        resetButton.setFill(Color.web("#7ce66a"));

        resetButton.setOnMouseClicked(e -> this.handlePlayAgain());
        resetText.setOnMouseClicked(e -> this.handlePlayAgain());

        this.UIRoot.getChildren().add(resetButton);
        this.UIRoot.getChildren().add(resetText);
        // ==============

        // ===============
        // Main Menu button
        double mainMenuButtonY = resetButtonY + buttonSpacing;

        Text mainMenuText = new Text("MAIN MENU");
        mainMenuText.setFont(new Font(0.4 * buttonHeight));
        mainMenuText.setFill(Color.WHITESMOKE);

        double mainMenuTextX = 0.5 * (this.UIScene.getWidth() - mainMenuText.getLayoutBounds().getWidth());
        double mainMenuTextY = mainMenuButtonY + 0.5 * (buttonHeight - mainMenuText.getLayoutBounds().getHeight());
        mainMenuText.relocate(mainMenuTextX, mainMenuTextY);

        double mainMenuButtonWidth = Math.max(minButtonWidth, mainMenuText.getLayoutBounds().getWidth() * 1.2);
        double mainMenuButtonX = 0.5 * (this.UIScene.getWidth() - mainMenuButtonWidth);

        Rectangle mainMenuButton = new Rectangle(mainMenuButtonX, mainMenuButtonY, mainMenuButtonWidth, buttonHeight);
        mainMenuButton.setArcHeight(buttonHeight);
        mainMenuButton.setArcWidth(buttonHeight);
        mainMenuButton.setFill(Color.web("#fa564a"));

        mainMenuButton.setOnMouseClicked(e -> this.handleMainMenu());   
        mainMenuText.setOnMouseClicked(e -> this.handleMainMenu());

        this.UIRoot.getChildren().add(mainMenuButton);
        this.UIRoot.getChildren().add(mainMenuText);
    }

    /**
     * Gets the string representation of the winning player.
     *
     * @return the string indicating the winning player
     */
    private String getWinnerString() {
        return "PLAYER " + (this.turnController.getTurnPlayerID() + 1) + " WINS";
    }


    /**
     * Handles the action to play the game again.
     */
    private void handlePlayAgain() {
        this.gameManager.startGame(this.gameMode);
    }
    
    /**
     * Handles the action to return to the main menu.
     */
    private void handleMainMenu() {
        StageController.INSTANCE.drawSceneHolder(this.mainMenuUI);
    }
}
