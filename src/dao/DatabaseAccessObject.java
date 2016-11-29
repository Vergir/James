package dao;

import dbobjects.interfaces.DbObject;
import dbobjects.interfaces.Nameable;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by Vergir on 14/11/2016.
 */
public interface DatabaseAccessObject {
    //C*U*
    BigInteger merge(DbObject object);

    //*R**
    <T extends DbObject> Set<T> getAll(Class<T> returnType);
    <T extends DbObject> T getDbObject(Class<T> returnType, BigInteger id);
    <T extends DbObject, Nameable> T getByName(Class<T> returnType, String name);

    //***D
    void delete(DbObject object);
}
