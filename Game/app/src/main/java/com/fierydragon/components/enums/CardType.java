package com.fierydragon.components.enums;

import com.fierydragon.core.interfaces.IResourceEnum;

import javafx.scene.paint.Color;

/**
 * Card type
 * 
 * Card types and resource paths according to IResourceEnum
 * 
 * @author Chang Yi Zhong
 */
public enum CardType implements IResourceEnum {
    SWAP("swap/",null),
    PIRATE_DRAGON("pirate-dragon/", null),
    BABYDRAGON("baby-dragon/", Color.LIGHTGREEN),
    SALAMANDER("salamander/", Color.LIGHTGRAY),
    BAT("bat/", Color.AQUA),
    SPIDER("spider/", Color.ORANGE);

    private final String resourcePath;
    private final Color color;

    /**
     * Constructor
     * 
     * @param resourcePath Relative resource path to the assets folder
     * @param color Card color
     */
    private CardType(String resourcePath, Color color) {
        this.resourcePath = resourcePath;
        this.color = color;
    }

    /**
     * Get resource path
     * 
     * @return Resource path
     */
    @Override
    public String getResourcePath() {
        return this.resourcePath;
    }

    /**
     * Get card color
     * 
     * @return Card color
     */
    public Color getColor() {
        return this.color;
    }
}
