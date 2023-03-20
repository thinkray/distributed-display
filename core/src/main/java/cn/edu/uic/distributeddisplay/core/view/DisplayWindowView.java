/**
 * Display according to the Profile
 *
 * @author Team 3
 * @version 1.0
 */

package cn.edu.uic.distributeddisplay.core.view;

import javax.swing.*;
import java.awt.*;

public class DisplayWindowView extends JFrame {

    private JPanel textPanel;
    private JLabel backgroundLabel;

    public DisplayWindowView() {
        initComponents();

        // Prevent close
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // Full screen mode
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        // Set the default color
        getContentPane().setBackground(Color.BLACK);
    }

    private void initComponents() {
        textPanel = new JPanel();
        add(textPanel);
        backgroundLabel = new JLabel();
        add(backgroundLabel);
    }

    public JPanel getTextPanel() {
        return textPanel;
    }

    public JLabel getBackgroundLabel() {
        return backgroundLabel;
    }
}
