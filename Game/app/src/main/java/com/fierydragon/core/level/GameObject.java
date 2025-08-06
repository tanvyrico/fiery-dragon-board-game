package com.fierydragon.core.level;

import com.fierydragon.core.interfaces.ISaveable;

import javafx.scene.Group;
import javafx.scene.shape.Circle;

/**
 * The GameObject class is an abstract base class representing an object in the game.
 * 
 * @author Chang Yi Zhong
 */
public abstract class GameObject implements ISaveable {
    protected GameLevel gameLevel;
    protected Group objectRoot;

    public GameObject(GameLevel gameLevel) {
        this.gameLevel = gameLevel;
        this.objectRoot = new Group();
    }

    public abstract void setBounds(Circle objectBound);

    public Group getRoot() {
        return this.objectRoot;
    }
}
