/**
 * The node list panel
 *
 * @author Team 3
 * @version 1.0
 */

package cn.edu.uic.distributeddisplay.server.view.panel;

import cn.edu.uic.distributeddisplay.core.util.*;
import cn.edu.uic.distributeddisplay.server.model.ClientListTableModel;
import cn.edu.uic.distributeddisplay.server.util.ProfileManager;
import cn.edu.uic.distributeddisplay.server.util.ProfileRow;

import javax.swing.*;
import java.awt.*;

public class ClientListPanel extends JPanel {
    private JTextField newNodeNameTextField;
    private JButton addNodeButton;
    private JButton deleteNodeButton;
    private ClientListTableModel clientListTableModel;
    private JTable nodeListTable;
    private JScrollPane nodeListTableScrollPane;
    private Thread updateWorker;

    public ClientListPanel() {
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
        clientListTableModel = new ClientListTableModel(data, columnNames);
        nodeListTable.setModel(clientListTableModel);
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
                            clientListTableModel.setDataVector(ProfileManager.getProfileTableRows());
                        } else {
                            Boolean refreshFlag = false;
                            for (int i = 0; i < clientListTableModel.getRowCount(); i++) {
                                String currentNodeName = (String) clientListTableModel.getValueAt(i, 0);
                                ProfileRow profileRowFromProfileManager = ProfileManager.getProfileRow(currentNodeName);
                                if (profileRowFromProfileManager == null) {
                                    refreshFlag = true;
                                    break;
                                } else {
                                    clientListTableModel.setValueAt(profileRowFromProfileManager.isOnline, i, 1);
                                }
                            }
                            if (refreshFlag) {
                                clientListTableModel.setDataVector(ProfileManager.getProfileTableRows());
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
}
