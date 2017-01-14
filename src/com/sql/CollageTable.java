package com.sql;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class CollageTable extends JFrame  {
    private JTable table = new JTable();

    private DefaultTableModel model = new DefaultTableModel();

    private Object[] columns = {"class", "lecturer", "Course id"};


    Connection conn ;

    private TableRowSorter<TableModel> rowSorter;

    Object[] row = new Object[6];
    private String[][] data;

    public CollageTable(Connection con) throws HeadlessException, SQLException {
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
        setTitle("Class query");
        setLocationRelativeTo(null);

        add(pane);


        PreparedStatement searched = con.prepareStatement("SELECT C.DAY,C.BEGINNING,C.END,L.ID,L.NAME,L.LAST_NAME,C.COURSE_ID,C.CLASS FROM COLLAGE_TABLE AS C JOIN LECTURERS_TABLE AS L ON  L.ID =C.LECTURER_ID ");
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