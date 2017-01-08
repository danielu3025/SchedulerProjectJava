import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by danielluzgarten on 08/01/2017.
 */
public class Lecturer  extends JFrame{
    JTextField number;
    JTextField building;
    JTextField floor;
    JButton addb,search,delte ,updateb;
    ArrayList<String> vals = new ArrayList<String>();
    public  static Connection conn;

    public Lecturer() throws HeadlessException {
        number = new JTextField("class numbr",20);
        building = new JTextField("class building",20);
        floor = new JTextField("class floor",20);
        addb = new JButton("add");
        search = new JButton("search");
        delte = new JButton("delete");
        updateb = new JButton("update");

        addb.setActionCommand("add");
        search.setActionCommand("search");
        delte.setActionCommand("delete");
        updateb.setActionCommand("update");

        addb.addActionListener(new ClassRoom.ButtonClickListener());
        search.addActionListener(new ClassRoom.ButtonClickListener());
        delte.addActionListener(new ClassRoom.ButtonClickListener());
        updateb.addActionListener(new ClassRoom.ButtonClickListener());



        setSize(500,500);
        setTitle("Class");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        number.setBounds(30,10,100,20);
        building.setBounds(30,40,100,20);
        floor.setBounds(30,70,100,20);
        addb.setBounds(30,100,100,20);
        search.setBounds(160,100,100,20);
        delte.setBounds(280,100,100,20);
        updateb.setBounds(30,130,100,20);


        add(number);add(building);add(floor);add(addb);add(search);add(delte);add(updateb);
        setLayout(new BorderLayout());
        setResizable(false);
        setLayout(new FlowLayout());
        setLayout(null);
        setVisible(true);

    }
    public  void clear(){
        number.setText("");building.setText("");floor.setText("");
    }
    public static Connection getConnection() throws Exception {
        try {
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:8889/schedulerDB";
            String userName = "root";
            String password = "root";
            Class.forName(driver);

            conn = DriverManager.getConnection(url, userName, password);
            System.out.println("connected secseed");
            return conn;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    public static void postclass(String b, String n , String f) throws Exception{
        String var1 = n;
        String var2 = b;
        String var3 = f;
        try {
            Connection con = getConnection();
            PreparedStatement posted = con.prepareStatement("INSERT INTO CLASS_TABLE (BUILDING, CLASS, FLOOR) VALUES ('"+var2+"', '"+var1+"', '"+var3+"')");
            posted.executeLargeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<String>searchClass(String n) throws Exception {
        String var1 = n;
        try {
            Connection con = getConnection();
            PreparedStatement searched = con.prepareStatement("SELECT * FROM CLASS_TABLE WHERE CLASS="+var1);
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
        String var1 = n;
        Connection con = getConnection();
        PreparedStatement deleted = con.prepareStatement("DELETE  FROM CLASS_TABLE WHERE CLASS="+var1);
        deleted.executeLargeUpdate();
    }

    public static void updateClass(String b, String n , String f) throws Exception {
        String var1 = n;
        String var2 = b;
        String var3 = f;
        Connection con = getConnection();
        PreparedStatement updated = con.prepareStatement("UPDATE CLASS_TABLE SET FLOOR="+var3+",BUILDING="+var2+" WHERE CLASS="+var1);
        updated.executeLargeUpdate();
    }
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if( command.equals( "add"))  {
                try {
                    postclass(building.getText(),number.getText(),floor.getText());
                    clear();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            else if( command.equals( "search" ) )  {
                try {
                    vals = searchClass(number.getText());

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                number.setText(vals.get(0));building.setText(vals.get(1));floor.setText(vals.get(2));
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
