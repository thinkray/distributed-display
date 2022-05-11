/**
 * A class that registers the current profile
 *
 * @author Bohui WU
 * @since 12/25/2019
 */
package cn.edu.uic.distributeddisplay.util;

import cn.edu.uic.distributeddisplay.profile.Profile;

public class ProfileManager {

    private static Profile profile;

    public static Profile getProfile() {
        return profile;
    }

    public static void setProfile(Profile profile) {
        ProfileManager.profile = profile;
    }

}
