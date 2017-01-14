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

public class CollageRangeQuery extends JFrame {
    private Connection conn;
    private DefaultTableModel model;
    private Object[] columns = {"LECTURER_ID","COURSE_ID", "CLASS", "DAY", "BEGINNING","END"};
    private JTable table;
    private TableRowSorter<TableModel> rowSorter;
    Object[] row = new Object[6];
    private String[][] data;
    private JTextField startHour;
    private JTextField endHour;
    private JTextField startDay;
    private JTextField endDay;

    public CollageRangeQuery(Connection con) throws HeadlessException, SQLException {
        conn = con;
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table = new JTable();
        table.setModel(model);
        JButton search = new JButton("search");
        startHour = new JTextField("start hour", 20);
        endHour = new JTextField("end hour", 20);
        startDay = new JTextField("start day", 20);
        endDay = new JTextField("end day", 20);
        table.setRowHeight(30);

        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(10, 10, 682, 200);
        search.setBounds(50, 290, 100, 20);
        startHour.setBounds(10, 230, 100, 20);
        endHour.setBounds(130, 230, 100, 20);
        startDay.setBounds(10, 260, 100, 20);
        endDay.setBounds(130, 260, 100, 20);

        setLayout(new BorderLayout());
        setLayout(new FlowLayout());
        setResizable(false);
        setLayout(null);
        setSize(700, 430);
        setTitle("Range query");
        setLocationRelativeTo(null);

        add(pane);
        add(startDay);
        add(endDay);
        add(startHour);
        add(endHour);
        add(search);

        setVisible(true);

        search.addActionListener(new ButtonClickListener());

    }

    public class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            String command = a.getActionCommand();
            if (command.equals("search")) {
                try {
                    String sD = startDay.getText(), sH = startHour.getText(), eD = endDay.getText(), eH = endHour.getText();
                    PreparedStatement searched = conn.prepareStatement("SELECT LECTURER_ID,COURSE_ID,CLASS,DAY,BEGINNING,END FROM COLLAGE_TABLE WHERE DAY >= ? AND DAY <= ?");
                    searched.setString(1, sD);
                    searched.setString(2, eD);

                    ResultSet result = searched.executeQuery();

                    model = new DefaultTableModel(data, columns) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;//This causes all cells to be not editable
                        }
                    };

                    table.setModel(buildTableModel(result,Float.parseFloat(sH),Float.parseFloat(eH)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static DefaultTableModel buildTableModel(ResultSet rs,float st,float en) throws SQLException {

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
            float t1 = Float.parseFloat(vector.elementAt(4).toString());
            float t2 = Float.parseFloat(vector.elementAt(5).toString());
            if((t1 >=st && t1 <=en) || (t2>=st && t2 <=en))
                data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }
}