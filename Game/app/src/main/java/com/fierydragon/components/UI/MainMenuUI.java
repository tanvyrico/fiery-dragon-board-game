package com.fierydragon.components.UI;

import java.io.File;
import java.util.Map;

import com.fierydragon.components.BaseUI;
import com.fierydragon.components.BasicGameMode;
import com.fierydragon.core.framework.GameManager;
import com.fierydragon.core.framework.ResourceLoader;
import com.fierydragon.core.framework.SaveHandler;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * The MainMenuUI class represents the main menu interface of the game, allowing the user to start a new game, adjust the player count, 
 * adjust the volcanoCard count, or exit the application.
 * 
 * @author Chang Yi Zhong
 * @author Lim Hung Xuan
 * 
 */
public class MainMenuUI extends BaseUI {
    private final int minPlayerCount = 2;
    private final int maxPlayerCount = 4;
    private int playerCount = this.minPlayerCount;
    
    // Add variables for volcanoCard count selection
    private final int minVolcanoCardCount = 8;
    private final int maxVolcanoCardCount = 21;
    private int volcanoCardCount = this.minVolcanoCardCount;

    private final GameManager gameManager;

    private ImageView menuBackground;
    private Text playerCountText;
    private Text volcanoCardCountText;

    /**
     * Constructs a MainMenuUI object.
     *
     * @param gameManager the game manager controlling the game
     */
    public MainMenuUI(GameManager gameManager) {
        this.UIName = "MainMenu";

        this.gameManager = gameManager;

        String menuBackgroundPath = ResourceLoader.ASSETS.getPath() + "background.png";
        this.menuBackground = new ImageView(new Image(menuBackgroundPath));

        this.playerCountText = new Text();
        this.volcanoCardCountText = new Text();
    }

