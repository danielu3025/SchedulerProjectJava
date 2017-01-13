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

public class LecturerQuery extends JFrame {

    private Connection conn ;
    DefaultTableModel model;
    private Object[] columns = {"lecturer id", "class number", "course id,day,time"};
    private JTable table;
    private TableRowSorter<TableModel> rowSorter;
    Object[] row = new Object[6];
    private String[][] data;
    private JTextField id;

    public LecturerQuery(Connection con) throws HeadlessException, SQLException {
        conn = con;
        Collage collage = new Collage(conn);
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table = new JTable();
        table.setModel(model);
        JButton search = new JButton("search");
        id = new JTextField("com.sql.Lecturer id",20);
        table.setRowHeight(30);

        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(10, 10, 682, 200);
        search.setBounds(130,230,100,20);
        id.setBounds(10,230,100,20);

        setLayout(new BorderLayout());
        setLayout(new FlowLayout());
        setResizable(false);
        setLayout(null);
        setSize(700,430);
        setTitle("Teacher query");
        setLocationRelativeTo(null);

        add(pane);
        add(id);
        add(search);

        setVisible(true);

        search.addActionListener(new ButtonClickListener());

    }
    public class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            String command = a.getActionCommand();
            String i = id.getText();
            if(command.equals("search")){
                try{
                PreparedStatement searched = conn.prepareStatement("SELECT LECTURER_ID,COURSE_ID,CLASS,DAY,BEGINNING FROM COLLAGE_TABLE WHERE  LECTURER_ID IN(SELECT ID FROM LECTURERS_TABLE WHERE ID = ?)");
                searched.setString(1,i);
                ResultSet result = searched.executeQuery();

                model = new DefaultTableModel(data, columns) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;//This causes all cells to be not editable
                    }
                };

                    table.setModel(buildTableModel(result));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
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