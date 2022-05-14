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
    private JMenuItem preferenceItem;
    private JMenuItem exitItem;
    private JPanel textPanel;
    private JLabel backgroundLabel;

    public DisplayView() {
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

    private void initRightClickMenu() {
        rightClickMenu = new JPopupMenu();
        preferenceItem = new JMenuItem(LangManger.get("preference"));
        exitItem = new JMenuItem(LangManger.get("exit"));
        rightClickMenu.add(preferenceItem);
        rightClickMenu.addSeparator();
        rightClickMenu.add(exitItem);
    }

    public JPopupMenu getRightClickMenu() {
        return rightClickMenu;
    }

    public JMenuItem getPreferenceItem() {
        return preferenceItem;
    }

    public JMenuItem getExitItem() {
        return exitItem;
    }

    public JPanel getTextPanel() {
        return textPanel;
    }

    public void setTextPanel(JPanel textPanel) {
        if (this.textPanel != null) {
            remove(this.textPanel);
        }
        this.textPanel = textPanel;
        add(textPanel, 0);
    }

    public JLabel getBackgroundLabel() {
        return backgroundLabel;
    }

    public void setBackgroundLabel(JLabel backgroundLabel) {
        if (this.backgroundLabel != null) {
            remove(this.backgroundLabel);
        }
        this.backgroundLabel = backgroundLabel;
        add(backgroundLabel);
    }
}
