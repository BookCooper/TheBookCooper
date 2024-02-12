package jdbc;

import jdbc.util.DataTransferObject;
import java.sql.Timestamp;

public class User implements DataTransferObject {

    private long userId;
    private String userName;
    private String password;
    private String email;
    private double bBucksBalance;
    private Timestamp creationDate;
    private Timestamp lastLogin;


    @Override
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public double getBBucksBalance() {
        return bBucksBalance;
    }

    public void setBBucksBalance(double bBucksBalance) {
        this.bBucksBalance = bBucksBalance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", bBucksBalance=" + bBucksBalance +
                ", creationDate=" + creationDate +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
