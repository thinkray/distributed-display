package cn.edu.uic.distributeddisplay.model;

import javax.swing.table.DefaultTableModel;

public class NodeListTableModel extends DefaultTableModel {
    public NodeListTableModel(Object[][] data, String[] columnNames) {
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
}
