package com.fierydragon.components.UI;

import com.fierydragon.components.BaseUI;
import com.fierydragon.core.framework.ResourceLoader;
import com.fierydragon.core.framework.SaveHandler;
import com.fierydragon.core.framework.StageController;
import com.fierydragon.core.interfaces.ISaveable;
import com.fierydragon.core.interfaces.ISceneHolder;

import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;


/**
 * The PauseMenuUI class represents the user interface for the pause menu in the application.
 * It extends the {@code BaseUI} class and provides methods for drawing the pause menu and handling
 * user interactions such as resuming the game, saving the game, and returning to the main menu.
 *
 * @author : All group member
 */
public class PauseMenuUI extends BaseUI {
    private final MainMenuUI mainMenuUI;
    private ImageView menuBackground;

    private ISceneHolder previousScene;
    private ISaveable saveTarget;


    /**
     * Constructs a new PauseMenuUI with the specified main menu UI.
     *
     * @param mainMenuUI the main menu UI associated with this pause menu
     */
    public PauseMenuUI(MainMenuUI mainMenuUI) {
        this.UIName = "PauseMenu";

        this.mainMenuUI = mainMenuUI;

        String menuBackgroundPath = ResourceLoader.ASSETS.getPath() + "background.png";
        this.menuBackground = new ImageView(new Image(menuBackgroundPath));
    }


    /**
     * Draws the pause menu UI, including the background, menu bounds, title, and buttons
     * for resuming the game, saving the game, and returning to the main menu.
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
        Text menuTitle = new Text("GAME PAUSED");
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
        double buttonSpacing = 1.25 * buttonHeight;
        // ================

        // ================
        // Resume button
        double resumeButtonY = 0.35 * menuHeight;

        Text resumeButtonText = new Text("RESUME");
        resumeButtonText.setFont(new Font(0.4 * buttonHeight));
        resumeButtonText.setFill(Color.AZURE);

        double resumeButtonTextX = menuX + 0.5 * (menuWidth - resumeButtonText.getLayoutBounds().getWidth());
        double resumeButtonTextY = resumeButtonY + 0.5 * (buttonHeight - resumeButtonText.getLayoutBounds().getHeight());
        resumeButtonText.relocate(resumeButtonTextX, resumeButtonTextY);

        double resumeButtonWidth = Math.max(minButtonWidth, resumeButtonText.getLayoutBounds().getWidth() * 1.2);
        double resumeButtonX = 0.5 * (this.UIScene.getWidth() - resumeButtonWidth);

        Rectangle resumeButton = new Rectangle(resumeButtonX, resumeButtonY, resumeButtonWidth, buttonHeight);
        resumeButton.setArcWidth(buttonHeight);
        resumeButton.setArcHeight(buttonHeight);
        resumeButton.setFill(Color.web("#7ce66a"));

        resumeButton.setOnMouseClicked(e -> this.handleResume());
        resumeButtonText.setOnMouseClicked(e -> this.handleResume());

        this.UIRoot.getChildren().addAll(resumeButton, resumeButtonText);
        // ================

        // ================
        // Save button
        double saveButtonY = resumeButtonY + buttonSpacing;

        Text saveButtonText = new Text("SAVE GAME");
        saveButtonText.setFont(new Font(0.4 * buttonHeight));
        saveButtonText.setFill(Color.AZURE);

        double saveButtonTextX = menuX + 0.5 * (menuWidth - saveButtonText.getLayoutBounds().getWidth());
        double saveButtonTextY = saveButtonY + 0.5 * (buttonHeight - saveButtonText.getLayoutBounds().getHeight());
        saveButtonText.relocate(saveButtonTextX, saveButtonTextY);

        double saveButtonWidth = Math.max(minButtonWidth, saveButtonText.getLayoutBounds().getWidth() * 1.2);
        double saveButtonX = 0.5 * (this.UIScene.getWidth() - saveButtonWidth);

        Rectangle saveButton = new Rectangle(saveButtonX, saveButtonY, saveButtonWidth, buttonHeight);
        saveButton.setArcWidth(buttonHeight);
        saveButton.setArcHeight(buttonHeight);
        saveButton.setFill(Color.web("#7ce66a"));

        saveButton.setOnMouseClicked(e -> this.handleSave());
        saveButtonText.setOnMouseClicked(e -> this.handleSave());

        this.UIRoot.getChildren().addAll(saveButton, saveButtonText);
        // ================

        // ================
        // Main Menu button
        double mainMenuButtonY = saveButtonY + buttonSpacing;

        Text mainMenuButtonText = new Text("MAIN MENU");
        mainMenuButtonText.setFont(new Font(0.4 * buttonHeight));
        mainMenuButtonText.setFill(Color.AZURE);

        double mainMenuButtonTextX = menuX + 0.5 * (menuWidth - mainMenuButtonText.getLayoutBounds().getWidth());
        double mainMenuButtonTextY = mainMenuButtonY + 0.5 * (buttonHeight - mainMenuButtonText.getLayoutBounds().getHeight());
        mainMenuButtonText.relocate(mainMenuButtonTextX, mainMenuButtonTextY);

        double mainMenuButtonWidth = Math.max(minButtonWidth, mainMenuButtonText.getLayoutBounds().getWidth() * 1.2);
        double mainMenuButtonX = 0.5 * (this.UIScene.getWidth() - mainMenuButtonWidth);

        Rectangle mainMenuButton = new Rectangle(mainMenuButtonX, mainMenuButtonY, mainMenuButtonWidth, buttonHeight);
        mainMenuButton.setArcWidth(buttonHeight);
        mainMenuButton.setArcHeight(buttonHeight);
        mainMenuButton.setFill(Color.web("#fa564a"));

        mainMenuButton.setOnMouseClicked(e -> this.handleMainMenu());
        mainMenuButtonText.setOnMouseClicked(e -> this.handleMainMenu());

        this.UIRoot.getChildren().addAll(mainMenuButton, mainMenuButtonText);
        // ================
    }


    /**
     * Sets the previous scene holder to return to when the resume button is clicked.
     *
     * @param previousScene the previous scene holder
     */
    public void setPreviousSceneHolder(ISceneHolder previousScene) {
        this.previousScene = previousScene;
    }

