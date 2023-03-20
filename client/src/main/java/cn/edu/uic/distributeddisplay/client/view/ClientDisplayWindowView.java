package cn.edu.uic.distributeddisplay.client.view;

import cn.edu.uic.distributeddisplay.core.util.LangManger;
import cn.edu.uic.distributeddisplay.core.view.DisplayWindowView;

import javax.swing.*;

public class ClientDisplayWindowView extends DisplayWindowView {

    private JPopupMenu rightClickMenu;
    private JMenuItem disconnectItem;
    private JMenuItem statusItem;
    private JMenuItem exitItem;

    public ClientDisplayWindowView() {
        super();

        // Initialize the right click menu
        initRightClickMenu();
    }

    private void initRightClickMenu() {
        rightClickMenu = new JPopupMenu();
        statusItem = new JMenuItem(LangManger.get("status:") + LangManger.get("not_connected"));
        statusItem.setEnabled(false);
        disconnectItem = new JMenuItem(LangManger.get("disconnect"));
        exitItem = new JMenuItem(LangManger.get("exit"));

        rightClickMenu.add(statusItem);
        rightClickMenu.addSeparator();
        rightClickMenu.add(disconnectItem);
        rightClickMenu.add(exitItem);
    }

    public JPopupMenu getRightClickMenu() {
        return rightClickMenu;
    }

    public JMenuItem getDisconnectItem() {
        return disconnectItem;
    }

    public JMenuItem getStatusItem() {
        return statusItem;
    }

    public JMenuItem getExitItem() {
        return exitItem;
    }
}
