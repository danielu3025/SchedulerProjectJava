import java.sql.Connection;
import java.sql.DriverManager;

public class Main{
     private static Connection con ;
    public static void main(String[] args) throws Exception {
        connect();
        if(con != null) {
            ClassRoom g = new ClassRoom(con);
            //Lecturer l = new Lecturer(con);
            //Cours c = new Cours(con);
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
            System.out.println(e);
        }
    }
}
