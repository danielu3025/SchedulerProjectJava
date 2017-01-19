package com.sql;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Lecturer  extends JFrame{
    private JTextField id;
    private JTextField name;
    private JTextField lastName;
    private JTextField dob;
    private JTextField address;
    private JTextField phone;
    private JLabel age;
    static ArrayList<String> lValArr = new ArrayList<>();
    private static Connection lectConn;

    public Lecturer(Connection con) throws HeadlessException, ParseException {
        lectConn = con;
        id = new JTextField("",15);
        name = new JTextField("",20);
        lastName = new JTextField("",20);
        dob = new JTextField("",20);
        address = new JTextField("",20);
        phone = new JTextField("",20);
        JButton addB = new JButton("add");
        JButton search = new JButton("search");
        JButton delete = new JButton("delete");
        JButton updateB = new JButton("update");
        JButton showPhons = new JButton("show phones");
        JButton showTeacher = new JButton("showTeacher");
        JButton TeachersPhons = new JButton("Teacher's Phons");


        JLabel lbId = new JLabel(" id - 1-15#");
        JLabel lbName = new JLabel("first name");
        JLabel lbLastName = new JLabel("last name");
        JLabel lbBod = new JLabel("birthday - dd/mm/yyyy 1940-2000");
        JLabel lbadresss = new JLabel("address name");
        JLabel lbphone = new JLabel("phone");
        age = new JLabel("Age:");



        addB.setActionCommand("add");
        search.setActionCommand("search");
        delete.setActionCommand("delete");
        updateB.setActionCommand("update");
        showPhons.setActionCommand("showPhons");
        showTeacher.setActionCommand("showT");
        TeachersPhons.setActionCommand("TP");

        addB.addActionListener(new ButtonClickListener());
        search.addActionListener(new ButtonClickListener());
        delete.addActionListener(new ButtonClickListener());
        updateB.addActionListener(new ButtonClickListener());
        showPhons.addActionListener(new ButtonClickListener());
        showTeacher.addActionListener(new ButtonClickListener());
        TeachersPhons.addActionListener(new ButtonClickListener());



        setSize(500,500);
        setTitle("Lecturer");
        lbId.setBounds(30,10,150,20);
        id.setBounds(30,30,100,20);

        lbName.setBounds(30,50,150,20);
        name.setBounds(30,80,100,20);

        lbLastName.setBounds(30,110,150,20);
        lastName.setBounds(30,130,100,20);

        lbBod.setBounds(30,160,250,20);
        dob.setBounds(30,180,100,20);
        age.setBounds(140,180,100,20);


        lbadresss.setBounds(30,210,200,20);
        address.setBounds(30,230,100,20);

        lbphone.setBounds(30,280,100,20);
        phone.setBounds(30,310,100,20);

        addB.setBounds(160,130,100,20);
        search.setBounds(160,100,100,20);
        delete.setBounds(280,100,100,20);
        updateB.setBounds(280,130,100,20);

        showPhons.setBounds(160,350,200,20);
        showTeacher.setBounds(160,380,200,20);







        add(id);add(name);add(lastName);add(dob);add(address);add(addB);add(search);add(delete);add(updateB);add(phone);
        add(lbId);add(lbName);add(lbLastName);add(lbBod);add(lbadresss);add(lbphone);add(age);
        add(showPhons);add(showTeacher);
        setLayout(new BorderLayout());
        setResizable(false);
        setLayout(new FlowLayout());
        setLayout(null);
        setVisible(true);


    }
    private void clear(){
        id.setText("");name.setText("");lastName.setText("");dob.setText("");address.setText("");phone.setText("");age.setText("");
    }
    public static void addLect(String id, String name, String lastName, String dob, String address,String phone) throws Exception{
        try {
            searchLect(id);
            if (lValArr.size()>0){
                System.out.println("can't add existing lecturer");
                JOptionPane.showMessageDialog(null, "can't add existing lecturer");

            } else {
                lectConn.setAutoCommit(false);
                try {
                    PreparedStatement posted = lectConn.prepareStatement("INSERT INTO LECTURERS_TABLE (ID, NAME, LAST_NAME, DOB, ADDRESS,AGE) VALUES (?,?,?,?,?,?)");
                    posted.setString(1,id);
                    posted.setString(2,name);
                    posted.setString(3,lastName);
                    posted.setString(4,dob);
                    posted.setString(5,address);

                    String string = dob;
                    DateFormat format = new SimpleDateFormat("dd/mm/yyyy" , Locale.ENGLISH);
                    Date date = format.parse(string);
                    Date today = new Date();

                    float age = today.getYear()-date.getYear();
                    posted.setString(6,String.valueOf(age));

                    PreparedStatement posted2 = lectConn.prepareStatement("INSERT INTO PHONE_TABLE (ID, PHONE) VALUES (?,?)");
                    posted2.setString(1,id);
                    posted2.setString(2,phone);
                    posted.executeLargeUpdate();
                    posted2.executeLargeUpdate();
                    System.out.println("\n>> Transaction steps are ready.\n");
                    lectConn.commit();
                    System.out.println("lecturer added");
                    JOptionPane.showMessageDialog(null, "lecturer added");

                }catch (Exception e){
                    lectConn.rollback();
                    e.printStackTrace();
                    System.out.println("\n>> Transaction rollback.\n");
                }finally {
                    lectConn.setAutoCommit(true);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void searchLect(String id) throws Exception {
        try {
            PreparedStatement searched = lectConn.prepareStatement("SELECT * FROM LECTURERS_TABLE WHERE ID=?");
            searched.setString(1,id);
            ResultSet result = searched.executeQuery();
            makeLectArr(result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void deleteLect(String id) throws Exception {
        lectConn.setAutoCommit(false);
        try {
            PreparedStatement deleted = lectConn.prepareStatement("DELETE  FROM LECTURERS_TABLE WHERE ID=?");
            deleted.setString(1,id);

            long t = deleted.executeLargeUpdate();
            System.out.println("\n>> Transaction steps are ready.\n");
            lectConn.commit();
            if (t>0) {
                System.out.println("row deleted");
                JOptionPane.showMessageDialog(null, "lecturer deleted");

            }
            else{
                System.out.println("row didn't found");
                JOptionPane.showMessageDialog(null, "row didn't found");
            }

        }catch (Exception e){
            e.printStackTrace();
            lectConn.rollback();
            System.out.println("\n>> Transaction ROLLED BACK.\n");
        }
        finally {
            lectConn.setAutoCommit(true);
        }
    }
    static void makeLectArr(ResultSet r){
        try {
            lValArr = new ArrayList<>();
            while (r.next()){
                lValArr.add(r.getString("ID"));
                lValArr.add(r.getString("NAME"));
                lValArr.add(r.getString("LAST_NAME"));
                lValArr.add(r.getString("DOB"));
                lValArr.add(r.getString("ADDRESS"));
                lValArr.add(r.getString("AGE"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateLect(String id, String name , String lastName, String dob, String address,String phone) throws Exception {
        lectConn.setAutoCommit(false);
        try {

            DateFormat format = new SimpleDateFormat("dd/mm/yyyy" , Locale.ENGLISH);
            Date date = format.parse(dob);
            Date now  = new Date();
            int age = now.getYear()-date.getYear();
            PreparedStatement updated = lectConn.prepareStatement("UPDATE LECTURERS_TABLE SET NAME=?,LAST_NAME=?,DOB=?,ADDRESS=?,age =? WHERE ID=?");

            updated.setString(1,name);
            updated.setString(2,lastName);
            updated.setString(3,dob);
            updated.setString(4,address);
            updated.setString(5,String.valueOf(age));
            updated.setString(6,id);


            String pchack = String.valueOf(phone);
            if (!pchack.isEmpty()){
                char c = phone.charAt(0);
                if (c ==  'N' || c == 'n'){
                    PreparedStatement updated2 = lectConn.prepareStatement("INSERT INTO PHONE_TABLE (ID, PHONE) VALUES (?,?)");
                    phone = phone.substring(1);
                    updated2.setString(1,id);
                    updated2.setString(2,phone);
                    updated2.executeLargeUpdate();
                }

            }

            long t = updated.executeLargeUpdate();
            System.out.println("prepared to commit");
            lectConn.commit();
            if (t>0) {
                System.out.println("row updated");
                JOptionPane.showMessageDialog(null, "row updated");

            }
            else {
                System.out.println("row didn't found");
                JOptionPane.showMessageDialog(null, "row didn't found");
            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("rollback");
        }
        finally {
            lectConn.setAutoCommit(true);
        }
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            String i = id.getText(), n = name.getText(), lN = lastName.getText(), bd = dob.getText(), add = address.getText(), ph=phone.getText();
            switch (command) {
                case "add":
                    try {
                        if (validation(i) && validation(n) && validation(lN) && dobValidation(bd) && validation(add)) {
                            addLect(i, n, lN, bd, add,ph);
                            clear();
                        }else
                        JOptionPane.showMessageDialog(null, "please fix inputs");

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "search":
                    try {
                        if(validation(i)) {
                            searchLect(i);
                            if (lValArr.size() > 0) {
                                id.setText(lValArr.get(0));
                                name.setText(lValArr.get(1));
                                lastName.setText(lValArr.get(2));
                                dob.setText(lValArr.get(3));
                                address.setText(lValArr.get(4));
                                age.setText("Age: " +  lValArr.get(5));
                                JOptionPane.showMessageDialog(null,"filled all records");
                            } else
                                JOptionPane.showMessageDialog(null,"no record");
                        } else
                            JOptionPane.showMessageDialog(null,"please fix inputs");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "delete":
                    try {
                        if (validation(i)){
                            deleteLect(i);
                            clear();
                        }else
                            JOptionPane.showMessageDialog(null,"please fix inputs");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "update":
                    try {
                        if(validation(i) && validation(n) && validation(lN) && dobValidation(bd) && validation(add)) {
                            updateLect(i, n, lN, bd, add,ph);
                            clear();
                        } else
                            JOptionPane.showMessageDialog(null,"please fix inputs");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "showPhons":
                    try {
                        LectPhones tp = new LectPhones(lectConn);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "showT":
                    try {
                        LecturerTable t = new LecturerTable(lectConn);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    break;

            }
        }
    }
    private boolean validation(String n){
        return n.length() > 0 && n.length() < 15;
    }
    private boolean dobValidation(String dob){
        String days,months,years;
        int d, m, y;
        if (dob.length() == 10 && dob.charAt(2) == '/' && dob.charAt(5) == '/'){
            days = dob.substring(0,2);
            d = Integer.parseInt(days);
            months = dob.substring(3,5);
            m = Integer.parseInt(months);
            years = dob.substring(6,10);
            y = Integer.parseInt(years);
            return d>0 && d<32 && m>0 && m<13 && y > 1940 && y < 2000;
        } else
            return false;
    }
}