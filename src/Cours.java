import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Cours  extends JFrame{
    JTextField id;
    JTextField name;
    JTextField length;
    JTextField semester;
    JTextField address;
    JButton addb,search,delte ,updateb;
    ArrayList<String> vals = new ArrayList<>();
    public  static Connection conn;

    public Cours(Connection con) {
        conn = con;
        id = new JTextField(" cours id",20);
        name = new JTextField("name",20);
        length = new JTextField("length",20);
        semester = new JTextField("semester",20);

        addb = new JButton("add");
        search = new JButton("search");
        delte = new JButton("delete");
        updateb = new JButton("update");

        addb.setActionCommand("add");
        search.setActionCommand("search");
        delte.setActionCommand("delete");
        updateb.setActionCommand("update");

        addb.addActionListener(new ButtonClickListenerr());
        search.addActionListener(new ButtonClickListenerr());
        delte.addActionListener(new ButtonClickListenerr());
        updateb.addActionListener(new ButtonClickListenerr());

        setSize(500,500);
        setTitle("Cours");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        id.setBounds(30,10,100,20);
        name.setBounds(30,40,100,20);
        length.setBounds(30,70,100,20);
        semester.setBounds(30,100,100,20);
        addb.setBounds(160,130,100,20);
        search.setBounds(160,100,100,20);
        delte.setBounds(280,100,100,20);
        updateb.setBounds(280,130,100,20);

        add(id);add(name);add(length);add(semester);add(addb);add(search);add(delte);add(updateb);
        setLayout(new BorderLayout());
        setResizable(false);
        setLayout(new FlowLayout());
        setLayout(null);
        setVisible(true);

    }

    public static void addCourse(String id, String name , String length, String semester) throws Exception{
        try {
            PreparedStatement posted = conn.prepareStatement("INSERT INTO COURSES_TABLE (ID, LENGTH, SEMSTER ,NAME) VALUES ('"+ id +"', '"+ length +"', '"+ semester +"', '"+ name +"')");
            posted.executeLargeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String>searchCourse(String id) throws Exception {
        try {
            PreparedStatement searched = conn.prepareStatement("SELECT * FROM COURSES_TABLE WHERE ID="+id);
            ResultSet result = searched.executeQuery();
            ArrayList<String> array = new ArrayList<String>();
            while (result.next()){
                array.add(result.getString("ID"));
                array.add(result.getString("NAME"));
                array.add(result.getString("SEMSTER"));
                array.add(result.getString("LENGTH"));
            }
            System.out.println("all records selected");
            return array;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteCours(String id) throws Exception {
        PreparedStatement deleted = conn.prepareStatement("DELETE  FROM COURSES_TABLE WHERE ID="+id);
        deleted.executeLargeUpdate();
    }


    public static void updateCours(String id, String name , String length, String semester) throws Exception {
        PreparedStatement updated = conn.prepareStatement("UPDATE COURSES_TABLE SET NAME="+name+",LENGTH="+length+",SEMSTER="+semester+" WHERE ID="+id);
        updated.executeLargeUpdate();
    }

    public  void clear(){
        id.setText("");name.setText("");length.setText("");semester.setText("");
    }

    private class ButtonClickListenerr implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("add")) {
                try {
                    addCourse(id.getText(),name.getText(),length.getText(),semester.getText());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                clear();
            }
            else if (command.equals("search")) {
                try {
                    vals = searchCourse(id.getText());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                id.setText(vals.get(0));name.setText(vals.get(1));length.setText(vals.get(2));semester.setText(vals.get(3));
            }
            else if (command.equals("delete")) {
                try {
                    deleteCours(id.getText());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                clear();
            }
            else if (command.equals("update")) {
                try {
                    updateCours(id.getText(),name.getName(),length.getText(),semester.getText());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                clear();
            }
        }
    }
}