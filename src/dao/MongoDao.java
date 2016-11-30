package dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.DeleteOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import dbobjects.interfaces.DbObject;
import dbobjects.interfaces.Nameable;
import org.bson.Document;
import org.bson.types.ObjectId;

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
    public BigInteger upsert(DbObject object) {
        BigInteger id = object.getId();
        if (id == null || id.equals(BigInteger.ZERO))
            return insert(object);
        else
            return update(object);
    }

    @Override
    public <T extends DbObject> Set<T> getAll(Class<T> returnType) {
        Set<T> entities = new HashSet<T>();
        MongoCursor<Document> result = instance.db.getCollection(returnType.getSimpleName()+"s").find().iterator();
        while (result.hasNext())
            entities.add(documentToDbObject(result.next(), returnType));
        result.close();
        return entities;
    }
    @Override
    public <T extends DbObject> T getDbObject(Class<T> returnType, BigInteger id) {
        T entity = null;
        Document findResult = instance.db.getCollection(returnType.getSimpleName()+"s").find(Filters.eq(new ObjectId(id.toByteArray()))).first();
        if (findResult != null)
            entity = documentToDbObject(findResult, returnType);
        return entity;
    }
    @Override
    public <T extends DbObject & Nameable> T getByName(Class<T> returnType, String name) {
        String dbNameField = getDbNameField(returnType);
        T entity = null;
        Document findResult = instance.db.getCollection(returnType.getSimpleName()+"s").find(Filters.eq(dbNameField,name)).first();
        if (findResult != null)
            entity = documentToDbObject(findResult, returnType);
        return entity;
    }
    @Override
    public BigInteger delete(DbObject object) {
        BigInteger id = object.getId();
        if (id == null || id.equals(BigInteger.ZERO))
            return null;

        DeleteResult deleteResult = db.getCollection(object.getClass().getSimpleName()+"s").deleteOne(Filters.eq(new ObjectId(id.toByteArray())));

        if (deleteResult.getDeletedCount() > 0)
            return id;
        else
            return null;
    }

    //Service methods
    private static void init(String username, String password, String dbHost) {
        instance.db = new MongoClient(dbHost).getDatabase("test");
        if (instance.db == null) {
            System.err.println("Unknown error when creating a connection...");
        }
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
    private Document dbObjectToDocument(DbObject dbObject) {
        Document result = new Document();
        try {
            BigInteger id = dbObject.getId();
            if (id != null && !id.equals(BigInteger.ZERO))
                result.append("_id", new ObjectId(id.toByteArray()));
            for (Field f : dbObject.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                Object value = f.get(dbObject);
                if (value == null || value.equals(dbObject.getId()))
                    continue;
                if (value instanceof Set) {
                    ArrayList<ObjectId> ids = new ArrayList<>();
                    for (BigInteger biId : (Set<BigInteger>)value)
                        ids.add(new ObjectId(biId.toByteArray()));
                    result.append(f.getName(), ids);
                    continue;
                }
                if (value instanceof BigInteger && f.getName().toLowerCase().contains("id"))
                    result.append(f.getName(), new ObjectId(((BigInteger)value).toByteArray()));
                else
                    result.append(f.getName(), f.get(dbObject));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private String getDbNameField(Class c) {
        if (Nameable.class.isAssignableFrom(c))
            try {
                return ((Nameable)c.newInstance()).getNameField();
            } catch (Exception e) {
                e.printStackTrace();
            }
        for (Field f : c.getDeclaredFields())
            if (f.getName().toLowerCase().contains("name"))
                return f.getName();
        return null;
    }

    private BigInteger insert(DbObject object) {
        MongoCollection coll = db.getCollection(object.getClass().getSimpleName()+"s");
        Document objectDoc = dbObjectToDocument(object);
        coll.insertOne(objectDoc, new InsertOneOptions().bypassDocumentValidation(true));
        BigInteger newId = new BigInteger(((Document)coll.find(objectDoc).first()).getObjectId("_id").toByteArray());
        setId(object, newId);

        return newId;
    }
    private BigInteger update(DbObject object) {
        UpdateResult updateResult = db.getCollection(object.getClass().getSimpleName()+"s").replaceOne(
                Filters.eq(new ObjectId(object.getId().toByteArray())),
                dbObjectToDocument(object),
                new UpdateOptions().upsert(false).bypassDocumentValidation(true)
        );

        return object.getId();
    }

    private void setId(DbObject object, BigInteger id) {
        List<Field> possibleIdFields = new ArrayList<Field>();
        try {
            for (Field f : object.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.getType().equals(BigInteger.class))
                    possibleIdFields.add(f);
            }
            if (possibleIdFields.size() == 0)
                return;
            if (possibleIdFields.size() > 1) {
                BigInteger currentId = object.getId();
                for (Field f : possibleIdFields) {
                    if ((currentId == null && (f.get(object) == null)) || (currentId != null && currentId.equals(f.get(object))))
                        continue;
                    else
                        possibleIdFields.remove(f);
                }
            }
            if (possibleIdFields.size() > 1) {
                for (Field f : possibleIdFields)
                    if (!f.getName().toLowerCase().contains("id"))
                        possibleIdFields.remove(f);
            }
            Field expectedIdField = possibleIdFields.get(0);
            expectedIdField.set(object, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
