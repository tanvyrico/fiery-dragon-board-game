package com.fierydragon.core.framework;

import javafx.scene.image.Image;

/**
 * Resource loader functional enum for getting runtime resource path
 * 
 * @author Chang Yi Zhong
 */
public enum ResourceLoader {
    ASSETS("assets/");

    private final String resourceFolder;

    /**
     * Constructor 
     * 
     * @param resourceFolder Relative path to folder in classpath
     */
    private ResourceLoader(String resourceFolder) {
        this.resourceFolder = this.getClass().getClassLoader().getResource(resourceFolder).toExternalForm();
    }

    /**
     * Get resource path
     * 
     * @return Resource path
     */
    public String getPath() {
        return this.resourceFolder;
    }

    /**
     * Load image from relative path
     * 
     * @param resourcePath Resource path
     * @return Image
     */
    public Image loadImage(String resourcePath) {
        return new Image(this.resourceFolder + resourcePath);
    }
}
