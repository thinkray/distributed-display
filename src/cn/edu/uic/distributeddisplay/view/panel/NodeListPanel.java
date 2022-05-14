/**
 * The node list panel
 *
 * @author Team 3
 * @version 1.0
 */

package cn.edu.uic.distributeddisplay.view.panel;

import cn.edu.uic.distributeddisplay.model.NodeListTableModel;
import cn.edu.uic.distributeddisplay.util.*;

import javax.swing.*;
import java.awt.*;

public class NodeListPanel extends JPanel {
    private JTextField newNodeNameTextField;
    private JButton addNodeButton;
    private JButton deleteNodeButton;
    private NodeListTableModel nodeListTableModel;
    private JTable nodeListTable;
    private JScrollPane nodeListTableScrollPane;
    private Thread updateWorker;

    public NodeListPanel() {
        setBorder(BorderFactory.createTitledBorder(LangManger.get("node_list")));
        setLayout(new BorderLayout());

        initTopPanel();

        // Initialize center panel
        initCenterPanel();
        initUpdateWorker();

        // Initialize bottom panel
//        initBottomPanel();
        setMinimumSize(new Dimension(300, 125));
        setPreferredSize(new Dimension(300, 125));
    }

    public JTextField getNewNodeNameTextField() {
        return newNodeNameTextField;
    }

    public JButton getAddNodeButton() {
        return addNodeButton;
    }

    public JButton getDeleteNodeButton() {
        return deleteNodeButton;
    }

    public JTable getNodeListTable() {
        return nodeListTable;
    }

    public void initTopPanel() {
        Container centerPanel = new Container();
        centerPanel.setLayout(new GridBagLayout());
        // Line 1: New Profile Name | Add button | Delete button
        CommonUtils.gbcNewLine();
        newNodeNameTextField = new JTextField();
        centerPanel.add(newNodeNameTextField, CommonUtils.getGridBagConstraints(0, 1, 1, 6, 1.0,
                DefaultConst.INSETS_CENTER));
        addNodeButton = new JButton(LangManger.get("add"));
        centerPanel.add(addNodeButton, CommonUtils.getGridBagConstraints(1, 1, 1, 0.5, 1.0,
                DefaultConst.INSETS_CENTER));
        deleteNodeButton = new JButton(LangManger.get("delete"));
        centerPanel.add(deleteNodeButton, CommonUtils.getGridBagConstraints(2, 1, 1, 0.5, 1.0,
                DefaultConst.INSETS_CENTER));

        add(centerPanel, BorderLayout.NORTH);
    }

    private void initCenterPanel() {
        Object[][] data = new Object[][]{};

        // Column Names
        String[] columnNames = {LangManger.get("node_name"), LangManger.get("online")};

        // Initializing the JTable
        nodeListTable = new JTable();
        nodeListTableModel = new NodeListTableModel(data, columnNames);
        nodeListTable.setModel(nodeListTableModel);
        nodeListTable.setAutoCreateRowSorter(true);
        nodeListTable.getTableHeader().setReorderingAllowed(false);
        // adding it to JScrollPane
        nodeListTableScrollPane = new JScrollPane(nodeListTable);
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
