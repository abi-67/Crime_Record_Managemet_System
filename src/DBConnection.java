import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    static final String URL = "jdbc:mysql://localhost:3306/CrimeRecordManagementSystem";
    static final String User = "root";
    static final String Password = "";

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(URL, User, Password);
        }catch (SQLException e){
            System.out.println("Connection Failed!");
            return null;
        }
    }
}
