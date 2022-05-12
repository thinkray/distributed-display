/**
 * A class that registers the current profile
 *
 * @author Bohui WU
 * @since 12/25/2019
 */
package cn.edu.uic.distributeddisplay.util;

import cn.edu.uic.distributeddisplay.profile.ServerSideProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileManager {

    private static ConcurrentHashMap<String, ProfileRow> profileMap = new ConcurrentHashMap<>();

    public static ProfileRow getProfileRow(String nodeName) {
        return profileMap.get(nodeName);
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
