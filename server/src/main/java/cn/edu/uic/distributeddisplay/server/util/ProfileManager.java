/**
 * A class that registers the current profile
 *
 * @author Team 3
 */
package cn.edu.uic.distributeddisplay.server.util;

import cn.edu.uic.distributeddisplay.core.profile.ServerSideProfile;
import cn.edu.uic.distributeddisplay.core.util.*;
import cn.edu.uic.distributeddisplay.server.controller.RMIServerController;
import org.apache.commons.text.StringEscapeUtils;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileManager {

    private static final File profileDirectory = new File(ConfigManager.getConfigEntry("profile_location"));
    private static ConcurrentHashMap<String, ProfileRow> profileMap = new ConcurrentHashMap<>();
    private static Thread onlineChecker;

    public static boolean saveProfileListToFile() {
        try {
            // Write the profile
            FileOutputStream fos = null;
            fos = new FileOutputStream(profileDirectory);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            ConcurrentHashMap<String, ServerSideProfile> profileMapForFile = new ConcurrentHashMap<>();
            for (Map.Entry<String, ProfileRow> row : profileMap.entrySet()) {
                profileMapForFile.put(row.getKey(), row.getValue().serverSideProfile);
            }

            out.writeObject(profileMapForFile);
            out.close();
            fos.close();
            Logger.log("Profile saved.");
            return true;
        } catch (Exception e) {
            Logger.logError(e.getMessage());
            return false;
        }
    }

    public static void loadProfileListFromFile() {
        try {
            // Read the profile
            FileInputStream fis = new FileInputStream(profileDirectory);
            ObjectInputStream in = new ObjectInputStream(fis);

            // Register the profile to the profile manager
            ConcurrentHashMap<String, ServerSideProfile> profileMapForFile = (ConcurrentHashMap<String,
                    ServerSideProfile>) in.readObject();
            for (Map.Entry<String, ServerSideProfile> row : profileMapForFile.entrySet()) {
                ProfileRow currentRow = new ProfileRow(row.getValue(), false, new Date(0L), "");
                profileMap.put(row.getKey(), currentRow);
            }

            in.close();
            fis.close();
            Logger.log("Profile loaded.");
        } catch (Exception e) {
            profileMap = new ConcurrentHashMap<>();
        }
    }

    public static void startOnlineChecker(RMIServerController rmiServerController) {
        onlineChecker = new Thread(() -> {
            try {
                while (true) {
                    Date currentDate = new Date();
                    for (Map.Entry<String, ProfileRow> row : profileMap.entrySet()) {
                        ProfileRow currentProfileRow = row.getValue();
                        if (currentProfileRow.isOnline) {
                            currentProfileRow.isOnline = Duration.between(row.getValue().lastSeen.toInstant(),
                                    currentDate.toInstant()).toMillis() < 5000;
                            if (!currentProfileRow.isOnline) {
                                rmiServerController.getServerDashboardController().updateConsole(String.format(
                                        "<div style=\"background-color: orange; color: white;\">%s %s[%s] %s</div>",
                                        new Date(), LangManger.get("node"),
                                        StringEscapeUtils.escapeHtml3(row.getKey()),
                                        LangManger.get("error_lost_connection")));
                            }
                        }
                    }
                    Thread.sleep(100);
                }
            } catch (InterruptedException ignored) {
            }
        });
        onlineChecker.start();
    }

    public static void stopOnlineChecker() {
        onlineChecker.interrupt();
        for (Map.Entry<String, ProfileRow> row : profileMap.entrySet()) {
            row.getValue().isOnline = false;
        }
    }

    public static ProfileRow getProfileRow(String nodeName) {
        try {
            ProfileRow profileRow = profileMap.get(nodeName);
            return profileRow;
        } catch (Exception ex) {
            return null;
        }
    }

    public static List getProfileTableRows() {
        List result = new ArrayList<ProfileTableRow>();
        for (Map.Entry<String, ProfileRow> row : profileMap.entrySet()) {
            result.add(new ProfileTableRow(row.getKey(), row.getValue().isOnline));
        }

        return result;
    }

    public static void putProfileRow(String nodeName, ProfileRow profileRow) {
        profileMap.put(nodeName, profileRow);
    }

    public static void removeProfileRow(String nodeName) {
        try {
            profileMap.remove(nodeName);
        } catch (Exception ex) {
            return;
        }
    }

    public static int getProfileSize() {
        return profileMap.size();
    }
}
