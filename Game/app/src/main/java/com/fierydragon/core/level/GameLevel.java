package com.fierydragon.core.level;

import com.fierydragon.core.interfaces.ISaveable;
import com.fierydragon.core.interfaces.ISceneHolder;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

/**
 * The GameLevel class is an abstract base class representing a level in the game. 
 * It provides common functionality for scene management and initialization.
 * 
 * @author Chang Yi Zhong
 * @author Lim Hung Xuan
 */
public abstract class GameLevel implements ISceneHolder, ISaveable {
    protected String sceneName;

    protected Scene scene;
    protected Group levelRoot;

    /**
     * Constructs a GameLevel object, setting up the scene and level root.
     * Initializes the scene with a light gray background and adds listeners to handle resizing.
     */
    public GameLevel() {
        this.levelRoot = new Group();
        this.scene = new Scene(this.levelRoot);
        this.scene.setFill(Color.LIGHTGRAY);

        this.scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            this.init();
        });

        this.scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            this.init();
        });
    }

    /**
     * Initializes the game level. This method must be implemented by subclasses.
     */
    public abstract void init();

    /**
     * Advances to the next turn in the game. This method must be implemented by subclasses.
     */
    public abstract void nextTurn();

    /**
     * Gets the scene associated with the game level.
     * 
     * @return the scene of the game level
     */
    @Override
    public Scene getScene() {
        return this.scene;
    }

    /**
     * Gets the name of the scene. By default, it returns "BaseGameLevel".
     * 
     * @return the name of the scene
     */
    @Override
    public String getSceneName() {
        return "BaseGameLevel";
    }
}
