package dbobjects.linkers;

import dbobjects.DbObject;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vergir on 05/11/2016.
 */
public class UsersGames implements Linker{
    private int userId;
    private int gameId;

    public UsersGames() {}
    public UsersGames(int userId, int gameId) {
        this.userId = userId;
        this.gameId = gameId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public DbObject fromResultSet(ResultSet rs) {
        try {
            this.userId = rs.getInt(1);
            this.gameId = rs.getInt(2);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public int getId1() {
        return getUserId();
    }

    @Override
    public int getId2() {
        return getGameId();
    }

    @Override
    public String toString() {
        return "UsersGames{" +
                "userId=" + userId +
                ", gameId=" + gameId +
                '}';
    }
}
