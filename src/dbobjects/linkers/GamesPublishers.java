package dbobjects.linkers;

import dbobjects.DbObject;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vergir on 05/11/2016.
 */
public class GamesPublishers implements Linker {
    private int gameId;
    private int publisherId;

    public GamesPublishers() {}
    public GamesPublishers(int gameId, int publisherId) {
        this.gameId = gameId;
        this.publisherId = publisherId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    @Override
    public DbObject fromResultSet(ResultSet rs) {
        try {
            this.gameId = rs.getInt(1);
            this.publisherId = rs.getInt(2);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public int getId1() {
        return getGameId();
    }

    @Override
    public int getId2() {
        return getPublisherId();
    }

    @Override
    public String toString() {
        return "GamesPublishers{" +
                "gameId=" + gameId +
                ", publisherId=" + publisherId +
                '}';
    }
}
