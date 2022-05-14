/**
 * The node connection config panel
 *
 * @author Team 3
 * @version 1.0
 */

package cn.edu.uic.distributeddisplay.view;

import cn.edu.uic.distributeddisplay.util.LangManger;
import cn.edu.uic.distributeddisplay.view.panel.NodeConnectionConfigPanel;

import javax.swing.*;
import java.awt.*;

public class NodeConfigView extends JFrame {
    private final NodeConnectionConfigPanel nodeConnectionConfigPanel;
    private final JLabel statusLabel;

    // Files
    private JMenuBar menuBar;

    // Menu: Language
    private JMenu languageMenu;
    private JMenuItem enusItem;
    private JMenuItem zhhansItem;
    private JMenuItem zhhantItem;

    public NodeConfigView() {
        setTitle(LangManger.get("connect"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        nodeConnectionConfigPanel = new NodeConnectionConfigPanel();
        add(nodeConnectionConfigPanel, BorderLayout.CENTER);

        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel(" ");
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);

        setMinimumSize(new Dimension(400, 200));
        setMaximizedBounds(new Rectangle(20, 20, 400, 200));

        initMenuBar();
    }

    private void initMenuBar() {
        // Prepare the menu
        menuBar = new JMenuBar();

        // Menu: Language
        languageMenu = new JMenu(LangManger.get("language"));
        enusItem = new JMenuItem("English");
        zhhansItem = new JMenuItem("简体中文");
        zhhantItem = new JMenuItem("正體中文");
        languageMenu.add(enusItem);
        languageMenu.add(zhhansItem);
        languageMenu.add(zhhantItem);
        menuBar.add(languageMenu);

        // Register the menu bar to JFrame
        setJMenuBar(menuBar);
    }

    public JTextField getNodeNameTextField() {
        return nodeConnectionConfigPanel.getNodeNameTextField();
    }

    public JTextField getServerAddressTextField() {
        return nodeConnectionConfigPanel.getServerAddressTextField();
    }

    public JButton getConnectButton() {
        return nodeConnectionConfigPanel.getConnectButton();
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JMenuItem getEnusItem() {
        return enusItem;
    }

    public JMenuItem getZhhansItem() {
        return zhhansItem;
    }

    public JMenuItem getZhhantItem() {
        return zhhantItem;
    }
}
