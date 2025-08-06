package com.fierydragon.core.interfaces;

import javafx.scene.Scene;

/**
 * The ISceneHolder interface provides a common interface for managing the primary stage of the application, including its size, resolution scaling, and active scene.
 * 
 * @author Chang Yi Zhong
 */
public interface ISceneHolder {
    public Scene getScene();

    public String getSceneName();
}