    /**
     * Sets the target object to be saved when the save button is clicked.
     *
     * @param saveTarget the saveable target object
     */
    public void setSaveTarget(ISaveable saveTarget) {
        this.saveTarget = saveTarget;
    }


    /**
     * Handles the action for resuming the game by switching to the previous scene.
     */
    private void handleResume() {
        if(this.previousScene != null) {
            StageController.INSTANCE.drawSceneHolder(this.previousScene);
        }
    }


    /**
     * Handles the action for saving the game by invoking the save handler on the target object
     * and displaying a success message.
     */
    private void handleSave() {
        if(this.saveTarget != null) {
            SaveHandler.INSTANCE.save(this.saveTarget);

            Text saveSuccessText = new Text("SAVE SUCCESS");
            saveSuccessText.setFont(new Font(0.05 * this.UIScene.getHeight()));
            saveSuccessText.setFill(Color.AZURE);

            //display at bottom left
            double saveSuccessTextX = 0.01 * this.UIScene.getWidth();
            double saveSuccessTextY = 0.99 * this.UIScene.getHeight() - saveSuccessText.getLayoutBounds().getHeight();
            saveSuccessText.relocate(saveSuccessTextX, saveSuccessTextY);

            this.UIRoot.getChildren().add(saveSuccessText);

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> {
                this.UIRoot.getChildren().remove(saveSuccessText);
            });
            pause.play();
        }
    }


    /**
     * Handles the action for returning to the main menu by switching to the main menu UI.
     */
    private void handleMainMenu() {
        StageController.INSTANCE.drawSceneHolder(this.mainMenuUI);
    }
}
