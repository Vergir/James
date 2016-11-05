package dbobjects;

import java.sql.ResultSet;

/**
 * Created by Vergir on 05/11/2016.
 */
public interface DbObject {
    public DbObject fromResultSet(ResultSet rs);
}
