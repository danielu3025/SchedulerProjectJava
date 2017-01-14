package com.sql;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class LecturerTable extends JFrame {
    private JTable table = new JTable();

    private DefaultTableModel model = new DefaultTableModel();

    private Object[] columns = {"class", "lecturer", "Course id"};


    Connection conn ;

    private TableRowSorter<TableModel> rowSorter;

    Object[] row = new Object[6];
    private String[][] data;

    public LecturerTable(Connection con) throws HeadlessException, SQLException {
        conn = con;
        model.setColumnIdentifiers(columns);
        table.setModel(model);

        table.setRowHeight(30);


        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(10, 10, 682, 200);

        setLayout(new BorderLayout());
        setLayout(new FlowLayout());
        setResizable(false);
        setLayout(null);
        setSize(700,600);
        setTitle("Lectuer Table");
        setLocationRelativeTo(null);

        add(pane);


        PreparedStatement searched = con.prepareStatement("SELECT * FROM LECTURERS_TABLE");
        ResultSet result = searched.executeQuery();

        model = new DefaultTableModel(data,columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };

        try {
            table.setModel(buildTableModel(result));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setVisible(true);

    }
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }
}