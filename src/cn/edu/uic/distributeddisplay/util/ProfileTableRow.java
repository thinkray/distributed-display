/**
 * A row in a ProfileTable
 *
 * @author Team 3
 * @version 1.0
 */
package cn.edu.uic.distributeddisplay.util;

public class ProfileTableRow {
    public String nodeName = "";
    public boolean isOnline = false;

    public ProfileTableRow(String nodeName, boolean isOnline) {
        this.nodeName = nodeName;
        this.isOnline = isOnline;
    }

    public Object[] toObjectArray() {
        return new Object[]{nodeName, isOnline};
    }
}
