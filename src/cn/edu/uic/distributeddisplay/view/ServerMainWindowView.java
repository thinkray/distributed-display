package cn.edu.uic.distributeddisplay.view;

import cn.edu.uic.distributeddisplay.util.LangManger;
import cn.edu.uic.distributeddisplay.util.ViewsManager;
import cn.edu.uic.distributeddisplay.view.panel.*;

import javax.swing.*;
import java.awt.*;

public class ServerMainWindowView extends JFrame {

    // Dashboard
    private ServerConfigPanel serverConfigPanel;
    private NodeListPanel nodeListPanel;
    private DisplayConfigPanel displayConfigPanel;
    private ConsolePanel consolePanel;

    // Files
    private JMenuBar menuBar;

    // Menu: File
    private JMenu fileMenu;
    private JMenuItem saveItem;
    private JMenuItem exitItem;

    // Menu: Language
    private JMenu languageMenu;
    private JMenuItem enusItem;
    private JMenuItem zhhansItem;
    private JMenuItem zhhantItem;

    // Menu: Help
    private JMenu helpMenu;
    private JMenuItem helpItem;
    private JMenuItem aboutItem;

    public ServerMainWindowView() {
        // Register references
        ViewsManager.setMainWindowView(this);

        setBounds(0, 0, 20, 20);
        setTitle(LangManger.get("preference"));

        initMenuBar();

        setLayout(new BorderLayout());

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.PAGE_AXIS));
        serverConfigPanel = new ServerConfigPanel();
        jPanel.add(serverConfigPanel);
        nodeListPanel = new NodeListPanel();
        jPanel.add(nodeListPanel);
        add(jPanel, BorderLayout.LINE_START);
//        add(nodeConnectionConfigPanel, BorderLayout.LINE_START);

        // Server Dashboard Panel
        displayConfigPanel = new DisplayConfigPanel();
        add(displayConfigPanel, BorderLayout.CENTER);

        consolePanel = new ConsolePanel();
        add(consolePanel, BorderLayout.SOUTH);

        // Prepare the window for display
        setSize(800, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initMenuBar() {
        // Prepare the menu
        menuBar = new JMenuBar();

        // Menu: File
        fileMenu = new JMenu(LangManger.get("file"));
        saveItem = new JMenuItem(LangManger.get("save"));
        exitItem = new JMenuItem(LangManger.get("exit"));
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // Menu: Language
        languageMenu = new JMenu(LangManger.get("language"));
        enusItem = new JMenuItem("English");
        zhhansItem = new JMenuItem("简体中文");
        zhhantItem = new JMenuItem("正體中文");
        languageMenu.add(enusItem);
        languageMenu.add(zhhansItem);
        languageMenu.add(zhhantItem);
        menuBar.add(languageMenu);

        // Menu: Help
        helpMenu = new JMenu(LangManger.get("help"));
        helpItem = new JMenuItem(LangManger.get("help"));
        aboutItem = new JMenuItem(LangManger.get("about"));
        helpMenu.add(helpItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        // Register the menu bar to JFrame
        setJMenuBar(menuBar);
    }

    public ServerConfigPanel getServerConfigPanel() {
        return serverConfigPanel;
    }

    public NodeListPanel getNodeListPanel() {
        return nodeListPanel;
    }

    public DisplayConfigPanel getDisplayConfigPanel() {
        return displayConfigPanel;
    }

    public ConsolePanel getConsolePanel() {
        return consolePanel;
    }

    public JMenuItem getSaveItem() {
        return saveItem;
    }

    public JMenuItem getExitItem() {
        return exitItem;
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

    public JMenuItem getAboutItem() {
        return aboutItem;
    }

    public JMenuItem getHelpItem() {
        return helpItem;
    }
}
