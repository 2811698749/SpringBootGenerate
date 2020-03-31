package GeneritorUtil;
import lombok.*;

import java.sql.*;
import java.util.ArrayList;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionPool {
    private String url;
    private String userName;
    private String password;
    private ArrayList<String> tableNames = new ArrayList<>(16);
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public ConnectionPool (String url ,String userName,String password){
            this.url = url;
            this.userName = userName;
            this.password = password;
    }
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url,userName,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public ResultSet getResultSet(String tableName){
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("select  * from "+tableName);
            rs = preparedStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
}
