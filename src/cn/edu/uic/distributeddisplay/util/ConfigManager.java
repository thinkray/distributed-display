/**
 * The ConfigManager handles the configuration of the program
 *
 * @author Bohui WU
 * @version 1.0
 * @since 12/25/2019
 */
package cn.edu.uic.distributeddisplay.util;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class ConfigManager {

    private static JSONObject config;
    private static File configFile;

    public static void initConfigManager() {
        configFile = new File("config.json");
        try {
            String jsonString = FileUtils.readFileToString(configFile, "UTF-8");
            config = new JSONObject(jsonString);
        } catch (IOException e) {
            // Initialize the array
            config = new JSONObject();

            // Default profile location
            config.put("profile_location", "profile.bin");

            // Get default language and store the default language
            String lang = Locale.getDefault().getLanguage();
            switch (lang) {
                case "zh":
                    // When the language is Chinese
                    if (Locale.getDefault().getCountry().equals("CN")) {
                        // If the region is mainland China, use simplified Chinese
                        lang = "zh-Hans";
                    } else {
                        // If the region is outside mainland China but still uses Chinese, use tradition Chinese
                        lang = "zh-Hant";
                    }
                    break;
                case "en":
                default:
                    lang = "en-US";
            }
            config.put("lang", lang);
            saveConfig();
        }
    }

    public static void saveConfig() {
        try {
            FileUtils.writeStringToFile(configFile, config.toString(), "UTF-8");
            Log.log("Configuration updated.");
        } catch (Exception e) {
            Log.logError(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static String getConfigEntry(String key) {
        try {
            return (String) config.get(key);
        } catch (Exception e) {
            Log.logError(e.getMessage());
        }
        return null;
    }

    public static void setConfigEntry(String key, String value) {
        try {
            config.put(key, value);
            saveConfig();
        } catch (Exception e) {
            Log.logError(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
