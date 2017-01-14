package com.sql;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

public class Gui extends JFrame {
    JButton lect  = new JButton("Lecturer");
    JButton courses  = new JButton("Courses");
    JButton classroom  = new JButton("classroom");
    JButton report1  = new JButton("lecturer query");
    JButton report2  = new JButton("classroom query");
    JButton report3  = new JButton("time query");
    JButton sched  = new JButton("scheduler");
    Connection  conn ;

    public Gui(Connection con) throws HeadlessException {
        conn = con;
        lect.setActionCommand("l");
        courses.setActionCommand("c");
        classroom.setActionCommand("cl");
        report1.setActionCommand("r1");
        report2.setActionCommand("r2");
        report3.setActionCommand("r3");
        sched.setActionCommand("s");

        lect.addActionListener(new ButtonClickListener());
        courses.addActionListener(new ButtonClickListener());
        classroom.addActionListener(new ButtonClickListener());
        report1.addActionListener(new ButtonClickListener());
        report2.addActionListener(new ButtonClickListener());
        report3.addActionListener(new ButtonClickListener());
        sched.addActionListener(new ButtonClickListener());

        lect.setBounds(22,10,350,100);
        add(lect);
        courses.setBounds(22,110,350,100);
        add(courses);
        classroom.setBounds(22,210,350,100);
        add(classroom);
        report1.setBounds(22,310,350,100);
        add(report1);
        report2.setBounds(22,410,350,100);
        add(report2);
        report3.setBounds(22,510,350,100);
        add(report3);
        sched.setBounds(22,610,350,100);
        add(sched);

        setLayout(new FlowLayout());
        setResizable(false);
        setLayout(null);
        setSize(400,750);
        setTitle("Scheduler APP");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command == "l"){
                try {
                    Lecturer l = new Lecturer(conn);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
            if (command == "c"){
                Courses c = new Courses(conn);
            }
            if (command == "cl"){
                ClassRoom  cl = new ClassRoom(conn);

            }
            if (command == "r1"){
                try {
                    LecturerQuery r1 = new LecturerQuery(conn);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
            if (command == "r2"){
                try {
                    ClassQuery r2 = new ClassQuery(conn);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
            if (command == "r3"){
                try {
                    CollageRangeQuery r3 = new CollageRangeQuery(conn);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
            if (command == "s"){
                Collage c = new Collage(conn);
            }
        }

    }
}
