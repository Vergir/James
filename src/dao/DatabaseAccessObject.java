package dao;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import entities.*;
import entities.Nameable;
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

    public <T extends Entity> List<T> getAll(Class<T> type) {
        ResultSet results;
        List<T> entities = new ArrayList<T>();
        try {
            results = c.createStatement().executeQuery("SELECT * FROM " + type.getSimpleName().toUpperCase() + "S");
            while (results.next())
                entities.add((T)type.newInstance().fromResultSet(results));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return entities;
    }
    public <T extends Entity & Nameable> T getByName(Class<T> type, String name) {
        String dbName;
        if (type.equals(User.class))
            dbName = "UserInfo.NickName";
        else if (type.equals(Game.class))
            dbName = "Title";
        else
            dbName = "Name";

        T result = null;
        try {
            ResultSet results = c.createStatement().executeQuery("SELECT r.* FROM " + type.getSimpleName().toUpperCase()
                    + "S r WHERE r." + dbName + " = "  + name);
            result = (T)type.newInstance().fromResultSet(results);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public int merge(Entity e){
        throw new NotImplementedException();
    }

    public int merge(List<Entity> entities){
        throw new NotImplementedException();
    }

    public int delete(Entity e){
        throw new NotImplementedException();
    }
    public int delete(List<Entity> entities){
        throw new NotImplementedException();
    }

    public ResultSet execute(String query){
        throw new NotImplementedException();
    }
}
