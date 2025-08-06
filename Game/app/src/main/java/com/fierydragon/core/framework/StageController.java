package com.fierydragon.core.framework;

import com.fierydragon.core.interfaces.ISceneHolder;

import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * The StageController class manages the primary stage of the application, including its size, resolution scaling, and active scene.
 * 
 * @author Chang Yi Zhong
 * @author Lim Hung Xuan
 */
public class StageController {
    public static final StageController INSTANCE = new StageController();

    private Stage stage;

    private double stageWidth;
    private double stageHeight;
    private double outputScaleX;
    private double outputScaleY;
    private double scaledWidth;
    private double scaledHeight;

    private String activeSceneName = "";

    /**
     * Private constructor to enforce singleton pattern.
     */
    private StageController() {
        this.outputScaleX = Screen.getPrimary().getOutputScaleX();
        this.outputScaleY = Screen.getPrimary().getOutputScaleY();
    }

    /**
     * Sets the primary stage with default width and height.
     * 
     * @param stage the primary stage
     */
    public void setStage(Stage stage) {
        this.setStage(stage, 1280, 720);
    }

    /**
     * Sets the primary stage with specified height and width.
     * 
     * @param stage the primary stage
     * @param width the width of the stage
     * @param height the height of the stage
     */
    public void setStage(Stage stage, double width, double height) {
        this.stage = stage;
        this.stageWidth = width;
        this.stageHeight = height;
        this.scaledWidth = Math.floor(this.stageWidth / this.outputScaleX);
        this.scaledHeight = Math.floor(this.stageHeight / this.outputScaleY);

        this.stage.setWidth(this.scaledWidth);
        this.stage.setHeight(this.scaledHeight);
        this.stage.setResizable(true);

        // this.stage.setMinWidth(1280 / this.outputScaleX);
        // this.stage.setMinHeight(720 / this.outputScaleY);
    }

    /**
     * Draws the scene from the provided scene holder on the primary stage.
     * 
     * @param sceneHolder the scene holder containing the scene to be drawn
     */
    public void drawSceneHolder(ISceneHolder sceneHolder) {
        if (this.stage == null) {
            return;
        }
        this.stage.setScene(sceneHolder.getScene());
        this.activeSceneName = sceneHolder.getSceneName();
        this.show();
    }

    /**
     * Shows the primary stage.
     */
    public void show() {
        if (this.stage == null) {
            return;
        }
        this.stage.show();
    }

    /**
     * Gets the width of the primary stage.
     * 
     * @return the width of the stage
     */
    public double getStageWidth() {
        return this.scaledWidth;
    }

    /**
     * Gets the height of the primary stage.
     * 
     * @return the height of the stage
     */
    public double getStageHeight() {
        return this.scaledHeight;
    }

    /**
     * Gets the name of the currently active scene.
     * 
     * @return the name of the active scene
     */
    public String getActiveSceneName() {
        return this.activeSceneName;
    }
}
