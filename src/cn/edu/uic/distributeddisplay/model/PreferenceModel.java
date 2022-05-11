/**
 * The model for the frame preference. It handles the IO of the current profile
 *
 * @author Bohui WU
 * @since 12/20/2019
 */
package cn.edu.uic.distributeddisplay.model;

import cn.edu.uic.distributeddisplay.profile.Profile;
import cn.edu.uic.distributeddisplay.util.LangManger;
import cn.edu.uic.distributeddisplay.util.Log;
import cn.edu.uic.distributeddisplay.util.ProfileManager;
import cn.edu.uic.distributeddisplay.util.ConfigManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class PreferenceModel {

    private File profileDirectory;
    private Color color;

    public PreferenceModel() {
        profileDirectory = new File(ConfigManager.getConfigEntry("profile_location"));

        if (!profileDirectory.exists()) {
            JOptionPane.showMessageDialog(null, LangManger.get("init_profile"));
            ProfileManager.setProfile(new Profile());
        } else {
            try {
                read();
            } catch (IOException | NullPointerException | ClassNotFoundException e) {
                Log.log(e.getMessage());
                if (JOptionPane.showConfirmDialog(null, LangManger.get("incompatible_profile"),
                        LangManger.get("message"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    // When an incompatible profile is detected
                    ProfileManager.setProfile(new Profile());
                } else {
                    System.exit(1);
                }
            }
        }
    }

    public void save(Profile profile) throws IOException{
        ProfileManager.setProfile(profile);  // Register the profile to the profile manager
        // Write the profile
        FileOutputStream fos = new FileOutputStream(profileDirectory);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(profile);
        out.close();
        fos.close();
        Log.log("Profile saved.");
    }

    public void read() throws IOException, ClassNotFoundException {
        // Read the profile
        FileInputStream fis = new FileInputStream(profileDirectory);
        ObjectInputStream in = new ObjectInputStream(fis);

        // Register the profile to the profile manager
        ProfileManager.setProfile((Profile)in.readObject());
        in.close();
        fis.close();
        Log.log("Profile loaded.");
    }

    public Profile getProfile() {
        // Return the profile in the profile manager
        return ProfileManager.getProfile();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
