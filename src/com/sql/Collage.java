package com.sql;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

import static com.sql.ClassRoom.cValArr;
import static com.sql.ClassRoom.makeClassArr;
import static com.sql.Courses.coursesValArr;
import static com.sql.Courses.makeCourseArr;
import static com.sql.Lecturer.lValArr;
import static com.sql.Lecturer.makeLectArr;

public class Collage extends JFrame {
    private JTextField lID;
    private JTextField cID;
    private JTextField clNum;
    private JTextField day;
    private JTextField begin;
    private String sLID,sCID,sCNUM,sDay,sBegin;
    private static ArrayList<String> valArr = new ArrayList<>();
    private static ArrayList<ArrayList<String>> qarr = new ArrayList<ArrayList<String>>();
    private static Connection conn;
    private enum Day {
        SATURDAY,SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
        THURSDAY, FRIDAY
    }


    Collage(Connection con) throws HeadlessException {
        conn = con;
        lID = new JTextField("com.sql.Lecturer ID",20);
        cID = new JTextField("course ID",20);
        clNum = new JTextField("Class num",20);
        day = new JTextField("Day - sun",3);
        begin = new JTextField("Begin",3);
        JButton addB = new JButton("add");
        JButton search = new JButton("search");
        JButton delete = new JButton("delete");
        JButton updateB = new JButton("update");

        addB.setActionCommand("add");
        search.setActionCommand("search");
        delete.setActionCommand("delete");
        updateB.setActionCommand("update");

        addB.addActionListener(new ButtonClickListener());
        search.addActionListener(new ButtonClickListener());
        delete.addActionListener(new ButtonClickListener());
        updateB.addActionListener(new ButtonClickListener());



        setSize(500,500);
        setTitle("scheduler Table");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        lID.setBounds(30,10,100,20);
        cID.setBounds(30,40,100,20);
        clNum.setBounds(30,70,100,20);
        day.setBounds(30,100,100,20);
        begin.setBounds(30,130,100,20);
        addB.setBounds(160,130,100,20);
        search.setBounds(160,100,100,20);
        delete.setBounds(280,100,100,20);
        updateB.setBounds(280,130,100,20);


        add(lID);add(cID);add(clNum);add(day);add(begin);add(addB);add(search);add(delete);add(updateB);
        setLayout(new BorderLayout());
        setResizable(false);
        setLayout(new FlowLayout());
        setLayout(null);
        setVisible(true);

    }
    private void clearSearch(){
        sLID = "";
        sCID = "";
        sCNUM = "";
        sDay = "";
        sBegin = "";
    }
    private void clear(){
        lID.setText("");
        cID.setText("");
        clNum.setText("");
        day.setText("");
        begin.setText("");
    }
    private static void addToCollage(String lid, String cid, String classnum, String day, String beginning) throws Exception{
        try {
            PreparedStatement search = conn.prepareStatement("SELECT * FROM LECTURERS_TABLE WHERE ID =?");
            search.setString(1,lid);
            ResultSet r = search.executeQuery();
            makeLectArr(r);
            if(lValArr.size()>0) {
                search = conn.prepareStatement("SELECT * FROM CLASS_TABLE WHERE CLASS = ?");
                search.setString(1,classnum);
                r = search.executeQuery();
                makeClassArr(r);
                if(cValArr.size()>0) {
                    search = conn.prepareStatement("SELECT * FROM COURSES_TABLE WHERE ID = ?");
                    search.setString(1,cid);
                    r = search.executeQuery();
                    makeCourseArr(r);
                    if(coursesValArr.size()>0){
                        float end = Float.parseFloat(coursesValArr.get(2));
                        end += Float.parseFloat(beginning);
                        PreparedStatement posted = conn.prepareStatement("INSERT INTO COLLAGE_TABLE (LECTURER_ID, COURSE_ID, CLASS, DAY, BEGINNING, END) VALUES (?,?,?,?,?,?)");
                        posted.setString(1,lid);
                        posted.setString(2,cid);
                        posted.setString(3,classnum);
                        posted.setInt(4,Integer.parseInt(day));
                        posted.setFloat(5,Float.parseFloat(beginning));
                        posted.setFloat(6,end);
                        //data validation
                        posted.executeLargeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void searchCollage(String lid, String cid, String classnum, String day, String beginning) throws Exception {
        try {
            PreparedStatement searched = conn.prepareStatement("SELECT * FROM COLLAGE_TABLE WHERE (LECTURER_ID = ? AND COURSE_ID= ? AND CLASS=? AND DAY= ?  AND BEGINNING= ?)");
            searched.setString(1,lid);searched.setString(2,cid);searched.setString(3,classnum);searched.setString(4,day);searched.setString(5,beginning);
            ResultSet result = searched.executeQuery();
            makeCollageArr(result);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void deleteFromCollage(String lid, String cid, String classnum, String day, String beginning) throws Exception {
        PreparedStatement deleted = conn.prepareStatement("DELETE FROM COLLAGE_TABLE WHERE LECTURER_ID = ? AND COURSE_ID= ? AND CLASS=? AND DAY= ?  AND BEGINNING= ?");
        //delete from  all tables before;
        deleted.setString(1,lid);
        deleted.setString(2,cid);
        deleted.setString(3,classnum);
        deleted.setString(4,day);
        deleted.setString(5,beginning);
        deleted.executeLargeUpdate();
    }
    private static void makeCollageArr(ResultSet r){
        try {
            valArr = new ArrayList<>();
            while (r.next()){
                valArr.add(r.getString("LECTURER_ID"));
                valArr.add(r.getString("COURSE_ID"));
                valArr.add(r.getString("CLASS"));
                valArr.add(r.getString("DAY"));
                valArr.add(r.getString("BEGINNING"));
                System.out.println(valArr.get(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void updateCollage(String lid, String cid, String classnum, String day, String beginning) throws Exception {
        PreparedStatement searched = conn.prepareStatement("SELECT * FROM COLLAGE_TABLE WHERE LECTURER_ID = ? AND COURSE_ID= ? AND CLASS=? AND DAY= ? AND BEGINNING=?");
        searched.setString(1,lid);searched.setString(2,cid);searched.setString(3,classnum);searched.setString(4,day);searched.setString(5,beginning);
        ResultSet result = searched.executeQuery();
        makeCollageArr(result);
        makeCollageArr(result);
        if(valArr.size() == 0) {
            PreparedStatement updated = conn.prepareStatement("UPDATE COLLAGE_TABLE SET LECTURER_ID = ? AND COURSE_ID= ? AND CLASS=? AND DAY= ? AND BEGINNING=? WHERE LECTURER_ID = ? AND COURSE_ID= ? AND CLASS=? AND DAY= ? AND BEGINNING=? ");
            updated.setString(1,lid);updated.setString(2,cid);
            updated.setString(3,classnum);updated.setString(4,day);
            updated.setString(5,beginning);updated.setString(6,lid);
            updated.setString(7,cid);
            updated.setString(8,classnum);updated.setString(9,day);
            updated.setString(10,beginning);

            long t = updated.executeLargeUpdate();
            if (t>0)
                System.out.println("row updated");
            else
                System.out.println("row didn't found");
        } else
            System.out.println("can't update lecturer which have course attached");
        //need to update collage table first*/
    }
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            String lect = lID.getText(),cl=clNum.getText(),co=cID.getText(),d=day.getText(),b=begin.getText();
            switch (command) {
                case "add":
                    try {
                        addToCollage(lID.getText(), cID.getText(), clNum.getText(), day.getText(), begin.getText());
                        clear();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    clearSearch();
                    break;
                case "search":
                    try {
                        searchCollage(lID.getText(),cID.getText(), clNum.getText(), day.getText(), begin.getText());
                        if(valArr.size() != 0) {
                            lID.setText(valArr.get(0));
                            cID.setText(valArr.get(1));
                            clNum.setText(valArr.get(2));
                            day.setText(valArr.get(3));
                            begin.setText(valArr.get(4));
                            sLID = lID.getText();
                            sCID = cID.getText();
                            sCNUM = clNum.getText();
                            sDay = day.getText();
                            sBegin = begin.getText();
                        } else
                            System.out.println("no record");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "delete":
                    try {
                        deleteFromCollage(lID.getText(), cID.getText(), clNum.getText(), day.getText(), begin.getText());
                        clear();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    clearSearch();
                    break;
                case "update":
                    try {
                        updateCollage(lID.getText(), cID.getText(), clNum.getText(), day.getText(), begin.getText());
                        clear();
                        clearSearch();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
            }
        }
    }

    public static ResultSet queryAll() throws SQLException {
        PreparedStatement searched = conn.prepareStatement("SELECT * FROM COLLAGE_TABLE");
        ResultSet resultSet = searched.executeQuery();
        return resultSet;
    }
    private boolean validation(String n){
        return n.length() > 0 && n.length() < 15;
    }

    private boolean validateDif(float l, char y, char s){
        return l > 1 && l < 10 && (y == 'A'||y == 'B'||y== 'C'||y=='D'||y=='a'||y=='b'||y=='c'||y=='d') && (s == 'A'||s == 'B'||s == 'S'||s=='a'||s=='b'||s=='s');
    }
}