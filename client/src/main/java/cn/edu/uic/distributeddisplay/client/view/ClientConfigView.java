/**
 * The node connection config panel
 *
 * @author Team 3
 * @version 1.0
 */

package cn.edu.uic.distributeddisplay.client.view;

import cn.edu.uic.distributeddisplay.client.view.panel.ClientConnectionConfigPanel;
import cn.edu.uic.distributeddisplay.core.util.LangManger;

import javax.swing.*;
import java.awt.*;

public class ClientConfigView extends JFrame {
    private final ClientConnectionConfigPanel clientConnectionConfigPanel;
    private final JLabel statusLabel;

    // Files
    private JMenuBar menuBar;

    // Menu: Language
    private JMenu languageMenu;
    private JMenuItem enUsItem = new JMenuItem("English");
    private JMenuItem zhHansItem = new JMenuItem("简体中文");
    private JMenuItem zhHantItem = new JMenuItem("正體中文");

    public ClientConfigView() {
        setTitle(LangManger.get("connect"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        clientConnectionConfigPanel = new ClientConnectionConfigPanel();
        add(clientConnectionConfigPanel, BorderLayout.CENTER);

        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel(" ");
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);

        setAlwaysOnTop(true);

        setMinimumSize(new Dimension(400, 220));
        setMaximizedBounds(new Rectangle(20, 20, 400, 220));

        initMenuBar();
    }

    private void initMenuBar() {
        // Prepare the menu
        menuBar = new JMenuBar();

        // Menu: Language
        languageMenu = new JMenu(LangManger.get("language"));
        languageMenu.add(enUsItem);
        languageMenu.add(zhHansItem);
        languageMenu.add(zhHantItem);
        menuBar.add(languageMenu);

        // Register the menu bar to JFrame
        setJMenuBar(menuBar);
    }

    public JTextField getNodeNameTextField() {
        return clientConnectionConfigPanel.getNodeNameTextField();
    }

    public JTextField getServerAddressTextField() {
        return clientConnectionConfigPanel.getServerAddressTextField();
    }

    public JButton getConnectButton() {
        return clientConnectionConfigPanel.getConnectButton();
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JMenuItem getEnUsItem() {
        return enUsItem;
    }

    public JMenuItem getZhHansItem() {
        return zhHansItem;
    }

    public JMenuItem getZhHantItem() {
        return zhHantItem;
    }
}
