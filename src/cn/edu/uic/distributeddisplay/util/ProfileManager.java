/**
 * A class that registers the current profile
 *
 * @author Bohui WU
 * @since 12/25/2019
 */
package cn.edu.uic.distributeddisplay.util;

import cn.edu.uic.distributeddisplay.profile.ServerSideProfile;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileManager {

    private static File profileDirectory = new File(ConfigManager.getConfigEntry("profile_location"));
    private static ConcurrentHashMap<String, ProfileRow> profileMap = new ConcurrentHashMap<>();
    private static Thread onlineChecker;

    public static void saveProfileListToFile() {
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
            Log.log("Profile saved.");
        } catch (Exception e) {
            // TODO: Print error
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
            Log.log("Profile loaded.");
        } catch (Exception e) {
            profileMap = new ConcurrentHashMap<>();
        }
    }

    public static void startOnlineChecker() {
        onlineChecker = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Date currentDate = new Date();
                        for (Map.Entry<String, ProfileRow> row : profileMap.entrySet()) {
                            row.getValue().isOnline = Duration.between(row.getValue().lastSeen.toInstant(),
                                    currentDate.toInstant()).toMillis() < 5000;
                        }
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        };
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

    public static int getProfileSize() {
        return profileMap.size();
    }
}
