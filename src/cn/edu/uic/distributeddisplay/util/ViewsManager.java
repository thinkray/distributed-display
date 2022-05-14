/**
 * The ViewsManger class registers the references of the views
 *
 * @author Team 3
 * @version 1.0
 */

package cn.edu.uic.distributeddisplay.util;

import cn.edu.uic.distributeddisplay.view.DisplayView;
import cn.edu.uic.distributeddisplay.view.NodeConfigView;
import cn.edu.uic.distributeddisplay.view.ServerMainWindowView;

public class ViewsManager {

    private static DisplayView displayView;
    private static ServerMainWindowView serverMainWindowView;
    private static NodeConfigView nodeConfigView;

    public static DisplayView getDisplayView() {
        return displayView;
    }

    public static void setDisplayView(DisplayView displayView) {
        // Close the old DisplayView
        if (ViewsManager.displayView != null) {
            ViewsManager.displayView.dispose();
        }
        ViewsManager.displayView = displayView;
    }

    public static ServerMainWindowView getMainWindowView() {
        return serverMainWindowView;
    }

    public static void setMainWindowView(ServerMainWindowView serverMainWindowView) {
        // Not allowed to change the reference once it is set
        if (serverMainWindowView != null) {
            ViewsManager.serverMainWindowView = serverMainWindowView;
        }
    }

    public static NodeConfigView getNodeConfigView() {
        return nodeConfigView;
    }

    public static void setNodeConfigView(NodeConfigView nodeConfigView) {
        ViewsManager.nodeConfigView = nodeConfigView;
    }
}
