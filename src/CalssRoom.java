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
    private ArrayList<String> valArr = new ArrayList<>();
    private static Connection con;

    public ClassRoom(Connection conn) throws HeadlessException {
        con = conn;
        number = new JTextField("class numbr",20);
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
    public  void clear(){
        number.setText("");building.setText("");floor.setText("");
    }

    public static void addClass(String b, String n, String f) throws Exception{
        try {
            PreparedStatement posted = con.prepareStatement("INSERT INTO CLASS_TABLE (BUILDING, CLASS, FLOOR) VALUES ('"+ b +"', '"+ n +"', '"+ f +"')");
            posted.executeLargeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<String>searchClass(String n) throws Exception {
        try {
            PreparedStatement searched = con.prepareStatement("SELECT * FROM CLASS_TABLE WHERE CLASS="+ n);
            ResultSet result = searched.executeQuery();
            ArrayList<String> array = new ArrayList<String>();
            while (result.next()){
                System.out.println(result.getString("CLASS"));
                System.out.println(result.getString("FLOOR"));
                System.out.println(result.getString("BUILDING"));

                array.add(result.getString("ClASS"));
                array.add(result.getString("FLOOR"));
                array.add(result.getString("BUILDING"));
            }
            System.out.println("all records selected");
            return array;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static void deleteClass(String n) throws Exception {
        PreparedStatement deleted = con.prepareStatement("DELETE  FROM CLASS_TABLE WHERE CLASS="+ n);
        deleted.executeLargeUpdate();
    }

    public static void updateClass(String b, String n , String f) throws Exception {
        String var1 = n;
        String var2 = b;
        String var3 = f;
        PreparedStatement updated = con.prepareStatement("UPDATE CLASS_TABLE SET FLOOR="+var3+",BUILDING="+var2+" WHERE CLASS="+var1);
        updated.executeLargeUpdate();
    }
    public class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if( command.equals( "add"))  {
                try {
                    addClass(building.getText(),number.getText(),floor.getText());
                    clear();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            else if( command.equals( "search" ) )  {
                try {
                    valArr = searchClass(number.getText());

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                number.setText(valArr.get(0));building.setText(valArr.get(1));floor.setText(valArr.get(2));
            }
            else  if (command.equals("delete")){
                try {
                    deleteClass(number.getText());
                    clear();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            else if (command.equals("update")){
                try {
                    updateClass(building.getText(),number.getText(),floor.getText());
                    clear();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}
