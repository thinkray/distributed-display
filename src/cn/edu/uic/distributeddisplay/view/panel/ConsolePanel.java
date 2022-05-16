/**
 * The Console panel
 *
 * @author Team 3
 * @version 1.0
 */

package cn.edu.uic.distributeddisplay.view.panel;

import cn.edu.uic.distributeddisplay.util.LangManger;

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
        consoleEditorPane.setEditable(false);
        consoleEditorPane.setText(String.format("<html><body><div id=\"container\"><div>%s</div></div></body></html>"
                , LangManger.get("ready")));

        consoleScrollPane = new JScrollPane(consoleEditorPane);
    }

    private void initCenterPanelLayout() {
        setBorder(BorderFactory.createTitledBorder(LangManger.get("console")));
        setLayout(new BorderLayout());

        add(consoleScrollPane, BorderLayout.CENTER);
    }
}
