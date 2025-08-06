package com.fierydragon.components;

import com.fierydragon.core.interfaces.ISceneHolder;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

/**
 * Base UI abstract class for all UI 
 * 
 * @author Chang Yi Zhong
 */
public abstract class BaseUI implements ISceneHolder {
    protected String UIName;

    protected Scene UIScene;
    protected Group UIRoot;

    /**
     * Constructor
     */
    public BaseUI() {
        this.UIName = "BaseUI";

        this.UIRoot = new Group();
        this.UIScene = new Scene(this.UIRoot);
        this.UIScene.setFill(Color.LIGHTGRAY);

        this.UIScene.widthProperty().addListener((observable, oldValue, newValue) -> {
            this.draw();
        });

        this.UIScene.heightProperty().addListener((observable, oldValue, newValue) -> {
            this.draw();
        });
    }

    /**
     * Draw UI abstract method
     */
    public abstract void draw();

    public Group getRoot() {
        return this.UIRoot;
    }

    /**
     * Get scene
     * 
     * @return Scene
     */
    @Override
    public Scene getScene() {
        return this.UIScene;
    }

    /**
     * Get scene name
     * 
     * @return Scene name
     */
    @Override
    public String getSceneName() {
        return this.UIName;
    }
}
