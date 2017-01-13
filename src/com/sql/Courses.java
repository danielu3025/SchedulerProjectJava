package com.sql;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Courses extends JFrame{
    private JTextField id;
    private JTextField name;
    private JTextField length;
    private JTextField year;
    private JTextField semester;
    private JButton showC;
    static ArrayList<String> coursesValArr = new ArrayList<>();
    private static Connection coursesConn;

    public Courses(Connection con) {
        coursesConn = con;
        id = new JTextField("",15);
        name = new JTextField("",15);
        length = new JTextField("",4);
        semester = new JTextField("",4);
        year = new JTextField("",4);
        showC = new JButton("");

        JLabel lbID = new JLabel("course id");
        JLabel lbname = new JLabel("name");
        JLabel lblength = new JLabel("length: 2.5");
        JLabel lbsemester = new JLabel("semester");
        JLabel lbyear = new JLabel("year - yyyy");


        JButton addB = new JButton("add");
        JButton search = new JButton("search");
        JButton deleteB = new JButton("delete");
        JButton updateB = new JButton("update");

        addB.setActionCommand("add");
        search.setActionCommand("search");
        deleteB.setActionCommand("delete");
        updateB.setActionCommand("update");
        showC.setActionCommand("C");

        addB.addActionListener(new ButtonClickListener());
        search.addActionListener(new ButtonClickListener());
        deleteB.addActionListener(new ButtonClickListener());
        updateB.addActionListener(new ButtonClickListener());
        showC.addActionListener(new ButtonClickListener());


        setSize(500,500);
        setTitle("Course Form");

        lbID.setBounds(30,10,100,20);
        id.setBounds(30,40,100,20);

        lbname.setBounds(30,70,100,20);
        name.setBounds(30,100,100,20);

        lblength.setBounds(30,130,100,20);
        length.setBounds(30,160,100,20);

        lbsemester.setBounds(30,190,100,20);
        semester.setBounds(30,250,100,20);

        lbyear.setBounds(30,290,100,20);
        year.setBounds(30,330,100,20);

        addB.setBounds(160,130,100,20);
        search.setBounds(160,100,100,20);
        deleteB.setBounds(280,100,100,20);
        updateB.setBounds(280,130,100,20);

        showC.setBounds(160,200,100,20);

        add(id);add(name);add(length);add(semester);add(year);add(addB);add(search);add(deleteB);add(updateB);
        add(lbID);add(lbname);add(lbsemester);add(lblength);add(lbyear); add(showC);
        setLayout(new BorderLayout());
        setResizable(false);
        setLayout(new FlowLayout());
        setLayout(null);
        setVisible(true);

    }

    private void addCourse(String id, String name, float length, char semester, char year) throws Exception{
        try {
            searchCourse(id);
            if (coursesValArr.size() > 0) {
                System.out.println("can't add existing course");
            } else {
                PreparedStatement posted = coursesConn.prepareStatement("INSERT INTO COURSES_TABLE (ID, LENGTH, SEMESTER, YEAR ,NAME) VALUES (?,?,?,?,?)");
                posted.setString(1, id);
                posted.setFloat(2, length);
                posted.setString(3, String.valueOf(semester));
                posted.setString(4,String.valueOf(year));
                posted.setString(5, name);
                posted.executeLargeUpdate();
                System.out.println("course added");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchCourse(String id) throws Exception {
        try {
            PreparedStatement searched = coursesConn.prepareStatement("SELECT * FROM COURSES_TABLE WHERE ID=?");
            searched.setString(1,id);
            ResultSet result = searched.executeQuery();
            makeCourseArr(result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteCourse(String id) throws Exception {
            PreparedStatement deleted = coursesConn.prepareStatement("DELETE  FROM COURSES_TABLE WHERE ID=?");
            deleted.setString(1,id);
            long t = deleted.executeLargeUpdate();
            if (t>0)
                System.out.println("row deleted");
            else
                System.out.println("row didn't found");
    }

    static void makeCourseArr(ResultSet r){
        try {
            coursesValArr = new ArrayList<>();
            while (r.next()){
                coursesValArr.add(r.getString("ID"));
                coursesValArr.add(r.getString("NAME"));
                coursesValArr.add(r.getString("LENGTH"));
                coursesValArr.add(r.getString("SEMESTER"));
                coursesValArr.add(r.getString("YEAR"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCourse(String id, String name, float length, char semester, char year) throws Exception {
            coursesConn.setAutoCommit(false);
            try{
                PreparedStatement updated = coursesConn.prepareStatement("UPDATE COURSES_TABLE SET NAME=?, LENGTH=?, SEMESTER=?, YEAR=? WHERE ID=?");
                updated.setString(1,name);
                updated.setFloat(2,length);
                updated.setString(3,String.valueOf(semester));
                updated.setString(4,String.valueOf(year));
                updated.setString(5,id);
                System.out.println("redt to transucion");
                coursesConn.commit();
                long t = updated.executeLargeUpdate();
                if (t>0)
                    System.out.println("row updated");
                else
                    System.out.println("row didn't found");

            }catch (Exception e){
                e.printStackTrace();
            }
            finally {
                coursesConn.setAutoCommit(false);
            }
    }

    private void clear(){
        id.setText("");name.setText("");length.setText("");semester.setText("");year.setText("");
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            String n = name.getText(), i = id.getText();
            String l = length.getText();
            String s = semester.getText(), y = year.getText();
            switch (command) {
                case "add":
                    try {
                        if(validation(i) && validation(n)&& validateDif(Float.parseFloat(l),y.charAt(0),s.charAt(0))) {
                            addCourse(i, n, Float.parseFloat(l), s.charAt(0), y.charAt(0));
                        } else
                            System.out.println("please fix inputs");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    clear();
                    break;
                case "search":
                    try {
                        if (validation(i)) {
                            searchCourse(i);
                            if (coursesValArr.size() > 0){
                                id.setText(coursesValArr.get(0));
                                name.setText(coursesValArr.get(1));
                                length.setText(coursesValArr.get(2));
                                semester.setText(coursesValArr.get(3));
                                year.setText(coursesValArr.get(4));
                            } else
                                System.out.println("no record");
                        } else
                            System.out.println("fix inputs");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    break;
                case "delete":
                    try {
                        if (validation(i))
                            deleteCourse(i);
                        else
                            System.out.println("please fix inputs");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    clear();
                    break;
                case "update":
                    try {
                        if (validation(i) && validation(n) && validateDif(Float.parseFloat(l), y.charAt(0), s.charAt(0)))
                            updateCourse(i,n,Float.parseFloat(l),s.charAt(0),y.charAt(0));
                        else
                            System.out.println("please fix inputs");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    clear();
                    break;
                case "C":
                    Courses c = new Courses(coursesConn);
            }
        }
    }
    private boolean validation(String n){
        return n.length() > 0 && n.length() < 15;
    }

    private boolean validateDif(float l, char y, char s){
        return l > 1 && l < 10 && (y == 'A'||y == 'B'||y== 'C'||y=='D'||y=='a'||y=='b'||y=='c'||y=='d') && (s == 'A'||s == 'B'||s == 'S'||s=='a'||s=='b'||s=='s');
    }
}