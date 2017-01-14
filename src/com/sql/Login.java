package com.sql;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Login extends JFrame {
    static JTextField server;
    static JTextField dbName ;
    static JLabel lbs;
    static JLabel lbDb;
    static JButton ok ;
    private static Connection con ;
    String url;
    public Login() throws HeadlessException {
            lbs = new JLabel("serever:port");
            server = new JTextField("",100);
            lbDb = new JLabel("DB name");
            dbName = new JTextField("",100);
            ok = new JButton("login");


            ok.setActionCommand("login");
            ok.addActionListener(new ButtonClickListener());


            lbs.setBounds(200,10,100,20);
            server.setBounds(150,40,200,20);

            lbDb.setBounds(200,70,100,20);
            dbName.setBounds(200,100,100,20);

            ok.setBounds(200,140,100,20);

            add(lbs);add(server);add(lbDb);add(dbName);add(ok);
            setLayout(new FlowLayout());
            setResizable(false);
            setLayout(null);
            setSize(500,300);
            setTitle("Scheduler APP login");
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(true);
    }
    private  void connect() throws Exception {
        try {
            String driver = "com.mysql.jdbc.Driver";
            //url = "jdbc:mysql://localhost:8889/schedulerDB";
            //url = "jdbc:mysql://localhost:3306/schedulerDB";
            String userName = "root";
            String password = "root";
            Class.forName(driver);

            con = DriverManager.getConnection(url, userName, password);
            JOptionPane.showMessageDialog(null,"connected succeed");



        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"connected failed");
            e.printStackTrace();
        }
    }

    private  void triggersCreator() throws Exception{
        try {
            PreparedStatement trigger = con.prepareStatement("CREATE TRIGGER PHONES_DELETE BEFORE DELETE ON LECTURERS_TABLE FOR EACH ROW BEGIN DELETE FROM PHONE_TABLE WHERE ID = old.ID;END ");
            trigger.executeLargeUpdate();
            PreparedStatement trigger2 = con.prepareStatement("CREATE TRIGGER CLASS_DELETE BEFORE DELETE ON CLASS_TABLE FOR EACH ROW BEGIN DELETE FROM COLLAGE_TABLE WHERE CLASS = old.CLASS;END ");
            trigger2.executeLargeUpdate();
            PreparedStatement trigger3 = con.prepareStatement("CREATE TRIGGER COURSE_DELETE BEFORE DELETE ON COURSES_TABLE FOR EACH ROW BEGIN DELETE FROM COLLAGE_TABLE WHERE COURSE_ID = old.ID;END ");
            trigger3.executeLargeUpdate();
            PreparedStatement trigger4 = con.prepareStatement("CREATE TRIGGER LECT_DELETE BEFORE DELETE ON LECTURERS_TABLE FOR EACH ROW BEGIN DELETE FROM COLLAGE_TABLE WHERE LECTURER_ID = old.ID;END ");
            trigger4.executeLargeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static void triggerDist() throws Exception {
        try {
            PreparedStatement trigger = con.prepareStatement("DROP TRIGGER PHONES_DELETE;");
            trigger.executeLargeUpdate();
            PreparedStatement trigger1 = con.prepareStatement("DROP TRIGGER CLASS_DELETE;");
            trigger1.executeLargeUpdate();
            PreparedStatement trigger2 = con.prepareStatement("DROP TRIGGER COURSE_DELETE;");
            trigger2.executeLargeUpdate();
            PreparedStatement trigger3 = con.prepareStatement("DROP TRIGGER LECT_DELETE;");
            trigger3.executeLargeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if(command == "login"){
                url  = "jdbc:mysql://" +server.getText()+"/"+ dbName.getText();
                try {
                    connect();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                try {
                    triggersCreator();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if(con != null) {
                    Gui g = new Gui(con);
                    setVisible(false);

                }

            }

        }

    }

}
