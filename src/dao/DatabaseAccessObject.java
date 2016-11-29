package dao;

import dbobjects.DbObject;
import dbobjects.entities.Entity;
import dbobjects.entities.Nameable;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by Vergir on 14/11/2016.
 */
public interface DatabaseAccessObject {
    //C*U*
    Integer merge(DbObject object);

    //*R**
    <T extends DbObject> Set<T> getAll(Class<T> returnType);
    <T extends Entity> T getEntity(Class<T> returnType, BigInteger id);
    <T extends Nameable> T getByName(Class<T> returnType, String name);

    //***D
    void delete(DbObject object);
}
