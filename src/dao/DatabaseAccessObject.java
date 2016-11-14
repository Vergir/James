package dao;

import dbobjects.DbObject;
import dbobjects.entities.Entity;
import dbobjects.entities.Nameable;
import dbobjects.linkers.Linker;

import java.util.*;

/**
 * Created by Vergir on 14/11/2016.
 */
public interface DatabaseAccessObject {
    //C*U*
    Integer merge(DbObject object);

    //*R**
    <T extends DbObject> Set<T> getAll(Class<T> returnType);
    <T extends Entity> T getEntity(Class<T> returnType, int id);
    <T extends Linker> T getLinker(Class<T> returnType, int id1, int id2);
    <T extends Nameable> T getByName(Class<T> returnType, String name);

    //***D
    void delete(DbObject object);
}
