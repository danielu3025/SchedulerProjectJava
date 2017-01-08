import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Lecturer  extends JFrame{
    JTextField id;
    JTextField name;
    JTextField lastName;
    JTextField dob;
    JTextField address;
    JButton addb,search,delte ,updateb;
    ArrayList<String> vals = new ArrayList<>();
    public  static Connection conn;

    public Lecturer(Connection con) throws HeadlessException {
        conn = con;
        id = new JTextField("lecturer id",20);
        name = new JTextField("name",20);
        lastName = new JTextField("last name",20);
        dob = new JTextField("date of birth",20);
        address = new JTextField("address",20);
        addb = new JButton("add");
        search = new JButton("search");
        delte = new JButton("delete");
        updateb = new JButton("update");

        addb.setActionCommand("add");
        search.setActionCommand("search");
        delte.setActionCommand("delete");
        updateb.setActionCommand("update");

        addb.addActionListener(new ButtonClickListener());
        search.addActionListener(new ButtonClickListener());
        delte.addActionListener(new ButtonClickListener());
        updateb.addActionListener(new ButtonClickListener());



        setSize(500,500);
        setTitle("Class");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        id.setBounds(30,10,100,20);
        name.setBounds(30,40,100,20);
        lastName.setBounds(30,70,100,20);
        dob.setBounds(30,100,100,20);
        address.setBounds(30,130,100,20);
        addb.setBounds(160,130,100,20);
        search.setBounds(160,100,100,20);
        delte.setBounds(280,100,100,20);
        updateb.setBounds(280,130,100,20);


        add(id);add(name);add(lastName);add(dob);add(address);add(addb);add(search);add(delte);add(updateb);
        setLayout(new BorderLayout());
        setResizable(false);
        setLayout(new FlowLayout());
        setLayout(null);
        setVisible(true);

    }
    public  void clear(){
        id.setText("");name.setText("");lastName.setText("");dob.setText("");address.setText("");
    }
    public static void addLect(String id, String name , String lastName, String dob, String address) throws Exception{
        try {
            PreparedStatement posted = conn.prepareStatement("INSERT INTO LECTURERS_TABLE (ID, NAME, LAST_NAME, DOB, ADDRESS) VALUES ('"+ id +"', '"+ name +"', '"+ lastName +"', '"+ dob +"', '"+ address +"')");
            posted.executeLargeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<String>searchLect(String id) throws Exception {
        try {
            PreparedStatement searched = conn.prepareStatement("SELECT * FROM LECTURERS_TABLE WHERE ID="+id);
            ResultSet result = searched.executeQuery();
            ArrayList<String> array = new ArrayList<String>();
            while (result.next()){
                System.out.println(result.getString("ID"));
                System.out.println(result.getString("NAME"));
                System.out.println(result.getString("LAST_NAME"));
                System.out.println(result.getString("DOB"));
                System.out.println(result.getString("ADDRESS"));

                array.add(result.getString("ID"));
                array.add(result.getString("NAME"));
                array.add(result.getString("LAST_NAME"));
                array.add(result.getString("DOB"));
                array.add(result.getString("ADDRESS"));
            }
            System.out.println("all records selected");
            return array;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static void deleteLect(String id) throws Exception {
        PreparedStatement deleted = conn.prepareStatement("DELETE  FROM LECTURERS_TABLE WHERE ID="+id);
        deleted.executeLargeUpdate();
    }

    public static void updateLect(String id, String name , String lastName, String dob, String address) throws Exception {
        PreparedStatement updated = conn.prepareStatement("UPDATE LECTURERS_TABLE SET ID="+id+",NAME="+name+",LAST_NAME="+lastName+",DOB="+dob+",ADDRESS="+address+" WHERE ID="+id);
        updated.executeLargeUpdate();
    }
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if( command.equals( "add"))  {
                try {
                    addLect(id.getText(),name.getText(),lastName.getText(),dob.getText(),address.getText());
                    clear();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            else if( command.equals( "search" ) )  {
                try {
                    vals = searchLect(id.getText());

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                id.setText(vals.get(0));name.setText(vals.get(1));lastName.setText(vals.get(2));dob.setText(vals.get(3));address.setText(vals.get(4));
            }
            else  if (command.equals("delete")){
                try {
                    deleteLect(id.getText());
                    clear();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
            else if (command.equals("update")){
                try {
                    updateLect(id.getText(),name.getText(),lastName.getText(),dob.getText(),address.getText());
                    clear();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}