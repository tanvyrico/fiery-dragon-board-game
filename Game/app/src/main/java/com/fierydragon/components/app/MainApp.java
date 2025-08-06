package com.fierydragon.components.app;

import com.fierydragon.components.UI.MainMenuUI;
import com.fierydragon.core.framework.GameManager;
import com.fierydragon.core.framework.StageController;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point for the application
 * 
 * @author Chang Yi Zhong
 */
public class MainApp extends Application {
    protected GameManager gameManager;
    protected MainMenuUI mainMenu;

    /**
     * Application initialization method
     * 
     * Runs when the application starts
     */
    @Override
    public void init() {
        this.gameManager = new GameManager();
        this.mainMenu = new MainMenuUI(this.gameManager);
    }

    /**
     * Starts the application
     * 
     * @param primaryStage Provided by JavaFX main application thread
     */
    @Override
    public void start(Stage primaryStage) {
        StageController.INSTANCE.setStage(primaryStage);
        StageController.INSTANCE.show();

        StageController.INSTANCE.drawSceneHolder(this.mainMenu);
    }

    /**
     * Application main run method
     */
    public static void main(String[] args) {
        launch(args);
    }
}
