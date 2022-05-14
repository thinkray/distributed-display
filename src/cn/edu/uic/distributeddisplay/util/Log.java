/**
 * The Logger for the program
 *
 * @author Team 3
 * @version 1.0
 * @since 1/2/2020
 */
package cn.edu.uic.distributeddisplay.util;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {

    private static FileHandler fh;
    private static Logger logger;

    public static void initLogger(String fileName) {
        try {
            File logFile = new File(fileName);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            fh = new FileHandler(fileName, true);
            logger = Logger.getLogger("Runtime Error");
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
        } catch (Exception e) {
            System.out.println("[ERROR] Unable to create a logger. Errors may not be logged!");
        }
    }

    public static void logError(String msg) {
        try {
            logger.setLevel(Level.SEVERE);
            logger.severe(msg);
        } catch (Exception e) {
            System.out.println("[ERROR] Unable to log error message. Message:" + msg);
        }
    }

    public static void log(String msg) {
        try {
            logger.setLevel(Level.FINE);
            logger.fine(msg);
        } catch (Exception e) {
            System.out.println("[ERROR] Unable to log error message. Message:" + msg);
        }
    }

    public static void logWarning(String msg) {
        try {
            logger.setLevel(Level.WARNING);
            logger.warning(msg);
        } catch (Exception e) {
            System.out.println("[ERROR] Unable to log error message. Message:" + msg);
        }
    }

}
