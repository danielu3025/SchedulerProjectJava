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
    private static String sLID="",sCID="",sCNUM="",sDay="",sBegin="",sEnd="",sTableID="";
    private static ArrayList<String> valArr = new ArrayList<>();
    private static ArrayList<ArrayList<String>> qarr = new ArrayList<ArrayList<String>>();
    private static Connection conn;

    Collage(Connection con) throws HeadlessException {
        conn = con;
        lID = new JTextField("",20);
        cID = new JTextField("",20);
        clNum = new JTextField("",20);
        day = new JTextField("",4);
        begin = new JTextField("",4);
        JButton addB = new JButton("add");
        JButton search = new JButton("search");
        JButton delete = new JButton("delete");
        JButton updateB = new JButton("update");
        JButton showTable = new JButton("show Table");

        JLabel lbID = new JLabel("LecturerID 1-15#");
        JLabel lbCid = new JLabel("courseID 1-15#");
        JLabel lbCnum = new JLabel("Class num 1-15#");
        JLabel lbDay = new JLabel("Day-1-6:");
        JLabel lbBeg = new JLabel("Begin 8-20:");


        addB.setActionCommand("add");
        search.setActionCommand("search");
        delete.setActionCommand("delete");
        updateB.setActionCommand("update");
        showTable.setActionCommand("s");

        addB.addActionListener(new ButtonClickListener());
        search.addActionListener(new ButtonClickListener());
        delete.addActionListener(new ButtonClickListener());
        updateB.addActionListener(new ButtonClickListener());
        showTable.addActionListener(new ButtonClickListener());




        setSize(500,500);
        setTitle("scheduler");
        lbID.setBounds(30,10,150,20);
        lID.setBounds(30,35,100,20);

        lbCid.setBounds(30,65,150,20);
        cID.setBounds(30,90,100,20);

        lbCnum.setBounds(30,115,150,20);
        clNum.setBounds(30,140,100,20);

        lbDay.setBounds(30,165,150,20);
        day.setBounds(30,182,100,20);

        lbBeg.setBounds(30,205,150,20);
        begin.setBounds(30,230,100,20);


        search.setBounds(160,230,100,20);
        addB.setBounds(160,260,100,20);
        delete.setBounds(280,230,100,20);
        updateB.setBounds(280,260,100,20);
        showTable.setBounds(160,300,220,20);



        add(lID);add(cID);add(clNum);add(day);add(begin);add(addB);add(search);add(delete);add(updateB);
        add(lbBeg);add(lbCid);add(lbCnum);add(lbDay);add(lbID);add(showTable);
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
        conn.setAutoCommit(false);
        boolean test = false;
        boolean check;
        try{
            PreparedStatement search = conn.prepareStatement("SELECT * FROM LECTURERS_TABLE WHERE ID =?");
            search.setString(1, lid);
            ResultSet r = search.executeQuery();
            makeLectArr(r);
            if (lValArr.size() > 0) {
                search = conn.prepareStatement("SELECT * FROM CLASS_TABLE WHERE CLASS = ?");
                search.setString(1, classnum);
                r = search.executeQuery();
                makeClassArr(r);
                if (cValArr.size() > 0) {
                    search = conn.prepareStatement("SELECT * FROM COURSES_TABLE WHERE ID = ?");
                    search.setString(1, cid);
                    r = search.executeQuery();
                    makeCourseArr(r);
                    if (coursesValArr.size() > 0) {
                        float length = Float.parseFloat(coursesValArr.get(2));
                        float end = length + Float.parseFloat(beginning);
                        if (validateEnd(end)) {
                            search = conn.prepareStatement("SELECT BEGINNING, DAY, LECTURER_ID, CLASS, TABLE_ID, END FROM COLLAGE_TABLE WHERE DAY = ?");
                            search.setInt(1, Integer.parseInt(day));
                            r = search.executeQuery();
                            check = testHours(r, length, Float.parseFloat(beginning), end, lid, classnum, Integer.parseInt(day));
                            if (check) {
                                PreparedStatement posted = conn.prepareStatement("INSERT INTO COLLAGE_TABLE (LECTURER_ID, COURSE_ID, CLASS, DAY, BEGINNING, END) VALUES (?,?,?,?,?,?)");
                                posted.setString(1, lid);
                                posted.setString(2, cid);
                                posted.setString(3, classnum);
                                posted.setInt(4, Integer.parseInt(day));
                                posted.setFloat(5, Float.parseFloat(beginning));
                                posted.setFloat(6, end);
                                //data validation
                                long t = posted.executeLargeUpdate();
                                if (t > 0) {
                                    System.out.println("row created");
                                    test = true;
                                } else
                                    System.out.println("something went wrong");
                            } else {
                                System.out.println("can't update course due to course end time");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            conn.rollback();
            e.printStackTrace();
        } finally {
            conn.setAutoCommit(true);
        }
        if (!test)
            System.out.println("please fix you new record data");
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
        conn.setAutoCommit(false);
        try {
            PreparedStatement deleted = conn.prepareStatement("DELETE FROM COLLAGE_TABLE WHERE LECTURER_ID = ? AND COURSE_ID= ? AND CLASS=? AND DAY= ?  AND BEGINNING= ?");
            //delete from  all tables before;
            deleted.setString(1,lid);
            deleted.setString(2,cid);
            deleted.setString(3,classnum);
            deleted.setString(4,day);
            deleted.setString(5,beginning);
            long t = deleted.executeLargeUpdate();
            conn.commit();
            if(t>0){
                System.out.println("record deleted");
            } else
                System.out.println("record not found");
        }
        catch (Exception e){
            e.printStackTrace();
            conn.rollback();
            System.out.println("rollback");
        }
        finally {
            conn.setAutoCommit(true);
        }

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
                valArr.add(r.getString("END"));
                valArr.add(r.getString("TABLE_ID"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static boolean testHours(ResultSet r,float len, float beggin, float end, String lect, String clas, int d){
        try {
            coursesValArr = new ArrayList<>();
            while (r.next()){
                coursesValArr.add(r.getString("BEGINNING"));
                coursesValArr.add(r.getString("DAY"));
                coursesValArr.add(r.getString("LECTURER_ID"));
                coursesValArr.add(r.getString("CLASS"));
                coursesValArr.add(r.getString("TABLE_ID"));
                coursesValArr.add(r.getString("END"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(coursesValArr.size()>0){
            for (int i = 0; i<coursesValArr.size(); i++) {
                float beg1 = Float.parseFloat(coursesValArr.get(i));
                int day = Integer.parseInt(coursesValArr.get(++i));
                String lecturer1 = coursesValArr.get(++i);
                String class1 = coursesValArr.get(++i);
                String id1 = coursesValArr.get(++i);
                float end1 = Float.parseFloat(coursesValArr.get(++i));
                if(d == day && !sTableID.equals(id1)){
                    if(lect.equals(lecturer1) || clas.equals(class1)){
                        if((beggin>= beg1 && beggin<= end1) || (end >= beg1 && end<= end1)){
                            return false;
                        }
                    }
                }

            }
        }
        return true;
    }
    private static void updateCollage(String lid, String cid, String classnum, int day, float beginning) throws Exception {
        boolean test = false;
        conn.setAutoCommit(false);
        boolean check;
        float length = Float.parseFloat(sEnd) - Float.parseFloat(sBegin);
        try{
            PreparedStatement search = conn.prepareStatement("SELECT * FROM LECTURERS_TABLE WHERE ID =?");
            search.setString(1, lid);
            ResultSet r = search.executeQuery();
            makeLectArr(r);
            if (lValArr.size() > 0) {
                search = conn.prepareStatement("SELECT * FROM CLASS_TABLE WHERE CLASS = ?");
                search.setString(1, classnum);
                r = search.executeQuery();
                makeClassArr(r);
                if (cValArr.size() > 0) {
                    search = conn.prepareStatement("SELECT * FROM COURSES_TABLE WHERE ID = ?");
                    search.setString(1, cid);
                    r = search.executeQuery();
                    makeCourseArr(r);
                    if (coursesValArr.size() > 0) {
                        float end1 = Float.parseFloat(coursesValArr.get(2));
                        end1 += beginning;
                        if (validateEnd(end1)) {
                            search = conn.prepareStatement("SELECT BEGINNING, DAY, LECTURER_ID, CLASS, TABLE_ID, END FROM COLLAGE_TABLE WHERE DAY = ?");
                            search.setInt(1,day);
                            r = search.executeQuery();
                            check = testHours(r,length,beginning,end1,lid,classnum,day);
                            if(check) {
                                PreparedStatement updated = conn.prepareStatement("UPDATE COLLAGE_TABLE SET LECTURER_ID=?, COURSE_ID=?, CLASS=?, DAY=?, BEGINNING=?, END=? WHERE LECTURER_ID=? AND COURSE_ID=? AND CLASS=? AND DAY=? AND BEGINNING=?");
                                updated.setString(1, lid);
                                updated.setString(2, cid);
                                updated.setString(3, classnum);
                                updated.setInt(4, day);
                                updated.setFloat(5, beginning);
                                updated.setFloat(6, end1);
                                updated.setString(7, sLID);
                                updated.setString(8, sCID);
                                updated.setString(9, sCNUM);
                                updated.setString(10, sDay);
                                updated.setString(11, sBegin);
                                long t = updated.executeLargeUpdate();
                                if (t > 0) {
                                    System.out.println("row updated");
                                    test = true;
                                } else
                                    System.out.println("something went wrong");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            conn.rollback();
            e.printStackTrace();
        }finally {
            conn.setAutoCommit(true);
        }
        if (!test)
            System.out.println("please fix you new update data");
    }
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            String lect = lID.getText(),cl=clNum.getText(),co=cID.getText(),d=day.getText(),b=begin.getText();
            switch (command) {
                case "add":
                    try {
                        if(validation(lect, cl, co,d,b) && validateDif(Integer.parseInt(d),Float.parseFloat(b)))
                            addToCollage(lID.getText(), cID.getText(), clNum.getText(), day.getText(), begin.getText());
                        else
                            System.out.println("please fix inputs");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    clearSearch();
                    break;
                case "search":
                    try {
                        searchCollage(lect, co, cl, d, b);
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
                            sEnd = valArr.get(5);
                            sTableID = valArr.get(6);
                            System.out.println("record found");
                        } else
                            System.out.println("no record");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "delete":
                    try {
                        deleteFromCollage(lect, co, cl, d, b);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    clearSearch();
                    break;
                case "update":
                    try {
                        if(validation(sLID,sCID,sCNUM,sDay,sBegin)){
                            if(validation(lect, cl, co,d,b) && validateDif(Integer.parseInt(d),Float.parseFloat(b)))
                                updateCollage(lect, co, cl, Integer.parseInt(d), Float.parseFloat(b));
                            else
                                System.out.println("you need to fix inputs");
                        } else
                            System.out.println("you need to search before update");

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "s":
                    try {
                        CollageTable ct = new CollageTable(conn);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
            }
        }
    }

    public static ResultSet queryAll() throws SQLException {
        PreparedStatement searched = conn.prepareStatement("SELECT * FROM COLLAGE_TABLE");
        ResultSet resultSet = searched.executeQuery();
        return resultSet;
    }
    private boolean validation(String lN, String CN, String CI, String d, String b){
        return lN.length() > 0 && lN.length() < 15 && CN.length() > 0 && CN.length() < 15 && CI.length() > 0 && CI.length() < 15 && d.length()>0 && b.length()>0;
    }

    private boolean validateDif(int d, float s){

        return d >= 1 && d <= 6 && s >=8 && s<= 20;
    }
    private static boolean validateEnd(float e){
        return e <= 21 && e>= 8.5;
    }

    public boolean duplicatersDitection(String l,String c ,String cr,String d, String b ,String e) throws SQLException {
        PreparedStatement search = conn.prepareStatement("SELECT * FROM COLLAGE_TABLE WHERE LECTURER_ID =? AND COURSE_ID =? AND CLASS = ? AND DAY=? AND BEGINNING=? AND =?");
        search.setString(1,l);
        search.setString(2,c);
        search.setString(3,cr);
        search.setString(4,d);
        search.setString(5,b);search.setString(6,e);
        long t = search.executeLargeUpdate();
        if (t>0){
            return true;
        }
        return false;
    }
}