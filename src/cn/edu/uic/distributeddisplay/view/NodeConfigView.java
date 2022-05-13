package cn.edu.uic.distributeddisplay.view;

import cn.edu.uic.distributeddisplay.util.CommonUtils;
import cn.edu.uic.distributeddisplay.util.DefaultConst;
import cn.edu.uic.distributeddisplay.view.panel.NodeConnectionConfigPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class NodeConfigView extends JFrame {
    private NodeConnectionConfigPanel nodeConnectionConfigPanel;
    private JLabel statusLabel;

    public NodeConfigView() {
        setTitle("Connect");
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
}
