package cn.edu.uic.distributeddisplay.client.util;

import cn.edu.uic.distributeddisplay.client.view.ClientConfigView;
import cn.edu.uic.distributeddisplay.client.view.ClientDisplayWindowView;
import cn.edu.uic.distributeddisplay.core.util.ViewsManager;

public class ClientViewsManager extends ViewsManager {

    private static ClientConfigView clientConfigView;

    private static ClientDisplayWindowView clientDisplayWindowView;

    public static ClientDisplayWindowView getClientDisplayWindowView() {
        return clientDisplayWindowView;
    }

    public static void setClientDisplayWindowView(ClientDisplayWindowView clientDisplayWindowView) {
        ClientViewsManager.clientDisplayWindowView = clientDisplayWindowView;
    }

    public static ClientConfigView getNodeConfigView() {
        return clientConfigView;
    }

    public static void setNodeConfigView(ClientConfigView clientConfigView) {
        ClientViewsManager.clientConfigView = clientConfigView;
    }

}
