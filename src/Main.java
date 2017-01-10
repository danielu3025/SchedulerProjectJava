import java.sql.*;

public class Main{
     private static Connection con ;
    public static void main(String[] args) throws Exception {
        connect();
        triggersCreator();
        if(con != null) {
            //ClassRoom g = new ClassRoom(con);
            Lecturer l = new Lecturer(con);
            //Courses c = new Courses(con);
            //Menu m = new Menu();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    triggerDist();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }
    }
    private static void connect() throws Exception {
        try {
            String driver = "com.mysql.jdbc.Driver";
            //String url = "jdbc:mysql://localhost:8889/schedulerDB";
            String url = "jdbc:mysql://localhost:3306/schedulerDB";
            String userName = "root";
            String password = "root";
            Class.forName(driver);

            con = DriverManager.getConnection(url, userName, password);
            System.out.println("connected succeed");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void triggersCreator() throws Exception{
        try {
            PreparedStatement trigger = con.prepareStatement("CREATE TRIGGER PHONES_DELETE BEFORE DELETE ON LECTURERS_TABLE FOR EACH ROW BEGIN DELETE FROM PHONE_TABLE WHERE ID = old.ID;END ");
            trigger.executeLargeUpdate();
            PreparedStatement trigger2 = con.prepareStatement("CREATE TRIGGER CLASS_DELETE BEFORE DELETE ON CLASS_TABLE FOR EACH ROW BEGIN DELETE FROM COLLAGE_TABLE WHERE CLASS = old.CLASS;END ");
            trigger2.executeLargeUpdate();
            PreparedStatement trigger3 = con.prepareStatement("CREATE TRIGGER COURSE_DELETE BEFORE DELETE ON COURSES_TABLE FOR EACH ROW BEGIN DELETE FROM COLLAGE_TABLE WHERE COURSE_ID = old.ID;END ");
            trigger3.executeLargeUpdate();
            PreparedStatement trigger4 = con.prepareStatement("CREATE TRIGGER LECT_DELETE BEFORE DELETE ON LECTURERS_TABLE FOR EACH ROW BEGIN DELETE FROM COLLAGE_TABLE WHERE LECTURER_ID = old.ID;END ");
            trigger4.executeLargeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void triggerDist() throws Exception {
        try {
            PreparedStatement trigger = con.prepareStatement("DROP TRIGGER PHONES_DELETE;");
            trigger.executeLargeUpdate();
            PreparedStatement trigger1 = con.prepareStatement("DROP TRIGGER CLASS_DELETE;");
            trigger1.executeLargeUpdate();
            PreparedStatement trigger2 = con.prepareStatement("DROP TRIGGER COURSE_DELETE;");
            trigger2.executeLargeUpdate();
            PreparedStatement trigger3 = con.prepareStatement("DROP TRIGGER LECT_DELETE;");
            trigger3.executeLargeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