    /**
     * Draws the MainMenuUI elements on the screen, including the background, title, player count controls, and buttons.
     */
    @Override
    public void draw() {
        this.UIRoot.getChildren().clear();

        // ================
        // Menu Background
        this.menuBackground.setX(0);
        this.menuBackground.setY(0);
        this.menuBackground.setFitWidth(this.UIScene.getWidth());
        this.menuBackground.setFitHeight(this.UIScene.getHeight());
        this.menuBackground.setOpacity(0.85);

        this.UIRoot.getChildren().add(this.menuBackground);
        // ================

        // ================
        // Menu Bounds
        double menuHeight = this.UIScene.getHeight();
        double menuWidth = 0.3 * this.UIScene.getWidth();
        double menuX = 0.5 * (this.UIScene.getWidth() - menuWidth);
        double menuY = 0;

        Rectangle menuBound = new Rectangle(menuX, menuY, menuWidth, menuHeight);
        menuBound.setFill(Color.web("#eb9b54", 0.5));

        this.UIRoot.getChildren().add(menuBound);
        // ================

        // ================
        // Menu Title
        Text menuTitle = new Text("FIERY DRAGONS");
        menuTitle.setFont(new Font(0.2 * menuHeight));
        menuTitle.setFill(Color.AZURE);
        menuTitle.setStroke(Color.web("#eb9b54").brighter());
        menuTitle.setStrokeWidth(0.007 * menuHeight);

        double menuTitleX = 0.5 * (this.UIScene.getWidth() - menuTitle.getLayoutBounds().getWidth());
        double menuTitleY = 0.01 * menuHeight;
        menuTitle.relocate(menuTitleX, menuTitleY);

        this.UIRoot.getChildren().add(menuTitle);
        // ================

        // ================
        // Menu buttons constraints
        double buttonHeight = 0.15 * menuHeight;
        double minButtonWidth = 0.8 * menuWidth;
        double buttonSpacing = 1.2 * buttonHeight;
        // ================

        // ================
        // Player Count Button Panel
        double playerCountPanelHeight = 0.5 * buttonHeight;

        // Player Count Label
        Text playerCountLabel = new Text("PLAYERS");
        playerCountLabel.setFont(new Font(0.3 * playerCountPanelHeight));
        playerCountLabel.setFill(Color.WHITESMOKE);

        double playerCountLabelX = menuX + 0.5 * (menuWidth - playerCountLabel.getLayoutBounds().getWidth());
        double playerCountLabelY = 0.25 * menuHeight;
        playerCountLabel.relocate(playerCountLabelX, playerCountLabelY);

        this.UIRoot.getChildren().add(playerCountLabel);

        double playerCountPanelY = playerCountLabelY + 1.005 * playerCountLabel.getLayoutBounds().getHeight();

        // Player Count Display
        double playerCountDisplayWidth = 0.4 * minButtonWidth;
        double playerCountDisplayX = menuX + 0.5 * (menuWidth - playerCountDisplayWidth);
        Rectangle playerCountDisplay = new Rectangle(playerCountDisplayX, playerCountPanelY, playerCountDisplayWidth, playerCountPanelHeight);
        playerCountDisplay.setArcHeight(0.5 * playerCountPanelHeight);
        playerCountDisplay.setArcWidth(0.5 * playerCountPanelHeight);
        playerCountDisplay.setFill(Color.WHITESMOKE);
        playerCountDisplay.setStroke(Color.web("#eb9b54").darker());
        playerCountDisplay.setStrokeWidth(0.005 * buttonHeight);

        this.playerCountText.setText(Integer.toString(this.playerCount));
        this.playerCountText.setFont(new Font(0.4 * buttonHeight));
        this.playerCountText.setFill(Color.web("#eb9b54").darker());

        double playerCountTextX = playerCountDisplayX + 0.5 * (playerCountDisplayWidth - playerCountText.getLayoutBounds().getWidth());
        double playerCountTextY = playerCountPanelY + 0.5 * (playerCountPanelHeight - playerCountText.getLayoutBounds().getHeight());
        playerCountText.relocate(playerCountTextX, playerCountTextY);

        this.UIRoot.getChildren().addAll(playerCountDisplay, this.playerCountText);

        // Increment Player Count Button
        double incrementButtonX = playerCountDisplayX + 1.01 * playerCountDisplayWidth;
        Rectangle incrementButton = new Rectangle(incrementButtonX, playerCountPanelY, playerCountPanelHeight, playerCountPanelHeight);
        incrementButton.setArcHeight(0.5 * playerCountPanelHeight);
        incrementButton.setArcWidth(0.5 * playerCountPanelHeight);
        incrementButton.setFill(Color.web("#1ee5e8"));

        Text incrementText = new Text("+");
        incrementText.setFont(new Font(0.4 * playerCountPanelHeight));
        incrementText.setFill(Color.AZURE);

        double incrementTextX = incrementButtonX + 0.5 * (playerCountPanelHeight - incrementText.getLayoutBounds().getWidth());
        double incrementTextY = playerCountPanelY + 0.5 * (playerCountPanelHeight - incrementText.getLayoutBounds().getHeight());
        incrementText.relocate(incrementTextX, incrementTextY);

        incrementButton.setOnMouseClicked(e -> handlePlayerCountChange(1));
        incrementText.setOnMouseClicked(e -> handlePlayerCountChange(1));

        this.UIRoot.getChildren().addAll(incrementButton, incrementText);

        // Decrement Player Count Button
        double decrementButtonX = playerCountDisplayX - 0.01 * playerCountDisplayWidth - playerCountPanelHeight;
        Rectangle decrementButton = new Rectangle(decrementButtonX, playerCountPanelY, playerCountPanelHeight, playerCountPanelHeight);
        decrementButton.setArcHeight(0.5 * playerCountPanelHeight);
        decrementButton.setArcWidth(0.5 * playerCountPanelHeight);
        decrementButton.setFill(Color.web("#d6365b"));

        Text decrementText = new Text("-");
        decrementText.setFont(new Font(0.4 * playerCountPanelHeight));
        decrementText.setFill(Color.AZURE);

        double decrementTextX = decrementButtonX + 0.5 * (playerCountPanelHeight - decrementText.getLayoutBounds().getWidth());
        double decrementTextY = playerCountPanelY + 0.5 * (playerCountPanelHeight - decrementText.getLayoutBounds().getHeight());
        decrementText.relocate(decrementTextX, decrementTextY);

        decrementButton.setOnMouseClicked(e -> handlePlayerCountChange(-1));
        decrementText.setOnMouseClicked(e -> handlePlayerCountChange(-1));

        this.UIRoot.getChildren().addAll(decrementButton, decrementText);
        // ================

        // ================
        // Volcano Card Count Button Panel
        double volcanoCardCountPanelHeight = 0.5 * buttonHeight;

        // Volcano Card Count Label
        Text volcanoCardCountLabel = new Text("VOLCANO CARDS");
        volcanoCardCountLabel.setFont(new Font(0.3 * volcanoCardCountPanelHeight));
        volcanoCardCountLabel.setFill(Color.WHITESMOKE);

        double volcanoCardCountLabelX = menuX + 0.5 * (menuWidth - volcanoCardCountLabel.getLayoutBounds().getWidth());
        double volcanoCardCountLabelY = playerCountPanelY + 1.08 * playerCountPanelHeight;
        volcanoCardCountLabel.relocate(volcanoCardCountLabelX, volcanoCardCountLabelY);

        this.UIRoot.getChildren().add(volcanoCardCountLabel);

        double volcanoCardCountPanelY = volcanoCardCountLabelY + 1.005 * volcanoCardCountLabel.getLayoutBounds().getHeight();

        // Volcano Card Count Display
        double volcanoCardCountDisplayWidth = 0.4 * minButtonWidth;
        double volcanoCardCountDisplayX = menuX + 0.5 * (menuWidth - volcanoCardCountDisplayWidth);
        Rectangle volcanoCardCountDisplay = new Rectangle(volcanoCardCountDisplayX, volcanoCardCountPanelY, volcanoCardCountDisplayWidth, volcanoCardCountPanelHeight);
        volcanoCardCountDisplay.setArcHeight(0.5 * volcanoCardCountPanelHeight);
        volcanoCardCountDisplay.setArcWidth(0.5 * volcanoCardCountPanelHeight);
        volcanoCardCountDisplay.setFill(Color.WHITESMOKE);
        volcanoCardCountDisplay.setStroke(Color.web("#eb9b54").darker());
        volcanoCardCountDisplay.setStrokeWidth(0.005 * buttonHeight);

        this.volcanoCardCountText.setText(Integer.toString(this.volcanoCardCount));
        this.volcanoCardCountText.setFont(new Font(0.4 * buttonHeight));
        this.volcanoCardCountText.setFill(Color.web("#eb9b54").darker());

        double volcanoCardCountTextX = volcanoCardCountDisplayX + 0.5 * (volcanoCardCountDisplayWidth - volcanoCardCountText.getLayoutBounds().getWidth());
        double volcanoCardCountTextY = volcanoCardCountPanelY + 0.5 * (volcanoCardCountPanelHeight - volcanoCardCountText.getLayoutBounds().getHeight());
        volcanoCardCountText.relocate(volcanoCardCountTextX, volcanoCardCountTextY);

        this.UIRoot.getChildren().addAll(volcanoCardCountDisplay, this.volcanoCardCountText);

        // Increment volcanoCard Count Button
        double incrementVolcanoCardButtonX = volcanoCardCountDisplayX + 1.01 * volcanoCardCountDisplayWidth;
        Rectangle incrementVolcanoCardButton = new Rectangle(incrementVolcanoCardButtonX, volcanoCardCountPanelY, volcanoCardCountPanelHeight, volcanoCardCountPanelHeight);
        incrementVolcanoCardButton.setArcHeight(0.5 * volcanoCardCountPanelHeight);
        incrementVolcanoCardButton.setArcWidth(0.5 * volcanoCardCountPanelHeight);
        incrementVolcanoCardButton.setFill(Color.web("#1ee5e8"));

        Text incrementVolcanoCardText = new Text("+");
        incrementVolcanoCardText.setFont(new Font(0.4 * volcanoCardCountPanelHeight));
        incrementVolcanoCardText.setFill(Color.AZURE);

        double incrementVolcanoCardTextX = incrementVolcanoCardButtonX + 0.5 * (volcanoCardCountPanelHeight - incrementVolcanoCardText.getLayoutBounds().getWidth());
        double incrementVolcanoCardTextY = volcanoCardCountPanelY + 0.5 * (volcanoCardCountPanelHeight - incrementVolcanoCardText.getLayoutBounds().getHeight());
        incrementVolcanoCardText.relocate(incrementVolcanoCardTextX, incrementVolcanoCardTextY);

        incrementVolcanoCardButton.setOnMouseClicked(e -> handleVolcanoCardCountChange(1));
        incrementVolcanoCardText.setOnMouseClicked(e -> handleVolcanoCardCountChange(1));

        this.UIRoot.getChildren().addAll(incrementVolcanoCardButton, incrementVolcanoCardText);

        // Decrement volcanoCard Count Button
        double decrementVolcanoCardButtonX = volcanoCardCountDisplayX - 0.01 * volcanoCardCountDisplayWidth - volcanoCardCountPanelHeight;
        Rectangle decrementVolcanoCardButton = new Rectangle(decrementVolcanoCardButtonX, volcanoCardCountPanelY, volcanoCardCountPanelHeight, volcanoCardCountPanelHeight);
        decrementVolcanoCardButton.setArcHeight(0.5 * volcanoCardCountPanelHeight);
        decrementVolcanoCardButton.setArcWidth(0.5 * volcanoCardCountPanelHeight);
        decrementVolcanoCardButton.setFill(Color.web("#d6365b"));

        Text decrementVolcanoCardText = new Text("-");
        decrementVolcanoCardText.setFont(new Font(0.4 * volcanoCardCountPanelHeight));
        decrementVolcanoCardText.setFill(Color.AZURE);

        double decrementVolcanoCardTextX = decrementVolcanoCardButtonX + 0.5 * (volcanoCardCountPanelHeight - decrementVolcanoCardText.getLayoutBounds().getWidth());
        double decrementVolcanoCardTextY = volcanoCardCountPanelY + 0.5 * (volcanoCardCountPanelHeight - decrementVolcanoCardText.getLayoutBounds().getHeight());
        decrementVolcanoCardText.relocate(decrementVolcanoCardTextX, decrementVolcanoCardTextY);

        decrementVolcanoCardButton.setOnMouseClicked(e -> handleVolcanoCardCountChange(-1));
        decrementVolcanoCardText.setOnMouseClicked(e -> handleVolcanoCardCountChange(-1));

        this.UIRoot.getChildren().addAll(decrementVolcanoCardButton, decrementVolcanoCardText);
        // ================

        // ================
        // New Game Button
        double newGameButtonY = volcanoCardCountPanelY + 1.08 * volcanoCardCountPanelHeight;

        Text newGameButtonText = new Text("NEW GAME");
        newGameButtonText.setFont(new Font(0.4 * buttonHeight));
        newGameButtonText.setFill(Color.AZURE);

        double newGameButtonTextX = menuX + 0.5 * (menuWidth - newGameButtonText.getLayoutBounds().getWidth());
        double newGameButtonTextY = newGameButtonY + 0.5 * (buttonHeight - newGameButtonText.getLayoutBounds().getHeight());
        newGameButtonText.relocate(newGameButtonTextX, newGameButtonTextY);

        double newGameButtonWidth = Math.max(minButtonWidth, newGameButtonText.getLayoutBounds().getWidth() * 1.2);
        double newGameButtonX = 0.5 * (this.UIScene.getWidth() - newGameButtonWidth);

        Rectangle newGameButton = new Rectangle(newGameButtonX, newGameButtonY, newGameButtonWidth, buttonHeight);
        newGameButton.setArcWidth(buttonHeight);
        newGameButton.setArcHeight(buttonHeight);
        newGameButton.setFill(Color.web("#7ce66a"));

        newGameButton.setOnMouseClicked(e -> this.handleNewGame());
        newGameButtonText.setOnMouseClicked(e -> this.handleNewGame());

        this.UIRoot.getChildren().addAll(newGameButton, newGameButtonText);
        // ================

        // ================
        // Load Game Button
        double loadGameButtonY = newGameButtonY + buttonSpacing;

        Text loadGameButtonText = new Text("LOAD GAME");
        loadGameButtonText.setFont(new Font(0.4 * buttonHeight));
        loadGameButtonText.setFill(Color.AZURE);

        double loadGameButtonTextX = menuX + 0.5 * (menuWidth - loadGameButtonText.getLayoutBounds().getWidth());
        double loadGameButtonTextY = loadGameButtonY + 0.5 * (buttonHeight - loadGameButtonText.getLayoutBounds().getHeight());
        loadGameButtonText.relocate(loadGameButtonTextX, loadGameButtonTextY);

        double loadGameButtonWidth = Math.max(minButtonWidth, loadGameButtonText.getLayoutBounds().getWidth() * 1.2);
        double loadGameButtonX = 0.5 * (this.UIScene.getWidth() - loadGameButtonWidth);

        Rectangle loadGameButton = new Rectangle(loadGameButtonX, loadGameButtonY, loadGameButtonWidth, buttonHeight);
        loadGameButton.setArcWidth(buttonHeight);
        loadGameButton.setArcHeight(buttonHeight);
        loadGameButton.setFill(Color.web("#7ce66a"));

        loadGameButton.setOnMouseClicked(e -> this.handleLoadGame());
        loadGameButtonText.setOnMouseClicked(e -> this.handleLoadGame());

        this.UIRoot.getChildren().addAll(loadGameButton, loadGameButtonText);
        // ================

        // ================
        // Exit Button
        double exitButtonY = loadGameButtonY + buttonSpacing;

        Text exitButtonText = new Text("EXIT");
        exitButtonText.setFont(new Font(0.4 * buttonHeight));
        exitButtonText.setFill(Color.WHITESMOKE);

        double exitButtonTextX = menuX + 0.5 * (menuWidth - exitButtonText.getLayoutBounds().getWidth());
        double exitButtonTextY = exitButtonY + 0.5 * (buttonHeight - exitButtonText.getLayoutBounds().getHeight());
        exitButtonText.relocate(exitButtonTextX, exitButtonTextY);

        double exitButtonWidth = Math.max(minButtonWidth, exitButtonText.getLayoutBounds().getWidth() * 1.2);
        double exitButtonX = 0.5 * (this.UIScene.getWidth() - exitButtonWidth);

        Rectangle exitButton = new Rectangle(exitButtonX, exitButtonY, exitButtonWidth, buttonHeight);
        exitButton.setArcWidth(buttonHeight);
        exitButton.setArcHeight(buttonHeight);
        exitButton.setFill(Color.web("#fa564a"));

        exitButton.setOnMouseClicked(e -> this.handleExit());
        exitButtonText.setOnMouseClicked(e -> this.handleExit());

        this.UIRoot.getChildren().addAll(exitButton, exitButtonText);
        // ================
    }


