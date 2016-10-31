package dao;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import entities.Developer;
import entities.Entity;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class DatabaseAccessObject {
    private static DatabaseAccessObject instance;
    private static Connection c;


    private DatabaseAccessObject(){
        if (instance != null)
            throw new ExceptionInInitializerError("Tried to initialize singleton");
    }


    private static void init(String username, String password){
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
        }
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC Driver not found...");
            e.printStackTrace();
        }

        try {
            c = DriverManager.getConnection("jdbc:oracle:thin:@"+rHost+":"+lPort+":"+sid, username, password);
        } catch (SQLException e) {
            System.err.println("Connection to DB failed...");
            e.printStackTrace();
        }

        if (c == null) {
            System.err.println("Unknown error when creating a connection...");
        }
    }

    public static DatabaseAccessObject getInstance(String username, String password) {
        if (username == null || password == null)
            throw new NullPointerException("username/password combination is invalid");
        init(username, password);
        if (instance == null)
            instance = new DatabaseAccessObject();
        return instance;
    }

    public List<String> GetGames(){
        ResultSet tmp = null;
        List<String> result = new ArrayList<String>();
        try {
            tmp = c.createStatement().executeQuery("SELECT * FROM GAMES");
            while (tmp.next())
                result.add(tmp.getString("Title"));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<Developer> getAllDevelopers(){
        ResultSet results = null;
        List<Developer> devs = new ArrayList<>();
        try {
            results = c.createStatement().executeQuery("SELECT * FROM DEVELOPERS");
            while (results.next()) {
                devs.add(new Developer(results.getInt(1), results.getString(2), results.getString(3), results.getString(4)));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return devs;
    }

    public <T extends Entity> List<T> getAll(Class<T> type) {

        if (type.equals(Developer.class))
            return (List<T>)getAllDevelopers();
        throw new NotImplementedException();
    }
}
