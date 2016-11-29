package dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import dbobjects.interfaces.DbObject;
import dbobjects.interfaces.Nameable;
import org.bson.Document;
import org.bson.types.ObjectId;
import sun.tools.asm.CatchData;

import javax.print.Doc;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.*;

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
    public BigInteger merge(DbObject object) {
        if (object.getId() == null || object.getId().compareTo(BigInteger.ZERO) == 0)
            return insertDbObject(object);
        else
            return updateDbObject(object);
    }

    @Override
    public <T extends DbObject> Set<T> getAll(Class<T> returnType) {
        Set<T> entities = new HashSet<T>();

        MongoCursor<Document> result = instance.db.getCollection(returnType.getSimpleName()+"s").find().iterator();

        try {
            while (result.hasNext())
                entities.add(documentToDbObject(result.next(), returnType));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            result.close();
        }

        return entities;
    }
    @Override
    public <T extends DbObject> T getDbObject(Class<T> returnType, BigInteger id) {
        T entity = null;

        Document d = instance.db.getCollection(returnType.getSimpleName()+"s").find(Filters.eq("_id",new ObjectId(id.toByteArray()))).first();

        if (d != null)
            try {
                entity = documentToDbObject(d, returnType);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        return entity;
    }
    @Override
    public <T extends DbObject, Nameable> T getByName(Class<T> returnType, String name) {
        String dbNameField = getDbNameField(returnType);
        T entity = null;

        Document d = instance.db.getCollection(returnType.getSimpleName()+"s").find(Filters.eq(dbNameField,name)).first();

        if (d != null)
            try {
                entity = documentToDbObject(d, returnType);
            }
            catch (Exception e) {
                e.printStackTrace();
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
        instance.db = new MongoClient(dbHost).getDatabase("test");
        if (instance.db == null) {
            System.err.println("Unknown error when creating a connection...");
        }
    }

    private Document dbObjectToInsertDocument(DbObject object) {
        return null;
    }
    private Document dbObjectToUpdateDocument(DbObject object) {
        return null;
    }

    private <T extends DbObject> T documentToDbObject(Document document, Class<T> returnType) {
        T instance = null;

        try {
            instance = returnType.newInstance();
            Field id = returnType.getDeclaredField("id");
            id.setAccessible(true);
            id.set(instance, new BigInteger(((ObjectId)document.get("_id")).toByteArray()));
            for (Field f : returnType.getDeclaredFields()) {
                f.setAccessible(true);
                Object value = document.get(f.getName());
                if (value == null)
                    continue;
                if (value instanceof ArrayList) {
                    Set<BigInteger> fieldValue = new HashSet<BigInteger>();
                    for (ObjectId oid : (ArrayList<ObjectId>) value)
                        fieldValue.add(new BigInteger(oid.toByteArray()));
                    f.set(instance, fieldValue);
                    continue;
                }
                if (value instanceof ObjectId)
                    f.set(instance, new BigInteger(((ObjectId)value).toByteArray()));
                else
                    f.set(instance, value);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return instance;
    }

    private String getDbNameField(Class c) {
        for (Field f : c.getDeclaredFields()) {
            String fieldName = f.getName().toLowerCase();
            if (fieldName.equals("name"))
                return "Name";
            if (fieldName.equals("nickname"))
                return "Nickname";
            if (fieldName.equals("title"))
                return "Title";
            if (fieldName.equals("nick_name"))
                return "Nick_name";
        }
        return null;
    }

    private BigInteger insertDbObject(DbObject object) {
        return null;
    }
    private BigInteger updateDbObject(DbObject object) {
        return null;
    }
}
