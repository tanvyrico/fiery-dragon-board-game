package com.fierydragon.core.framework;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fierydragon.core.interfaces.ISaveable;


/**
 * SaveHandler is a singleton class responsible for saving and loading
 * game state data to and from disk. It uses a predefined directory
 * under the user's home directory to store save files.
 *
 * @author: Yi Zhong
 */
public class SaveHandler {

    /**
     * The singleton instance of the SaveHandler.
     */
    public static final SaveHandler INSTANCE = new SaveHandler();;

    private static final String SAVEDIR = System.getProperty("user.home") + "\\.fierydragon\\saves\\";
    private static final String SAVEFILE_EXTENSION = ".dat";

    private SimpleDateFormat dateFormat;

    static {
        if(!new File(SAVEDIR).exists()) {
            new File(SAVEDIR).mkdirs();
        }
    }

    private SaveHandler() {
        this.dateFormat = new SimpleDateFormat("ddMMyyyy-HHmmss");
    }


    /**
     * Generates a unique save file name based on the current date and time.
     *
     * @return the generated save file name
     */
    private String getSaveName() {
        Date now = new Date();

        return "fierydragonsave_" + this.dateFormat.format(now) + SaveHandler.SAVEFILE_EXTENSION;
    }

    /**
     * Saves the state of the given saveable object to disk.
     *
     * @param saveable the object to save
     */
    public void save(ISaveable saveable) {
        String saveName = this.getSaveName();
        String savePath = SaveHandler.SAVEDIR + saveName;
        Map<String, String> properties = new LinkedHashMap<String, String>();

        saveable.invokeSave(properties, "");

        try(FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {
            for(Map.Entry<String, String> entry : properties.entrySet()) {
                fileOutputStream.write((entry.getKey() + "=" + entry.getValue() + "\n").getBytes());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the state from the specified save file.
     *
     * @param savePath the path to the save file
     * @return a map of the loaded properties, or null if an error occurs
     */
    public Map<String, String> load(String savePath) {
        Map<String, String> properties = new LinkedHashMap<String, String>();

        try(BufferedReader reader = new BufferedReader(new FileReader(savePath))) {
            String line;

            while((line = reader.readLine()) != null) {
                String[] split = line.split("=");

                if(split.length == 2) {
                    properties.put(split[0].trim(), split[1].trim());
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }

        return properties;
    }
    /**
     * Returns the directory where save files are stored.
     *
     * @return the save directory path
     */
    public String getSaveDirectory() {
        return SaveHandler.SAVEDIR;
    }
}
