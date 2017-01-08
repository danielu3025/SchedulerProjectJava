import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;

class ClassRoom extends JFrame {
    private JTextField number;
    private JTextField building;
    private JTextField floor;
    private static Connection con;
    private static ArrayList<String> array = new ArrayList<>();


    public ClassRoom(Connection conn) throws HeadlessException {
        con = conn;
        number = new JTextField("class number",20);
        building = new JTextField("class building",20);
        floor = new JTextField("class floor",20);


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
        number.setBounds(30,10,100,20);
        building.setBounds(30,40,100,20);
        floor.setBounds(30,70,100,20);
        addB.setBounds(30,100,100,20);
        search.setBounds(160,100,100,20);
        delete.setBounds(280,100,100,20);
        updateB.setBounds(30,130,100,20);


        add(number);add(building);add(floor);add(addB);add(search);add(delete);add(updateB);
        setLayout(new BorderLayout());
        setResizable(false);
        setLayout(new FlowLayout());
        setLayout(null);
        setVisible(true);

    }
    private void clear(){
        number.setText("");building.setText("");floor.setText("");
    }

    private static void addClass(String b, String n, String f) throws Exception{
        try {
            PreparedStatement posted = con.prepareStatement("INSERT INTO CLASS_TABLE (BUILDING, CLASS, FLOOR) VALUES ('"+ b +"', '"+ n +"', '"+ f +"')");
            posted.executeLargeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void searchClass(String n) throws Exception {
        try {
            PreparedStatement searched = con.prepareStatement("SELECT * FROM CLASS_TABLE WHERE CLASS="+ n);
            ResultSet result = searched.executeQuery();
            makeClassArr(result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void makeClassArr(ResultSet r){
        try {
            array = new ArrayList<>();
            while (r.next()){
                System.out.println(r.getString("CLASS"));
                System.out.println(r.getString("FLOOR"));
                System.out.println(r.getString("BUILDING"));

                array.add(r.getString("ClASS"));
                array.add(r.getString("FLOOR"));
                array.add(r.getString("BUILDING"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void deleteClass(String n) throws Exception {
        PreparedStatement searched = con.prepareStatement("SELECT * FROM COLLAGE_TABLE WHERE CLASS="+ n);
        ResultSet result = searched.executeQuery();
        makeClassArr(result);
        if(array.size() == 0) {
            PreparedStatement deleted = con.prepareStatement("DELETE  FROM CLASS_TABLE WHERE CLASS=" + n);
            long t = deleted.executeLargeUpdate();
            if (t>0)
                System.out.println("row deleted");
             else
                 System.out.println("row didn't found");
        }else
            System.out.println("can't delete class which have course attached");
            //need to delete first from Collage table
    }

    private static void updateClass(String b, String n, String f) throws Exception {
        PreparedStatement searched = con.prepareStatement("SELECT * FROM COLLAGE_TABLE WHERE CLASS="+ n);
        ResultSet result = searched.executeQuery();
        makeClassArr(result);
        if(array.size() == 0) {
            PreparedStatement updated = con.prepareStatement("UPDATE CLASS_TABLE SET FLOOR="+ f +",BUILDING="+ b +" WHERE CLASS="+ n);
            long t = updated.executeLargeUpdate();
            if (t>0)
                System.out.println("row updated");
             else
                System.out.println("row didn't found");
        } else
            System.out.println("can't update class which have course attached");
        //need to update collage table first
    }
    public class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            switch (command) {
                case "add":
                    try {
                        addClass(building.getText(), number.getText(), floor.getText());
                        clear();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "search":
                    try {
                        searchClass(number.getText());
                        if (array.size() != 0) {
                            number.setText(array.get(0));
                            building.setText(array.get(1));
                            floor.setText(array.get(2));
                            System.out.println("filled all records");
                        } else
                            System.out.println("no record");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "delete":
                    try {
                        deleteClass(number.getText());
                        clear();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case "update":
                    try {
                        updateClass(building.getText(), number.getText(), floor.getText());
                        clear();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
            }
        }
    }

}
