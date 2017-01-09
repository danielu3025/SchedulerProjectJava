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
    private JTextField semester;
    private static ArrayList<String> coursesValArr = new ArrayList<>();
    private static Connection coursesConn;

    public Courses(Connection con) {
        coursesConn = con;
        id = new JTextField("course id",20);
        name = new JTextField("name",20);
        length = new JTextField("length: hh:mm",20);
        semester = new JTextField("semester",20);

        JButton addB = new JButton("add");
        JButton search = new JButton("search");
        JButton deleteB = new JButton("delete");
        JButton updateB = new JButton("update");

        addB.setActionCommand("add");
        search.setActionCommand("search");
        deleteB.setActionCommand("delete");
        updateB.setActionCommand("update");

        addB.addActionListener(new ButtonClickListener());
        search.addActionListener(new ButtonClickListener());
        deleteB.addActionListener(new ButtonClickListener());
        updateB.addActionListener(new ButtonClickListener());

        setSize(500,500);
        setTitle("Courses");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        id.setBounds(30,10,100,20);
        name.setBounds(30,40,100,20);
        length.setBounds(30,70,100,20);
        semester.setBounds(30,100,100,20);
        addB.setBounds(160,130,100,20);
        search.setBounds(160,100,100,20);
        deleteB.setBounds(280,100,100,20);
        updateB.setBounds(280,130,100,20);

        add(id);add(name);add(length);add(semester);add(addB);add(search);add(deleteB);add(updateB);
        setLayout(new BorderLayout());
        setResizable(false);
        setLayout(new FlowLayout());
        setLayout(null);
        setVisible(true);

    }

    public static void addCourse(String id, String name , String length, String semester) throws Exception{
        try {
            searchCourse(id);
            if (coursesValArr.size() > 0) {
                System.out.println("can't add existing course");
            } else {
                PreparedStatement posted = coursesConn.prepareStatement("INSERT INTO COURSES_TABLE (ID, LENGTH, SEMESTER ,NAME) VALUES (?,?,?,?)");
                posted.setString(1, id);
                posted.setString(2, length);
                posted.setString(3, semester);
                posted.setString(4, name);
                posted.executeLargeUpdate();
                System.out.println("course added");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void searchCourse(String id) throws Exception {
        try {
            PreparedStatement searched = coursesConn.prepareStatement("SELECT * FROM COURSES_TABLE WHERE ID=?");
            searched.setString(1,id);
            ResultSet result = searched.executeQuery();
            makeCourseArr(result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void deleteCourse(String id) throws Exception {
        PreparedStatement searched = coursesConn.prepareStatement("SELECT * FROM COLLAGE_TABLE WHERE COURSE_ID=?");
        searched.setString(1,id);
        ResultSet result = searched.executeQuery();
        makeCourseArr(result);
        if (coursesValArr.size() == 0){
            PreparedStatement deleted = coursesConn.prepareStatement("DELETE  FROM COURSES_TABLE WHERE ID=?");
            deleted.setString(1,id);
            long t = deleted.executeLargeUpdate();
            if (t>0)
                System.out.println("row deleted");
            else
                System.out.println("row didn't found");
        }else
            System.out.println("can't delete course which has class attached");
        //need to delete first from Collage table*/
    }

    private static void makeCourseArr(ResultSet r){
        try {
            coursesValArr = new ArrayList<>();
            while (r.next()){
                coursesValArr.add(r.getString("ID"));
                coursesValArr.add(r.getString("NAME"));
                coursesValArr.add(r.getString("LENGTH"));
                coursesValArr.add(r.getString("SEMESTER"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCourse(String id, String name , String length, String semester) throws Exception {
        PreparedStatement searched = coursesConn.prepareStatement("SELECT * FROM COLLAGE_TABLE WHERE COURSE_ID=?");
        searched.setString(1,id);
        ResultSet result = searched.executeQuery();
        makeCourseArr(result);
        if(coursesValArr.size() == 0) {
            PreparedStatement updated = coursesConn.prepareStatement("UPDATE COURSES_TABLE SET NAME=?, LENGTH=?, SEMESTER=? WHERE ID=?");
            updated.setString(1,name);
            updated.setString(2,length);
            updated.setString(3,semester);
            updated.setString(4,id);
            long t = updated.executeLargeUpdate();
            if (t>0)
                System.out.println("row updated");
            else
                System.out.println("row didn't found");
        } else
            System.out.println("can't update lecturer which have course attached");
        //need to update collage table first*/

    }

    public  void clear(){
        id.setText("");name.setText("");length.setText("");semester.setText("");
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            String n = name.getText(), l = length.getText(), s = semester.getText(), i = id.getText();
            switch (command) {
                case "add":
                    try {
                        if(validation(i) && validation(n) && s.length()==1 && validateLength(l)) {
                            addCourse(i, n, l, s);
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
                        if (validation(i) && validation(n) && validation(s) && validateLength(l))
                            updateCourse(i,n,l,s);
                        else
                            System.out.println("please fix inputs");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    clear();
                    break;
            }
        }
    }
    private boolean validation(String n){
        return n.length() > 0 && n.length() < 15;
    }
    private boolean validateLength(String l){
        String hours,minutes;
        int h,m;
        if(l.length() == 5 && l.charAt(2) == ':') {
            hours = l.substring(0, 2);
            h = Integer.parseInt(hours);
            minutes = l.substring(3, 5);
            m = Integer.parseInt(minutes);
            return !(h == 0 && h == m) && h>=0 && h <= 12 && h >= 0 && m < 60 && m >= 0;
        } else
            return false;
    }
}