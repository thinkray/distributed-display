package cn.edu.uic.distributeddisplay.server.util;

import cn.edu.uic.distributeddisplay.core.util.ViewsManager;
import cn.edu.uic.distributeddisplay.server.view.ServerMainWindowView;

public class ServerViewsManager extends ViewsManager {

    private static ServerMainWindowView serverMainWindowView;

    public static ServerMainWindowView getMainWindowView() {
        return serverMainWindowView;
    }

    public static void setMainWindowView(ServerMainWindowView serverMainWindowView) {
        // Not allowed to change the reference once it is set
        if (serverMainWindowView != null) {
            ServerViewsManager.serverMainWindowView = serverMainWindowView;
        }
    }


}
