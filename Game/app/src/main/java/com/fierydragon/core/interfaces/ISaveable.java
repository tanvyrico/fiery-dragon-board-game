package com.fierydragon.core.interfaces;

import java.util.Map;
/**
 * Interface for objects that can be saved by SaveHandler.
 *
 * @author : Yi Zhong
 */

public interface ISaveable {

    /**
     * Populates the given properties map with the state of the object.
     *
     * @param properties the map to populate with the object's state
     * @param prefix a prefix to prepend to property keys (used for nested objects)
     */
    public void invokeSave(Map<String, String> properties, String prefix);
}
