package dao;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import entities.*;
import entities.Nameable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            System.err.print(e.getMessage());
        }
        return entities;
    }
    public <T extends Entity> T getById(Class<T> type, int id) {
        T entity = null;
        try {
            String s = "SELECT * FROM " + type.getSimpleName().toUpperCase()
                    + "S WHERE Id = " + id;
            ResultSet results = c.createStatement().executeQuery("SELECT * FROM " + type.getSimpleName().toUpperCase()
                    + "S WHERE Id = " + id);
            if (results.next())
                entity = (T)type.newInstance().fromResultSet(results);
        }
        catch (Exception e){
            System.err.print(e.getMessage());
        }
        return entity;
    }
    public <T extends Entity & Nameable> T getByName(Class<T> type, String name) {
        String dbName;
        if (type.equals(User.class))
            dbName = "UserInfo.NickName";
        else if (type.equals(Game.class))
            dbName = "Title";
        else
            dbName = "Name";

        T entity = null;
        try {
            ResultSet results = c.createStatement().executeQuery("SELECT r.* FROM " + type.getSimpleName().toUpperCase()
                    + "S r WHERE r." + dbName + " = '"  + name + "'");
            if (results.next())
                entity = (T)type.newInstance().fromResultSet(results);
        }
        catch (Exception e){
            System.err.print(e.getMessage());
        }
        return entity;
    }

    private String prepareInsertStatement(Entity e) {
        String dateFormat = "YYYY-MM-DD";
        DateFormat df = new SimpleDateFormat(dateFormat);
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(e.getClass().getSimpleName().toUpperCase()).append("S ( Id");
        List<String> values = new ArrayList<String>();

        Field[] fields = e.getClass().getDeclaredFields();
        try {
            for (int i = 1; i < fields.length; i++) {
                sb.append(", ").append(fields[i].getName());
                fields[i].setAccessible(true);
                if (fields[i].getType().equals(String.class))
                    values.add("'"+fields[i].get(e).toString()+"'");
                else if (fields[i].getType().equals(Date.class))
                    values.add("TO_DATE('"+df.format(fields[i].get(e))+"', '"+dateFormat+"'");
                else
                    values.add(fields[i].get(e).toString());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        sb.append(" ) VALUES ( 0");
        for (String v : values)
            sb.append(", ").append(v);
        sb.append(" )");

        return sb.toString();
    }
    private String prepareUpdateStatement(Entity e) {
        String dateFormat = "YYYY-MM-DD";
        DateFormat df = new SimpleDateFormat(dateFormat);
        StringBuilder sb = new StringBuilder("UPDATE ");
        Field[] fields = e.getClass().getDeclaredFields();
        fields[0].setAccessible(true);
        fields[1].setAccessible(true);
        sb.append(e.getClass().getSimpleName().toUpperCase()).append("S SET ");
        try {
            sb.append(fields[1].getName()).append(" = ");
            if (fields[1].getType().equals(String.class))
                sb.append("'"+fields[1].get(e).toString()+"'");
            else if (fields[1].getType().equals(Date.class))
                sb.append("TO_DATE('"+df.format(fields[1].get(e))+"', '"+dateFormat+"'");
            else
                sb.append(fields[1].get(e).toString());

            for (int i = 2; i < fields.length; i++) {
                sb.append(", ").append(fields[i].getName()).append(" = ");
                fields[i].setAccessible(true);
                if (fields[i].getType().equals(String.class))
                    sb.append("'"+fields[i].get(e).toString()+"'");
                else if (fields[i].getType().equals(Date.class))
                    sb.append("TO_DATE('"+df.format(fields[i].get(e))+"', '"+dateFormat+"'");
                else
                    sb.append(fields[i].get(e).toString());
            }
            sb.append(" WHERE Id = ").append(fields[0].get(e));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return sb.toString();
    }

    public void merge(Entity e){
        Entity search = getById(e.getClass(), e.getId());
        String sql;
        if (search == null)
            sql = prepareInsertStatement(e);
        else
            sql = prepareUpdateStatement(e);

        try {
            c.createStatement().executeUpdate(sql);
        }
        catch (Exception ex) {
            System.err.print(ex.getMessage());
        }

    }
    public void delete(Entity e) {
        try {
            c.createStatement().executeUpdate("DELETE FROM "+e.getClass().getSimpleName() + "S WHERE Id = "+e.getId());
        }
        catch (Exception ex) {
            System.err.print(ex.getMessage());
        }
    }
    public ResultSet execute(String query){
        ResultSet rs = null;
        try {
            rs = c.createStatement().executeQuery(query);
        }
        catch(Exception e) {
            System.err.print(e.getMessage());
        }
        return rs;
    }


}
