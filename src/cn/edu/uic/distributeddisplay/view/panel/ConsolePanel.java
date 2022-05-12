package cn.edu.uic.distributeddisplay.view.panel;

import javax.swing.*;
import java.awt.*;

public class ConsolePanel extends JPanel {
    private JEditorPane consoleEditorPane;
    private JScrollPane consoleScrollPane;

    public ConsolePanel() {
        // Initialize center panel
        initCenterPanelComponents();
        initCenterPanelLayout();

        // Initialize bottom panel
//        initBottomPanel();
        setMinimumSize(new Dimension(300, 125));
        setPreferredSize(new Dimension(300, 125));
        setMaximumSize(new Dimension(-1, 125));
    }

    public JEditorPane getConsoleEditorPane() {
        return consoleEditorPane;
    }

    private void initCenterPanelComponents() {
        consoleEditorPane = new JEditorPane();
        consoleEditorPane.setContentType("text/html");
        consoleEditorPane.setText("<html><body><div id=\"container\"><div>Ready</div></div></body></html>");

        consoleScrollPane = new JScrollPane(consoleEditorPane);
    }

    private void initCenterPanelLayout() {
        setBorder(BorderFactory.createTitledBorder("Console"));
        setLayout(new BorderLayout());

        add(consoleScrollPane, BorderLayout.CENTER);
    }

//    private void initBottomPanel() {
//        JPanel bottomPanel = new JPanel();
//        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
//        listenButton = new JButton("Listen");
//        bottomPanel.add(listenButton);
//
//        add(bottomPanel, BorderLayout.SOUTH);
//    }
}
