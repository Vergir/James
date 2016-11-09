package dao;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import dbobjects.DbObject;
import dbobjects.entities.*;
import dbobjects.linkers.Linker;
import oracle.jdbc.internal.OracleTypes;

import java.lang.reflect.Field;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public final class DatabaseAccessObject {
    private static DatabaseAccessObject instance;
    private static Connection c;

    private DatabaseAccessObject() {
        if (instance != null)
            throw new ExceptionInInitializerError("Tried to initialize singleton");
    }

    public static DatabaseAccessObject getInstance(String username, String password) {
        if (username == null || password == null)
            throw new NullPointerException("username/password combination is invalid");
        init(username, password);
        if (instance == null)
            instance = new DatabaseAccessObject();
        return instance;
    }


    //C*U*
    public Integer merge(DbObject object) {
        if(object == null)
            return -1;
        if (object.getClass().equals(User.class)) {
            return mergeUser((User) object);
        }

        if (object instanceof Entity) {
            return mergeEntity((Entity) object);
        } else if (object instanceof Linker) {
            mergeLinker((Linker) object);
            return null;
        }
        return null;
    }

    //*R**
    public <T extends DbObject> Set<T> getAll(Class<T> returnType) {
        if (returnType.equals(User.class))
            return (Set<T>) getAllUsers();

        Set<T> entities = new HashSet<T>();
        String optionalS;
        if (Linker.class.isAssignableFrom(returnType))
            optionalS = "";
        else
            optionalS = "S";
        try {
            ResultSet results;
            results = c.createStatement().executeQuery("SELECT * FROM " + returnType.getSimpleName().toUpperCase() + optionalS);
            while (results.next())
                entities.add((T) returnType.newInstance().fromResultSet(results));
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
        return entities;
    }

    public <T extends Entity> T getEntity(Class<T> returnType, int id) {
        if (returnType.equals(User.class)) {
            for (User u : getAllUsers())
                if (u.getId() == id)
                    return (T) u;
            return null;
        }
        T entity = null;
        try {
            ResultSet results = c.createStatement().executeQuery("SELECT * FROM " + returnType.getSimpleName().toUpperCase()
                    + "S WHERE Id = " + id);
            if (results.next())
                entity = (T) returnType.newInstance().fromResultSet(results);
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
        return entity;
    }

    public <T extends Linker> T getLinker(Class<T> returnType, int id1, int id2) {
        T linker = null;
        String[] classNames = splitClasses(returnType.getSimpleName());
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(returnType.getSimpleName());
        sql.append(" WHERE ").append(classNames[0]).append("_Id = ").append(id1);
        sql.append(" AND ").append(classNames[1]).append("_Id = ").append(id2);
        try {
            ResultSet results = c.createStatement().executeQuery(sql.toString());
            if (results.next())
                linker = (T) returnType.newInstance().fromResultSet(results);
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
        return linker;
    }

    public <T extends Nameable> T getByName(Class<T> returnType, String name) {
        String dbName;
        if (returnType.equals(User.class))
            return (T) getUserByName(name);
        if (returnType.equals(Game.class))
            dbName = "Title";
        else
            dbName = "Name";

        T entity = null;
        try {
            ResultSet results = c.createStatement().executeQuery("SELECT r.* FROM " + returnType.getSimpleName().toUpperCase()
                    + "S r WHERE r." + dbName + " = '" + name + "'");
            if (results.next())
                entity = (T) returnType.newInstance().fromResultSet(results);
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
        return entity;
    }

    //***D
    public void delete(DbObject object) {
        if (object instanceof Entity) {
            deleteEntity((Entity) object);
        } else if (object instanceof Linker) {
            deleteLinker((Linker) object);
        }
    }

    //QOL-method
    public ResultSet executeSelect(String query) {
        ResultSet rs = null;
        try {
            rs = c.createStatement().executeQuery(query);
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
        return rs;
    }


    //PL/SQL-calling methods
    private Integer mergeUser(User u) {
        if (u.getId() != 0)
            return updateUser(u);
        else
            return addUser(u);
    }

    private Integer addUser(User u) {
        Integer result = null;

        try {
            CallableStatement addUser = c.prepareCall("begin ? := Crud.AddUser( ?, ?, ?, ? ); end;");
            addUser.registerOutParameter(1, Types.NUMERIC);
            addUser.setString(2, u.getName());
            addUser.setString(3, u.getFirstName());
            addUser.setString(4, u.getLastName());
            addUser.setString(5, u.getEmail());
            addUser.execute();
            result = addUser.getInt(1);
            addUser.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    private Integer updateUser(User u) {
        Integer result = null;

        try {
            CallableStatement updateUser = c.prepareCall("begin ? := Crud.UpdateUser( ?, ?, ?, ?, ?, ?); end;");
            updateUser.registerOutParameter(1, Types.NUMERIC);
            updateUser.setInt(2, u.getId());
            updateUser.setString(3, u.getName());
            updateUser.setString(4, u.getFirstName());
            updateUser.setString(5, u.getLastName());
            updateUser.setString(6, u.getEmail());
            updateUser.setInt(7, u.getBalance());
            updateUser.execute();
            result = updateUser.getInt(1);
            updateUser.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    private Set<User> getAllUsers() {
        Set<User> users = new HashSet<User>();
        try {
            CallableStatement readUsers = c.prepareCall("begin ? := Crud.ReadUser( ? ); end;");
            readUsers.registerOutParameter(1, OracleTypes.CURSOR);
            readUsers.setNull(2, Types.NULL);
            readUsers.execute();
            ResultSet rs = (ResultSet) readUsers.getObject(1);
            while (rs.next())
                users.add((User) new User().fromResultSet(rs));
            readUsers.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return users;

    }

    private User getUserByName(String nickName) {
        User u = null;

        try {
            CallableStatement readUser = c.prepareCall("begin ? := Crud.ReadUser( ? ); end;");
            readUser.registerOutParameter(1, OracleTypes.CURSOR);
            readUser.setString(2, nickName);
            readUser.execute();
            ResultSet rs = (ResultSet) readUser.getObject(1);
            if (rs.next())
                u = (User) new User().fromResultSet(rs);
            readUser.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return u;
    }

    //Service methods
    private static void init(String username, String password) {
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
        } catch (JSchException e) {
            System.err.println("Could not make an ssh tunnel...");
            e.printStackTrace();
        }
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC Driver not found...");
            e.printStackTrace();
        }

        try {
            c = DriverManager.getConnection("jdbc:oracle:thin:@" + rHost + ":" + lPort + ":" + sid, username, password);
        } catch (Exception e) {
            System.err.println("Connection to DB failed...");
            e.printStackTrace();
        }

        if (c == null) {
            System.err.println("Unknown error when creating a connection...");
        }
    }

    private Integer mergeEntity(Entity e) {
        Entity search = getEntity(e.getClass(), e.getId());
        String sql;
        if (search == null)
            sql = prepareInsertStatement(e);
        else
            sql = prepareUpdateStatement(e);

        try {
            ResultSet rs;
            c.createStatement().executeUpdate(sql);
        } catch (Exception ex) {
            System.err.print(ex.getMessage());
        }
        if (e.getId() == 0) {
            List<Integer> ids = new ArrayList<Integer>();
            for (Entity entity : getAll((Class<Entity>) e.getClass()))
                ids.add(entity.getId());
            Collections.sort(ids);
            setEntityId(e,ids.get(ids.size()-1));
            return ids.get(ids.size()-1);
        }

        return null;
    }
    private void mergeLinker(Linker l) {
        String[] classNames = splitClasses(l.getClass().getSimpleName());

        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(l.getClass().getSimpleName()).append(" ( ");
        sql.append(classNames[0]).append("_Id, ").append(classNames[1]).append("_Id ) ");
        sql.append("VALUES (").append(l.getId1()).append(", ").append(l.getId2()).append(" )");

        try {
            c.createStatement().executeUpdate(sql.toString());
        }
        catch (Exception ex) {
            System.err.print(ex.getMessage());
        }
    }
    private void deleteEntity(Entity e) {
        try {
            c.createStatement().executeUpdate("DELETE FROM "+e.getClass().getSimpleName() + "S WHERE Id = "+e.getId());
        }
        catch (Exception ex) {
            System.err.print(ex.getMessage());
        }
    }
    private void deleteLinker(Linker l) {
        String[] classNames = splitClasses(l.getClass().getSimpleName());

        StringBuilder statement = new StringBuilder("DELETE FROM ").append(l.getClass().getSimpleName());
        statement.append(" WHERE ").append(classNames[0]).append("_Id = ").append(l.getId1());
        statement.append(" AND ").append(classNames[1]).append("_Id = ").append(l.getId2());
        try {
            c.createStatement().executeUpdate(statement.toString());
        }
        catch (Exception ex) {
            System.err.print(ex.getMessage());
        }
    }
    private String[] splitClasses(String classes) {
        String[] result = new String[2];
        int index = 0;
        while (true) {
            index = classes.indexOf('s', index+1);
            if (Character.isUpperCase(classes.charAt(index+1)))
                break;
        }
        result[0] = classes.substring(0, index);
        result[1] = classes.substring(index+1, classes.length()-1);

        return result;
    }
    private String prepareInsertStatement(Entity e) {
        String dateFormat = "YYYY-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
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
                    values.add("TO_DATE('"+df.format(fields[i].get(e))+"', '"+dateFormat+"')");
                else
                    values.add(fields[i].get(e) != null ? fields[i].get(e).toString() : null);
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
                sb.append("TO_DATE('"+df.format(fields[1].get(e))+"', '"+dateFormat+"')");
            else
                sb.append(fields[1].get(e).toString());

            for (int i = 2; i < fields.length; i++) {
                sb.append(", ").append(fields[i].getName()).append(" = ");
                fields[i].setAccessible(true);
                if (fields[i].getType().equals(String.class))
                    sb.append("'"+fields[i].get(e).toString()+"'");
                else if (fields[i].getType().equals(Date.class))
                    sb.append("TO_DATE('"+df.format(fields[i].get(e))+"', '"+dateFormat+"')");
                else
                    sb.append(fields[i].get(e) != null ? fields[i].get(e).toString() : null);
            }
            sb.append(" WHERE Id = ").append(fields[0].get(e));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return sb.toString();
    }
    private void setEntityId(Entity e, int id) {
        try {
            for (Field f : e.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.getName().toLowerCase().contains("id"))
                    f.set(e, id);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
