import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by danielluzgarten on 09/01/2017.
 */
public class Collage extends JFrame {
    private JTextField lID;
    private JTextField cID;
    private JTextField clNum;
    private JTextField day;
    private JTextField begine;
    private static ArrayList<String> valArr = new ArrayList<>();
    private static Connection conn;

    public Collage(Connection con) throws HeadlessException {
        conn = con;
        lID = new JTextField("lecturerc ID",20);
        cID = new JTextField("courseID",20);
        clNum = new JTextField("calss num",20);
        day = new JTextField("dat",3);
        begine = new JTextField("begine",3);
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
        setTitle("Class");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        lID.setBounds(30,10,100,20);
        cID.setBounds(30,40,100,20);
        clNum.setBounds(30,70,100,20);
        day.setBounds(30,100,100,20);
        begine.setBounds(30,130,100,20);
        addB.setBounds(160,130,100,20);
        search.setBounds(160,100,100,20);
        delete.setBounds(280,100,100,20);
        updateB.setBounds(280,130,100,20);


        add(lID);add(cID);add(clNum);add(day);add(begine);add(addB);add(search);add(delete);add(updateB);
        setLayout(new BorderLayout());
        setResizable(false);
        setLayout(new FlowLayout());
        setLayout(null);
        setVisible(true);

    }
    private void clear(){
        lID.setText("");
        cID.setText("");
        clNum.setText("");
        day.setText("");
        begine.setText("");
    }
    public static void addLect(String lid, String cid, String classnum, String day,String beginning) throws Exception{
        try {
            PreparedStatement posted = conn.prepareStatement("INSERT INTO COLLAGE_TABLE (LECTURER_ID, COURSE_ID, CLASS, DAY, BEGINNING) VALUES (?,?,?,?,?)");
            posted.setString(1,lid);posted.setString(2,cid);posted.setString(3,classnum);posted.setString(4,day);posted.setString(5,beginning);
            //data validation
            posted.executeLargeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void searchLect(String lid, String cid, String classnum, String day,String beginning) throws Exception {
        try {
            PreparedStatement searched = conn.prepareStatement("SELECT * FROM COLLAGE_TABLE WHERE (LECTURER_ID = ? AND COURSE_ID= ? AND CLASS=? AND DAY= ?  AND BEGINNING= ?)");
            searched.setString(1,lid);searched.setString(2,cid);searched.setString(3,classnum);searched.setString(4,day);searched.setString(5,beginning);
            ResultSet result = searched.executeQuery();
            makeLectArr(result);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void deleteLect(String lid, String cid, String classnum, String day,String beginning) throws Exception {
        PreparedStatement delelted = conn.prepareStatement("DELETE FROM COLLAGE_TABLE WHERE LECTURER_ID = ? AND COURSE_ID= ? AND CLASS=? AND DAY= ?  AND BEGINNING= ?");
        //delete from  all tables before;
        delelted.setString(1,lid);delelted.setString(2,cid);delelted.setString(3,classnum);delelted.setString(4,day);delelted.setString(5,beginning);
        delelted.executeLargeUpdate();
    }
    private static void makeLectArr(ResultSet r){
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
    public static void updateLect(String lid, String cid, String classnum, String day,String beginning) throws Exception {
        PreparedStatement searched = conn.prepareStatement("SELECT * FROM COLLAGE_TABLE WHERE LECTURER_ID = ? AND COURSE_ID= ? AND CLASS=? AND DAY= ? AND BEGINNING=?");
        searched.setString(1,lid);searched.setString(2,cid);searched.setString(3,classnum);searched.setString(4,day);searched.setString(5,beginning);
        ResultSet result = searched.executeQuery();
        makeLectArr(result);
        makeLectArr(result);
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
            switch (command) {
                case "add":
                    try {
                        addLect(lID.getText(), cID.getText(), clNum.getText(), day.getText(), begine.getText());
                        clear();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "search":
                    try {
                        searchLect(lID.getText(),cID.getText(), clNum.getText(), day.getText(), begine.getText());
                        if(valArr.size() != 0) {
                            lID.setText(valArr.get(0));
                            cID.setText(valArr.get(1));
                            clNum.setText(valArr.get(2));
                            day.setText(valArr.get(3));
                            begine.setText(valArr.get(4));
                            System.out.println("filled all records");
                        } else
                            System.out.println("no record");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "delete":
                    try {
                        deleteLect(lID.getText(), cID.getText(), clNum.getText(), day.getText(), begine.getText());
                        clear();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    break;
                case "update":
                    try {
                        updateLect(lID.getText(), cID.getText(), clNum.getText(), day.getText(), begine.getText());
                        clear();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
            }
        }
    }
}