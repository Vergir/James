package dbobjects.linkers;

import dbobjects.DbObject;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vergir on 05/11/2016.
 */
public class GamesDevelopers implements Linker {
    private int gameId;
    private int developerId;

    public GamesDevelopers() {}
    public GamesDevelopers(int gameId, int developerId) {
        this.gameId = gameId;
        this.developerId = developerId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(int developerId) {
        this.developerId = developerId;
    }

    @Override
    public DbObject fromResultSet(ResultSet rs) {
        try {
            this.gameId = rs.getInt(1);
            this.developerId = rs.getInt(2);
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
        return getDeveloperId();
    }

    @Override
    public String toString() {
        return "GamesDevelopers{" +
                "gameId=" + gameId +
                ", developerId=" + developerId +
                '}';
    }
}