    /**
     * Handles the action to start a new game.
     */
    private void handleNewGame() {
        this.gameManager.startGame(new BasicGameMode(this, this.playerCount, this.volcanoCardCount));
    }

    /**
     * Handles the action to load a game.
     */
    private void handleLoadGame() {
        Map<String, String> properties = null;
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Load Game");

        try {
            File savesDirectory = new File(SaveHandler.INSTANCE.getSaveDirectory());

            fileChooser.setInitialDirectory(savesDirectory);

            File selectedFile = fileChooser.showOpenDialog(this.UIScene.getWindow());

            if (selectedFile != null) {
                properties = SaveHandler.INSTANCE.load(selectedFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if(properties == null) {
            return;
        }

        this.gameManager.startGame(new BasicGameMode(this, properties));
    }

    /**
     * Handles the action to exit the application.
     */
    private void handleExit() {
        System.exit(0);
    }

    /**
     * Handles the action to change the player count.
     *
     * @param valueChange the value to change the player count by
     */
    private void handlePlayerCountChange(int valueChange) {
        this.playerCount += valueChange;

        if (this.playerCount < this.minPlayerCount) {
            this.playerCount = this.minPlayerCount;
        } else if (this.playerCount > this.maxPlayerCount) {
            this.playerCount = this.maxPlayerCount;
        }

        this.draw();
    }

    /**
     * Handles the action to change the volcanoCard count.
     *
     * @param valueChange the value to change the volcanoCard count by
     */
    private void handleVolcanoCardCountChange(int valueChange) {
        this.volcanoCardCount += valueChange;

        if (this.volcanoCardCount < this.minVolcanoCardCount) {
            this.volcanoCardCount = this.minVolcanoCardCount;
        } else if (this.volcanoCardCount > this.maxVolcanoCardCount) {
            this.volcanoCardCount = this.maxVolcanoCardCount;
        }

        this.draw();
    }
}
