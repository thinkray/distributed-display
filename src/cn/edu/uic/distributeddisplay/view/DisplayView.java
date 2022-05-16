/**
 * Display according to the Profile
 *
 * @author Team 3
 * @version 1.0
 */

package cn.edu.uic.distributeddisplay.view;

import cn.edu.uic.distributeddisplay.util.LangManger;

import javax.swing.*;
import java.awt.*;

public class DisplayView extends JFrame {

    private JPopupMenu rightClickMenu;
    private JMenuItem disconnectItem;
    private JMenuItem statusItem;
    private JMenuItem exitItem;
    private JPanel textPanel;
    private JLabel backgroundLabel;

    public DisplayView() {
        initComponents();

        // Prevent close
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // Full screen mode
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        // Initialize the right click menu
        initRightClickMenu();

        // Set the default color
        getContentPane().setBackground(Color.BLACK);
    }

    private void initComponents() {
        textPanel = new JPanel();
        add(textPanel);
        backgroundLabel = new JLabel();
        add(backgroundLabel);
    }

    private void initRightClickMenu() {
        rightClickMenu = new JPopupMenu();
        // statusItem = new JMenuItem(LangManger.get("status:") + LangManger.get("not_connected"));
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

    public JMenuItem getStatusItem() {
        return statusItem;
    }

    public JMenuItem getDisconnectItem() {
        return disconnectItem;
    }

    public JMenuItem getExitItem() {
        return exitItem;
    }

    public JPanel getTextPanel() {
        return textPanel;
    }

    public JLabel getBackgroundLabel() {
        return backgroundLabel;
    }
}
