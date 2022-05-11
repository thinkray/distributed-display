package cn.edu.uic.distributeddisplay.view.panel;

import cn.edu.uic.distributeddisplay.model.NodeListTableModel;

import javax.swing.*;
import java.awt.*;

public class NodeListPanel extends JPanel {
    private NodeListTableModel nodeListTableModel;
    private JTable nodeListTable;
    private JScrollPane nodeListTableScrollPane;

    public NodeListPanel() {
        // Initialize center panel
        initCenterPanelComponents();
        initCenterPanelLayout();

        // Initialize bottom panel
//        initBottomPanel();
        setMinimumSize(new Dimension(300, 125));
        setPreferredSize(new Dimension(300, 125));
    }

    private void initCenterPanelComponents() {
        Object[][] data = {
                { "Kundan Kumar Jha", new Boolean(true)},
                { "Anand Jha", new Boolean(false)}
        };

        // Column Names
        String[] columnNames = { "Node Name", "Online" };

        // Initializing the JTable
        nodeListTable = new JTable();
        nodeListTableModel = new NodeListTableModel(data, columnNames);
        nodeListTable.setModel(nodeListTableModel);
        nodeListTable.setAutoCreateRowSorter(true);
        // adding it to JScrollPane
        nodeListTableScrollPane = new JScrollPane(nodeListTable);
    }

    private void initCenterPanelLayout() {
        setBorder(BorderFactory.createTitledBorder("Node List"));
        setLayout(new BorderLayout());

        add(nodeListTableScrollPane,BorderLayout.CENTER);
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
