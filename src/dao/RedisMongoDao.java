package dao;

import dbobjects.DbObject;
import dbobjects.entities.Entity;
import dbobjects.entities.Nameable;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vergir on 14/11/2016.
 */
public final class RedisMongoDao implements DatabaseAccessObject {

    private static RedisMongoDao instance;
    private Jedis redis;
    private MongoDao realDao;
    private int expirationTime = 5;

    private RedisMongoDao() {
        if (instance != null)
            throw new ExceptionInInitializerError("Tried to initialize singleton");
    }

    public static RedisMongoDao getInstance(String username, String password, String dbHost, String redisHost) {
        if (username == null || password == null || redisHost == null)
            throw new NullPointerException("username/password combination is invalid");
        if (instance == null)
            instance = new RedisMongoDao();
        init(username, password, dbHost, redisHost);
        return instance;
    }
    private static void init(String username, String password, String dbHost, String redisHost) {
        instance.realDao = MongoDao.getInstance(username, password, dbHost);
        instance.redis = new Jedis(redisHost, 6379, 10000);
        instance.flushCache();
    }

    @Override
    public Integer merge(DbObject object) {
        if(object == null)
            return null;
        byte[] flagKey = convertToBytes("FLAG:"+object.getClass().getSimpleName());
        redis.set(flagKey, convertToBytes(new Boolean((true))));

        return realDao.merge(object);
    }

    @Override
    public <T extends DbObject> Set<T> getAll(Class<T> returnType) {
        String classKey = returnType.getSimpleName();
        byte[] dataKey = convertToBytes("DATA:"+classKey+":ALL");
        byte[] timeKey = convertToBytes("TIME:"+classKey+":ALL");
        byte[] flagKey = convertToBytes("FLAG:"+classKey);

        if (cacheIsFresh(timeKey, flagKey))
            return  (Set<T>)convertFromBytes(redis.get(dataKey));
        Set<T> result = realDao.getAll(returnType);
        redis.set(dataKey, convertToBytes(result));
        redis.set(timeKey, convertToBytes(new Date()));
        redis.set(flagKey, convertToBytes(new Boolean(false)));

        return result;
    }
    @Override
    public <T extends Entity> T getEntity(Class<T> returnType, BigInteger id) {
        String classKey = returnType.getSimpleName();
        byte[] dataKey = convertToBytes("DATA:"+classKey+":ID:"+id);
        byte[] timeKey = convertToBytes("TIME:"+classKey+":ID:"+id);
        byte[] flagKey = convertToBytes("FLAG:"+classKey);

        if (cacheIsFresh(timeKey, flagKey))
            return  (T)convertFromBytes(redis.get(dataKey));
        T result = realDao.getEntity(returnType, id);
        redis.set(dataKey, convertToBytes(result));
        redis.set(timeKey, convertToBytes(new Date()));
        redis.set(flagKey, convertToBytes(new Boolean(false)));

        return result;
    }

    @Override
    public <T extends Nameable> T getByName(Class<T> returnType, String name) {
        String classKey = returnType.getSimpleName();
        byte[] dataKey = convertToBytes("DATA:"+classKey+":NAME:"+name);
        byte[] timeKey = convertToBytes("TIME:"+classKey+":NAME:"+name);
        byte[] flagKey = convertToBytes("FLAG:"+classKey);

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
        if (object == null)
            return;
        byte[] flagKey = convertToBytes("FLAG:"+object.getClass().getSimpleName());
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

    public int getCacheSize() {
        return redis.scan(new byte[]{0}).getResult().size();
    }
    public Set<String> getAllKeys() {
        Set<String> result = new HashSet<String>();
        for (byte[] key : redis.scan(new byte[]{0}).getResult())
            result.add((String)convertFromBytes(key));
        return result;
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
