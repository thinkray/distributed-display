package cn.edu.uic.distributeddisplay.util;

import cn.edu.uic.distributeddisplay.profile.ServerSideProfile;

import java.util.Date;

public class ProfileRow {
    public ServerSideProfile serverSideProfile = null;
    public boolean isOnline = false;
    public Date lastSeen = null;
    public String uuid = "";
    public Boolean newConfigAvailable = true;

    public ProfileRow(ServerSideProfile serverSideProfile, boolean isOnline, Date lastSeen, String uuid) {
        this.serverSideProfile = serverSideProfile;
        this.isOnline = isOnline;
        this.lastSeen = lastSeen;
        this.uuid = uuid;
    }
}
