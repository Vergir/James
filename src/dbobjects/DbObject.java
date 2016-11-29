package dbobjects;

import org.bson.Document;

import java.io.Serializable;
import java.sql.ResultSet;

/**
 * Created by Vergir on 05/11/2016.
 */
public interface DbObject extends Serializable {
    DbObject fromDocument(Document doc);
}
