/**
 * The LangManager manages getting the correct text to display
 *
 * @author Bohui WU
 * @since 12/28/2019
 * @version 1.0
 */
package cn.edu.uic.distributeddisplay.util;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class LangManger {

    private static String lang;
    private static JSONObject langTable;

    public static void initLangManager() {
        lang = ConfigManager.getConfigEntry("lang");
        File langFile = new File("." + File.separator + "lang" + File.separator + lang + ".json");
        try {
            String jsonString = FileUtils.readFileToString(langFile, "UTF-8");
            langTable = new JSONObject(jsonString);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            String errMsg = "Unable to read language pack at " +
                    langFile.getAbsolutePath() + ".\nPlease consider reinstalling the program!";
            JOptionPane.showMessageDialog(null, errMsg, "Error",
                    JOptionPane.ERROR_MESSAGE);
            Log.logError(e.getMessage() + " | " + errMsg);
            System.exit(1);
        } catch (JSONException e) {
            String errMsg = "Error occurred while creating the language " +
                    "table.\nPlease verify the language file at " + langFile.getAbsolutePath();
            JOptionPane.showMessageDialog(null, errMsg);
            Log.logError(e.getMessage() + " | " + errMsg);
            System.exit(1);
        }
    }

    public static String get(String key) {
        try {
            return (String) langTable.get(key);
        }catch(NullPointerException | JSONException e) {
            Log.logError(e.getMessage() + " | Unable to retrieve value from the key [" + key + "]");
            return null;
        }
    }

}
