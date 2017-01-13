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

//import static com.sun.tools.internal.xjc.reader.Ring.add;

public class ClassQuery extends JFrame {

    private Connection conn ;
    private JTable table;

    private TableRowSorter<TableModel> rowSorter;
    private DefaultTableModel model;
    private Object[] columns = {"LECTURER_ID", "COURSE_ID", "CLASS"};

    Object[] row = new Object[6];
    private String[][] data;
    JTextField id;

    ClassQuery(Connection con) throws HeadlessException, SQLException {
        conn = con;
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table = new JTable();
        table.setModel(model);
        JButton search = new JButton("search");
        id = new JTextField("Class id",20);

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
        setTitle("Class query");
        setLocationRelativeTo(null);

        add(pane);

        add(id);
        add(search);

        setVisible(true);

        search.addActionListener(new ButtonClickListener());
    }

    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

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
    public class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent a) {
            String command = a.getActionCommand();
            String i = id.getText();
            if(command.equals("search")){
                try{
                    PreparedStatement searched = conn.prepareStatement("SELECT LECTURER_ID,COURSE_ID,CLASS FROM COLLAGE_TABLE WHERE  CLASS IN(SELECT CLASS FROM CLASS_TABLE WHERE CLASS = ?)");
                    searched.setString(1,i);
                    ResultSet result = searched.executeQuery();

                    model = new DefaultTableModel(data, columns) {
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
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}