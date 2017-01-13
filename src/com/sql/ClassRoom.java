package com.sql;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;

//import static com.sql.GlobalFunctions.Arr;
//import static com.sql.GlobalFunctions.searchInCollage;

class ClassRoom extends JFrame {
    private JTextField number;
    private JTextField building;
    private JTextField floor;
    private static Connection classCon;
    static ArrayList<String> cValArr = new ArrayList<>();
    JButton showClassroms;


    public ClassRoom(Connection conn) throws HeadlessException {
        classCon = conn;
        number = new JTextField("class number",20);
        building = new JTextField("building",20);
        floor = new JTextField("floor",20);
        JLabel lbNum = new JLabel("classroom Num");
        JLabel lbBuilding = new JLabel("building Num");
        JLabel lbFloor = new JLabel("floor Num");

        showClassroms = new JButton("show Classrooms");


        JButton addB = new JButton("add");
        JButton search = new JButton("search");
        JButton delete = new JButton("delete");
        JButton updateB = new JButton("update");


        addB.setActionCommand("add");
        search.setActionCommand("search");
        delete.setActionCommand("delete");
        updateB.setActionCommand("update");
        showClassroms.setActionCommand("s");


        addB.addActionListener(new ButtonClickListener());
        search.addActionListener(new ButtonClickListener());
        delete.addActionListener(new ButtonClickListener());
        updateB.addActionListener(new ButtonClickListener());
        showClassroms.addActionListener(new ButtonClickListener());




        setSize(500,500);
        setTitle("ClassRoom");

        lbNum.setBounds(30,10,100,20);
        number.setBounds(30,40,100,20);

        lbBuilding.setBounds(30,70,100,20);
        building.setBounds(30,100,100,20);

        lbFloor.setBounds(30,130,100,20);
        floor.setBounds(30,160,100,20);



        addB.setBounds(30,200,100,20);
        search.setBounds(160,200,100,20);
        delete.setBounds(280,200,100,20);
        updateB.setBounds(30,230,100,20);


        add(number);add(building);add(floor);add(addB);add(search);add(delete);add(updateB);
        add(lbNum);add(lbBuilding);add(lbFloor);
        setLayout(new BorderLayout());
        setResizable(false);
        setLayout(new FlowLayout());
        setLayout(null);
        setVisible(true);

    }
    private void clear(){
        number.setText("");building.setText("");floor.setText("");
    }

    private static void addClass(String b, String n, String f) throws Exception{
        try {
            searchClass(n);
            if (cValArr.size()>0){
                System.out.println("class already exist");
            }else {
                PreparedStatement posted = classCon.prepareStatement("INSERT INTO CLASS_TABLE (BUILDING, CLASS, FLOOR) VALUES (?,?,?)");
                posted.setString(1,b);
                posted.setString(2,n);
                posted.setString(3,f);
                posted.executeLargeUpdate();
                System.out.println("class added");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void searchClass(String n) throws Exception {
        try {
            PreparedStatement searched = classCon.prepareStatement("SELECT * FROM CLASS_TABLE WHERE CLASS=?");
            searched.setString(1,n);
            ResultSet result = searched.executeQuery();
            makeClassArr(result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    static void makeClassArr(ResultSet r){
        try {
            cValArr = new ArrayList<>();
            while (r.next()){
                cValArr.add(r.getString("ClASS"));
                cValArr.add(r.getString("BUILDING"));
                cValArr.add(r.getString("FLOOR"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void deleteClass(String n) throws Exception {
            PreparedStatement deleted = classCon.prepareStatement("DELETE  FROM CLASS_TABLE WHERE CLASS=?");
            deleted.setString(1,n);
            long t = deleted.executeLargeUpdate();
            if (t>0)
                System.out.println("row deleted");
             else
                 System.out.println("row didn't found");
    }

    private static void updateClass(String b, String n, String f) throws Exception {
            PreparedStatement updated = classCon.prepareStatement("UPDATE CLASS_TABLE SET FLOOR=?,BUILDING=? WHERE CLASS=?");
            updated.setString(1,f);
            updated.setString(2,b);
            updated.setString(3,n);
            long t = updated.executeLargeUpdate();
            if (t>0)
                System.out.println("row updated");
             else
                System.out.println("row didn't found");
    }
    public class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            String b = building.getText(), n = number.getText(), f = floor.getText();
            switch (command) {
                case "add":
                    try {
                        if(validation(b)&&validation(n)&&validation(f)) {
                            addClass(b,n,f);
                            clear();
                        } else
                            System.out.println("please fix the inputs");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "search":
                    try {
                        if (validation(n)) {
                            searchClass(n);
                            if (cValArr.size() > 0) {
                                number.setText(cValArr.get(0));
                                building.setText(cValArr.get(1));
                                floor.setText(cValArr.get(2));
                                System.out.println("filled all records");
                            } else
                                System.out.println("no record");
                        }else
                            System.out.println("please fix class number");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "delete":
                    try {
                        if (validation(n)) {
                            deleteClass(n);
                            clear();
                        } else
                            System.out.println("please fix class number");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "update":
                    try {
                        if (validation(b)&&validation(n)&&validation(f)) {
                            updateClass(b, n, f);
                            clear();
                        } else
                            System.out.println("please fix the inputs");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "s":

            }
        }
    }
    private boolean validation(String n){
        return n.length() > 0 && n.length() < 15;
    }
}
