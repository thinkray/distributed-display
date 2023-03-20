package cn.edu.uic.distributeddisplay.server.model;


import cn.edu.uic.distributeddisplay.server.util.ProfileTableRow;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Vector;

public class ClientListTableModel extends DefaultTableModel {
    public ClientListTableModel(Object[][] data, String[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return String.class;
            case 1:
            default:
                return Boolean.class;
        }
    }

    public void setDataVector(List<ProfileTableRow> profileTableRows) {
        setDataVector(new Vector(), super.columnIdentifiers);
        for (int i = 0; i < profileTableRows.size(); i++) {
            addRow(profileTableRows.get(i).toObjectArray());
        }
    }
}
