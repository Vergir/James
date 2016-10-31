package entities;

import java.sql.ResultSet;

public interface Entity {
    int getId();

    Entity fromResultSet(ResultSet rs);
}