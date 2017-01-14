package com.sql;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;

import static com.sql.Login.triggerDist;

public class Main {

    public static void main(String[] args) throws Exception {
        Login l = new Login();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                  triggerDist();
            } catch (Exception r) {
                r.printStackTrace();
            }
        }));
    }

}
