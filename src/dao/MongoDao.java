package dao;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import dbobjects.DbObject;
import dbobjects.entities.*;
import oracle.jdbc.internal.OracleTypes;
import org.bson.Document;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.function.Consumer;

public final class MongoDao implements DatabaseAccessObject{
    private static MongoDao instance;
    private MongoDatabase db;

    private MongoDao() {
        if (instance != null)
            throw new ExceptionInInitializerError("Tried to initialize singleton");
    }

    public static MongoDao getInstance(String username, String password, String dbHost) {
        if (instance == null)
            instance = new MongoDao();
        init(username, password, dbHost);
        return instance;
    }

    @Override
    public Integer merge(DbObject object) {
        if (object == null)
            return null;
//        if (object.getClass().equals(User.class)) {
//            return mergeUser((User) object);

//        if (object instanceof Entity) {
//            return mergeEntity((Entity) object);
//        } else if (object instanceof Linker) {
//            mergeLinker((Linker) object);
//            return null;
//        }
        return null;
    }

    @Override
    public <T extends DbObject> Set<T> getAll(Class<T> returnType) {
        Set<T> entities = new HashSet<T>();
        MongoCursor<Document> result = instance.db.getCollection(returnType.getSimpleName()+"s").find().iterator();

        try {
            while (result.hasNext())
                entities.add((T)returnType.newInstance().fromDocument(result.next()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return entities;
    }
    @Override
    public <T extends Entity> T getEntity(Class<T> returnType, BigInteger id) {
        Document d = instance.db.getCollection(returnType.getSimpleName()+"s").find().first();

//        if (returnType.equals(User.class)) {
//            for (User u : getAllUsers())
//                if (u.getId() == id)
//                    return (T) u;
//            return null;
//        }
        T entity = null;
        try {
            //ResultSet results = db.createStatement().executeQuery("SELECT * FROM " + returnType.getSimpleName().toUpperCase()+ "S WHERE Id = " + id);
            ResultSet results = null;
//            if (results.next())
//                entity = (T) returnType.newInstance().fromResultSet(results);
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
        return entity;
    }
    @Override
    public <T extends Nameable> T getByName(Class<T> returnType, String name) {
        String dbName;
//        if (returnType.equals(User.class))
//            return (T) getUserByName(name);
        if (returnType.equals(Game.class))
            dbName = "Title";
        else
            dbName = "Name";

        T entity = null;
        try {
            //ResultSet results = db.createStatement().executeQuery("SELECT r.* FROM " + returnType.getSimpleName().toUpperCase() + "S r WHERE r." + dbName + " = '" + name + "'");
            ResultSet results = null;
//            if (results.next())
//                entity = (T) returnType.newInstance().fromResultSet(results);
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
        return entity;
    }
    @Override
    public void delete(DbObject object) {
//        if (object instanceof Entity) {
//            deleteEntity((Entity) object);
//        } else if (object instanceof Linker) {
//            deleteLinker((Linker) object);
//        }
    }

    //Service methods
    private static void init(String username, String password, String dbHost) {
        MongoClient mc = new MongoClient(dbHost);
        instance.db = mc.getDatabase("test");

        if (instance.db == null) {
            System.err.println("Unknown error when creating a connection...");
        }
    }

    private Integer mergeEntity(Entity e) {
        Object search = getEntity(e.getClass(), e.getId());
        String sql;
        if (search == null)
            sql = prepareInsertStatement(e);
        else
            sql = prepareUpdateStatement(e);

        try {
            ResultSet rs;
            //db.createStatement().executeUpdate(sql);
//            if (e.getId() == 0) {
//                List<Integer> ids = new ArrayList<Integer>();
////                for (Entity entity : getAll((Class<Entity>) e.getClass()))
////                    ids.add(entity.getId());
//                Collections.sort(ids);
//                setEntityId(e,ids.get(ids.size()-1));
//                return ids.get(ids.size()-1);
//            }
        } catch (Exception ex) {
            System.err.print(ex.getMessage());
        }
        return null;
    }
    private void deleteEntity(Entity e) {
        try {
            //db.createStatement().executeUpdate("DELETE FROM "+e.getClass().getSimpleName() + "S WHERE Id = "+e.getId());
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
        String dateFormat = "YYYY-MM-dd";
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
