package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class JDBCExecutor {

    public static void main(String... args) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", 5555,
                "thebookcooper", "BCdev", "password");

        try {
            Connection connection = dcm.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");
            if (resultSet.next()) {
                System.out.println("Number of users: " + resultSet.getInt(1));
            }
            UserDAO userDAO = new UserDAO(connection);
            User user = userDAO.findById(1);
            System.out.println(user.toString());
            
            User newUser = new User();
            newUser.setUserName("test4");
            newUser.setPassword("password321");
            newUser.setEmail("test4@email.com");
            newUser.setBBucksBalance(100.00);
            newUser.setCreationDate(new Timestamp(System.currentTimeMillis()));
            newUser.setLastLogin(new Timestamp(System.currentTimeMillis()));

            newUser = userDAO.create(newUser);
            System.out.println("Created new user: " + newUser.toString());
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}