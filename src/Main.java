import com.jcraft.jsch.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Main{

    static String username;
    static String password;

    static Connection getConnection(String username, String password){
        final int lPort = 2222;
        final int rPort = 1521;
        final String rHost = "localhost";

        final String host = "helios.cs.ifmo.ru";
        final int port = 2222;

        final String sid = "orbis";

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            int assinged_port = session.setPortForwardingL(lPort, rHost, rPort);
        }
        catch (JSchException e) {
            System.err.println("Could not make an ssh tunnel...");
            e.printStackTrace();
            return null;
        }
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC Driver not found...");
            e.printStackTrace();
            return null;
        }

        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@"+rHost+":"+lPort+":"+sid, username, password);
        } catch (SQLException e) {
            System.err.println("Connection to DB failed...");
            e.printStackTrace();
            return null;
        }

        if (connection == null) {
            System.err.println("Unknown error when creating a connection...");
        }

        return connection;
    }

    public static void main(String[] arg) {
        if (arg.length != 2) {
            System.err.println("2 arguments are necessary: username and password");
            return;
        }
        username = arg[0];
        password = arg[1];

        Connection c = getConnection(username, password);
        if (c == null){
            System.err.println("Could not estabilish a connection with DB...");
            return;
        }

        ResultSet users = null;
        try {
            users = c.createStatement().executeQuery("SELECT * from GAMES");
            while (users.next())
                System.out.println(users.getInt(1)+" " + users.getString(2));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }


    }
}