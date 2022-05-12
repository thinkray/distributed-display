package cn.edu.uic.distributeddisplay.util;

import cn.edu.uic.distributeddisplay.profile.ServerSideProfile;

import java.util.Date;

public class ProfileTableRow {
    public String nodeName = "";
    public boolean isOnline = false;

    public ProfileTableRow(String nodeName, boolean isOnline)
    {
        this.nodeName = nodeName;
        this.isOnline = isOnline;
    }
    public Object[] toObjectArray() {
        return new Object[]{nodeName, isOnline};
    }
}
