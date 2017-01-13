package com.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


class GlobalFunctions {
/*    static ArrayList<String> Arr = new ArrayList<>();

    static void searchInCollage(String res, String val) throws Exception {
        try {
            switch (val) {
                case "CLASS": {
                    PreparedStatement searched1 = con.prepareStatement("SELECT * FROM COLLAGE_TABLE WHERE CLASS = ?");
                    searched1.setString(1, res);
                    ResultSet result = searched1.executeQuery();
                    CreateCollageArr(result);
                    break;
                }
                case "LECTURER_ID": {
                    PreparedStatement searched2 = con.prepareStatement("SELECT * FROM COLLAGE_TABLE WHERE LECTURER_ID = ?");
                    searched2.setString(1, res);
                    ResultSet result = searched2.executeQuery();
                    CreateCollageArr(result);
                    break;
                }
                case "COURSE_ID": {
                    PreparedStatement searched3 = con.prepareStatement("SELECT * FROM COLLAGE_TABLE WHERE COURSE_ID = ?");
                    searched3.setString(1, res);
                    ResultSet result = searched3.executeQuery();
                    CreateCollageArr(result);
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void CreateCollageArr(ResultSet r){
        try {
            Arr = new ArrayList<>();
            while (r.next()){
                Arr.add(r.getString("LECTURER_ID"));
                Arr.add(r.getString("CLASS_NUMBER"));
                Arr.add(r.getString("COURSE_ID"));
                Arr.add(r.getString("DAY"));
                Arr.add(r.getString("BEGINNING"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }*/
}
