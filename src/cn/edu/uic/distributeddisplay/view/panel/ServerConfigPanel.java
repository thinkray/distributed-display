package cn.edu.uic.distributeddisplay.view.panel;

import cn.edu.uic.distributeddisplay.util.CommonUtils;
import cn.edu.uic.distributeddisplay.util.DefaultConst;

import javax.swing.*;
import java.awt.*;

public class ServerConfigPanel extends JPanel {
    private JTextField listenAddressTextField;
    private JSpinner listenPortJSpinner;
    private JButton listenButton;

    public ServerConfigPanel() {
        // Initialize center panel
        initCenterPanelComponents();
        initCenterPanelLayout();

        // Initialize bottom panel
        initBottomPanel();

        setMinimumSize(new Dimension(300, 125));
        setPreferredSize(new Dimension(300, 125));
        setMaximumSize(new Dimension(1200, 125));
    }

    public JTextField getListenAddressTextField() {
        return listenAddressTextField;
    }

    public JSpinner getListenPortJSpinner() {
        return listenPortJSpinner;
    }

    public JButton getListenButton() {
        return listenButton;
    }

    private void initCenterPanelComponents() {
        listenAddressTextField = new JTextField();
        listenPortJSpinner = new JSpinner(new SpinnerNumberModel(1099, 1, 65535, 1));
        listenPortJSpinner.setEditor(new JSpinner.NumberEditor(listenPortJSpinner,"#"));
    }

    private void initCenterPanelLayout() {
        setBorder(BorderFactory.createTitledBorder("Config"));
        setLayout(new BorderLayout());

        Container centerPanel = new Container();
        centerPanel.setLayout(new GridBagLayout());
        // Line 1: Machine name
        CommonUtils.gbcNewLine();
        centerPanel.add(new JLabel("Listen Address: "), CommonUtils.getGridBagConstraints(0, 1, 1, 0.2, 1.0, DefaultConst.INSETS_LEFT));
        centerPanel.add(listenAddressTextField, CommonUtils.getGridBagConstraints(1, 8, 1, 1.6, 1.0, DefaultConst.INSETS_RIGHT));

        // Line 2: Server address
        CommonUtils.gbcNewLine();
        centerPanel.add(new JLabel("Port: "), CommonUtils.getGridBagConstraints(0, 1, 1, 0.2, 1.0, DefaultConst.INSETS_LEFT));
        centerPanel.add(listenPortJSpinner, CommonUtils.getGridBagConstraints(1, 8, 1, 1.6, 1.0, DefaultConst.INSETS_RIGHT));

        add(centerPanel, BorderLayout.NORTH);
    }

    private void initBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        listenButton = new JButton("Listen");
        bottomPanel.add(listenButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }
}
