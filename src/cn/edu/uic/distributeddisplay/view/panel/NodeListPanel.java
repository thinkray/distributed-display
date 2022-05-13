package cn.edu.uic.distributeddisplay.view.panel;

import cn.edu.uic.distributeddisplay.model.NodeListTableModel;
import cn.edu.uic.distributeddisplay.util.ProfileManager;
import cn.edu.uic.distributeddisplay.util.ProfileRow;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import java.awt.*;

public class NodeListPanel extends JPanel {
    private NodeListTableModel nodeListTableModel;
    private JTable nodeListTable;
    private JScrollPane nodeListTableScrollPane;
    private Thread updateWorker;

    public NodeListPanel() {
        // Initialize center panel
        initCenterPanelComponents();
        initCenterPanelLayout();
        initUpdateWorker();

        // Initialize bottom panel
//        initBottomPanel();
        setMinimumSize(new Dimension(300, 125));
        setPreferredSize(new Dimension(300, 125));
    }

    private void initCenterPanelComponents() {
        Object[][] data = {{"Kundan Kumar Jha", new Boolean(true)}, {"Anand Jha", new Boolean(false)}};

        // Column Names
        String[] columnNames = {"Node Name", "Online"};

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

        add(nodeListTableScrollPane, BorderLayout.CENTER);
    }

    private void initUpdateWorker() {
        updateWorker = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (ProfileManager.getProfileSize() != nodeListTable.getRowCount()) {
                            nodeListTableModel.setDataVector(ProfileManager.getProfileTableRows());
                        } else {
                            Boolean refreshFlag = false;
                            for (int i = 0; i < nodeListTableModel.getRowCount(); i++) {
                                String currentNodeName = (String) nodeListTableModel.getValueAt(i, 0);
                                ProfileRow profileRowFromProfileManager = ProfileManager.getProfileRow(currentNodeName);
                                if (profileRowFromProfileManager == null) {
                                    refreshFlag = true;
                                    break;
                                } else {
                                    nodeListTableModel.setValueAt(profileRowFromProfileManager.isOnline, i, 1);
                                }
                            }
                            if (refreshFlag) {
                                nodeListTableModel.setDataVector(ProfileManager.getProfileTableRows());
                            }
                        }

                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        };

        updateWorker.start();
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
