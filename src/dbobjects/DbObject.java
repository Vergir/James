package dbobjects;

import java.io.Serializable;
import java.sql.ResultSet;

/**
 * Created by Vergir on 05/11/2016.
 */
public interface DbObject extends Serializable {
    public DbObject fromResultSet(ResultSet rs);
}
