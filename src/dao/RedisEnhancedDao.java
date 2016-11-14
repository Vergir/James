package dao;

import dbobjects.DbObject;
import dbobjects.entities.Entity;
import dbobjects.entities.Nameable;
import dbobjects.linkers.Linker;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vergir on 14/11/2016.
 */
public final class RedisEnhancedDao implements DatabaseAccessObject {
    private static RedisEnhancedDao instance;
    private Jedis redis;
    private BareboneDao realDao;
    private int expirationTime = 5;

    private RedisEnhancedDao() {
        if (instance != null)
            throw new ExceptionInInitializerError("Tried to initialize singleton");
    }

    public static RedisEnhancedDao getInstance(String username, String password, String redisHost) {
        if (username == null || password == null || redisHost == null)
            throw new NullPointerException("username/password combination is invalid");
        if (instance == null)
            instance = new RedisEnhancedDao();
        init(username, password, redisHost);
        return instance;
    }
    private static void init(String username, String password, String redisHost) {
        instance.realDao = BareboneDao.getInstance(username, password);
        instance.redis = new Jedis(redisHost);
        instance.flushCache();
    }

    @Override
    public Integer merge(DbObject object) {
        byte[] flagKey = convertToBytes(object.getClass().getSimpleName()+"flag");
        redis.set(flagKey, convertToBytes(new Boolean((true))));

        return realDao.merge(object);
    }

    @Override
    public <T extends DbObject> Set<T> getAll(Class<T> returnType) {
        String classKey = returnType.getSimpleName();
        byte[] dataKey = convertToBytes("All"+classKey+"data");
        byte[] timeKey = convertToBytes("All"+classKey+"time");
        byte[] flagKey = convertToBytes(classKey+"flag");

        if (cacheIsFresh(timeKey, flagKey))
            return  (Set<T>)convertFromBytes(redis.get(dataKey));
        Set<T> result = realDao.getAll(returnType);
        redis.set(dataKey, convertToBytes(result));
        redis.set(timeKey, convertToBytes(new Date()));
        redis.set(flagKey, convertToBytes(new Boolean(false)));

        return result;
    }
    @Override
    public <T extends Entity> T getEntity(Class<T> returnType, int id) {
        String classKey = returnType.getSimpleName();
        byte[] dataKey = convertToBytes("Entity"+classKey+id+"data");
        byte[] timeKey = convertToBytes("Entity"+classKey+id+"time");
        byte[] flagKey = convertToBytes(classKey+"flag");

        if (cacheIsFresh(timeKey, flagKey))
            return  (T)convertFromBytes(redis.get(dataKey));
        T result = realDao.getEntity(returnType, id);
        redis.set(dataKey, convertToBytes(result));
        redis.set(timeKey, convertToBytes(new Date()));
        redis.set(flagKey, convertToBytes(new Boolean(false)));

        return result;
    }

    @Override
    public <T extends Linker> T getLinker(Class<T> returnType, int id1, int id2) {
        String classKey = returnType.getSimpleName();
        byte[] dataKey = convertToBytes("Linker"+classKey+id1+id2+"data");
        byte[] timeKey = convertToBytes("Linker"+classKey+id1+id2+"time");
        byte[] flagKey = convertToBytes(classKey+"flag");

        if (cacheIsFresh(timeKey, flagKey))
            return  (T)convertFromBytes(redis.get(dataKey));
        T result = realDao.getLinker(returnType, id1, id2);

        redis.set(dataKey, convertToBytes(result));
        redis.set(timeKey, convertToBytes(new Date()));
        redis.set(flagKey, convertToBytes(new Boolean(false)));

        return result;
    }

    @Override
    public <T extends Nameable> T getByName(Class<T> returnType, String name) {
        String classKey = returnType.getSimpleName();
        byte[] dataKey = convertToBytes("Name"+classKey+name+"data");
        byte[] timeKey = convertToBytes("Name"+classKey+name+"time");
        byte[] flagKey = convertToBytes(classKey+"flag");

        if (cacheIsFresh(timeKey, flagKey))
            return  (T)convertFromBytes(redis.get(dataKey));
        T result = realDao.getByName(returnType, name);
        redis.set(dataKey, convertToBytes(result));
        redis.set(timeKey, convertToBytes(new Date()));
        redis.set(flagKey, convertToBytes(new Boolean(false)));

        return result;
    }

    @Override
    public void delete(DbObject object) {
        byte[] flagKey = convertToBytes(object.getClass().getSimpleName()+"flag");
        redis.set(flagKey, convertToBytes(new Boolean((true))));

        realDao.delete(object);
    }

    public int getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(int expirationTime) {
        if (expirationTime > 0 && expirationTime < 20000)
            this.expirationTime = expirationTime;
    }

    public void flushCache() {
        redis.flushDB();
    }

    private byte[] convertToBytes(Object object) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private Object convertFromBytes(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean cacheIsFresh(byte[] timeKey, byte[] flagKey) {
        byte[] cachedTime = redis.get(timeKey);
        byte[] cachedFlag = redis.get(flagKey);
        if (cachedTime == null || cachedFlag == null)
            return false;
        Date timestamp = (Date)convertFromBytes(cachedTime);
        long timeDiff = TimeUnit.MINUTES.convert(new Date().getTime()-timestamp.getTime(), TimeUnit.MILLISECONDS);
        if (timeDiff > expirationTime)
            return false;
        Boolean wasChanged = (Boolean)convertFromBytes(cachedFlag);
        return !wasChanged;
    }
}
