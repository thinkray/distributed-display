/**
 * The LangManager manages getting the correct text to display
 *
 * @author Team 3
 * @version 1.0
 */
package cn.edu.uic.distributeddisplay.core.util;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class LangManger {

    private static String lang;
    private static JSONObject langTable;

    public static void initLangManager() {
        lang = ConfigManager.getConfigEntry("lang");
        try {
            byte[] langResource = LangManger.class.getClassLoader().getResourceAsStream("lang/" + lang + ".json").readAllBytes();
            String jsonString = new String(langResource, StandardCharsets.UTF_8);
            langTable = new JSONObject(jsonString);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            String errMsg = "Unable to read language pack." +
                    ".\nPlease consider reinstalling the program!";
            JOptionPane.showMessageDialog(null, errMsg, "Error",
                    JOptionPane.ERROR_MESSAGE);
            Logger.logError(e.getMessage() + " | " + errMsg);
            System.exit(1);
        } catch (JSONException e) {
            String errMsg = "Error occurred while creating the language table.";
            JOptionPane.showMessageDialog(null, errMsg);
            Logger.logError(e.getMessage() + " | " + errMsg);
            System.exit(1);
        }
    }

    public static String get(String key) {
        try {
            return (String) langTable.get(key);
        } catch (NullPointerException | JSONException e) {
            Logger.logError(e.getMessage() + " | Unable to retrieve value from the key [" + key + "]");
            return null;
        }
    }

}
