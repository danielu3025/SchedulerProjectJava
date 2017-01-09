import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Lecturer  extends JFrame{
    private JTextField id;
    private JTextField name;
    private JTextField lastName;
    private JTextField dob;
    private JTextField address;
    private static ArrayList<String> lValArr = new ArrayList<>();
    private static Connection lectConn;

    public Lecturer(Connection con) throws HeadlessException {
        lectConn = con;
        id = new JTextField("lecturer id",20);
        name = new JTextField("name",20);
        lastName = new JTextField("last name",20);
        dob = new JTextField("date of birth",20);
        address = new JTextField("address",20);
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
        id.setBounds(30,10,100,20);
        name.setBounds(30,40,100,20);
        lastName.setBounds(30,70,100,20);
        dob.setBounds(30,100,100,20);
        address.setBounds(30,130,100,20);
        addB.setBounds(160,130,100,20);
        search.setBounds(160,100,100,20);
        delete.setBounds(280,100,100,20);
        updateB.setBounds(280,130,100,20);


        add(id);add(name);add(lastName);add(dob);add(address);add(addB);add(search);add(delete);add(updateB);
        setLayout(new BorderLayout());
        setResizable(false);
        setLayout(new FlowLayout());
        setLayout(null);
        setVisible(true);

    }
    private void clear(){
        id.setText("");name.setText("");lastName.setText("");dob.setText("");address.setText("");
    }
    public static void addLect(String id, String name, String lastName, String dob, String address) throws Exception{
        try {
            searchLect(id);
            if (lValArr.size()>0){
                System.out.println("can't add existing lecturer");
            } else {
                PreparedStatement posted = lectConn.prepareStatement("INSERT INTO LECTURERS_TABLE (ID, NAME, LAST_NAME, DOB, ADDRESS) VALUES ('" + id + "', '" + name + "', '" + lastName + "', '" + dob + "', '" + address + "')");
                posted.executeLargeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void searchLect(String id) throws Exception {
        try {
            PreparedStatement searched = lectConn.prepareStatement("SELECT * FROM LECTURERS_TABLE WHERE ID="+id);
            ResultSet result = searched.executeQuery();
            makeLectArr(result);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void deleteLect(String id) throws Exception {
        PreparedStatement searched = lectConn.prepareStatement("SELECT * FROM COLLAGE_TABLE WHERE LECTURER_ID="+ id);
        ResultSet result = searched.executeQuery();
        makeLectArr(result);
        if (lValArr.size() == 0){
            PreparedStatement deleted = lectConn.prepareStatement("DELETE  FROM LECTURERS_TABLE WHERE ID="+id);
            long t = deleted.executeLargeUpdate();
            if (t>0)
                System.out.println("row deleted");
            else
                System.out.println("row didn't found");
        }else
            System.out.println("can't delete lecturer which has course attached");
            //need to delete first from Collage table*/
    }
    private static void makeLectArr(ResultSet r){
        try {
            lValArr = new ArrayList<>();
            while (r.next()){
                System.out.println(r.getString("ID"));
                System.out.println(r.getString("NAME"));
                System.out.println(r.getString("LAST_NAME"));
                System.out.println(r.getString("DOB"));
                System.out.println(r.getString("ADDRESS"));

                lValArr.add(r.getString("ID"));
                lValArr.add(r.getString("NAME"));
                lValArr.add(r.getString("LAST_NAME"));
                lValArr.add(r.getString("DOB"));
                lValArr.add(r.getString("ADDRESS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateLect(String id, String name , String lastName, String dob, String address) throws Exception {
        PreparedStatement searched = lectConn.prepareStatement("SELECT * FROM COLLAGE_TABLE WHERE LECTURER_ID="+ id);
        ResultSet result = searched.executeQuery();
        makeLectArr(result);
        if(lValArr.size() == 0) {
            PreparedStatement updated = lectConn.prepareStatement("UPDATE LECTURERS_TABLE SET NAME=?,LAST_NAME=?,DOB=?,ADDRESS=? WHERE ID=?");
            updated.setString(1,name);
            updated.setString(2,lastName);
            updated.setString(3,dob);
            updated.setString(4,address);
            updated.setString(5,id);
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
                        addLect(id.getText(), name.getText(), lastName.getText(), dob.getText(), address.getText());
                        clear();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "search":
                    try {
                        searchLect(id.getText());
                        if(lValArr.size() != 0) {
                            id.setText(lValArr.get(0));
                            name.setText(lValArr.get(1));
                            lastName.setText(lValArr.get(2));
                            dob.setText(lValArr.get(3));
                            address.setText(lValArr.get(4));
                            System.out.println("filled all records");
                        } else
                            System.out.println("no record");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "delete":
                    try {
                        deleteLect(id.getText());
                        clear();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    break;
                case "update":
                    try {
                        updateLect(id.getText(), name.getText(), lastName.getText(), dob.getText(), address.getText());
                        clear();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
            }
        }
    }
}