package cn.edu.uic.distributeddisplay.view.panel;

import cn.edu.uic.distributeddisplay.util.CommonUtils;
import cn.edu.uic.distributeddisplay.util.DefaultConst;

import javax.swing.*;
import java.awt.*;

public class NodeConnectionConfigPanel extends JPanel {
    private JTextField nodeNameTextField;
    private JTextField serverAddressTextField;
    private JButton connectButton;

    public NodeConnectionConfigPanel() {
        // Initialize center panel
        initCenterPanelComponents();
        initCenterPanelLayout();

        // Initialize bottom panel
        initBottomPanel();
    }

    public JTextField getNodeNameTextField() {
        return nodeNameTextField;
    }

    public JTextField getServerAddressTextField() {
        return serverAddressTextField;
    }

    public JButton getConnectButton() {
        return connectButton;
    }

    private void initCenterPanelComponents() {
        nodeNameTextField = new JTextField();
        serverAddressTextField = new JTextField();
    }

    private void initCenterPanelLayout() {
        setBorder(BorderFactory.createTitledBorder("Config"));
        setLayout(new BorderLayout());

        Container centerPanel = new Container();
        centerPanel.setLayout(new GridBagLayout());
        // Line 1: Node name
        CommonUtils.gbcNewLine();
        centerPanel.add(new JLabel("Node name: "), CommonUtils.getGridBagConstraints(0, 1, 1, 0.2, 1.0,
                DefaultConst.INSETS_LEFT));
        centerPanel.add(nodeNameTextField, CommonUtils.getGridBagConstraints(1, 8, 1, 1.6, 1.0,
                DefaultConst.INSETS_RIGHT));

        // Line 2: Server address
        CommonUtils.gbcNewLine();
        centerPanel.add(new JLabel("Server: "), CommonUtils.getGridBagConstraints(0, 1, 1, 0.2, 1.0,
                DefaultConst.INSETS_LEFT));
        centerPanel.add(serverAddressTextField, CommonUtils.getGridBagConstraints(1, 8, 1, 1.6, 1.0,
                DefaultConst.INSETS_RIGHT));

        add(centerPanel, BorderLayout.NORTH);
    }

    private void initBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        connectButton = new JButton("Connect");
        bottomPanel.add(connectButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }
}
