package dbobjects.linkers;

import dbobjects.DbObject;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vergir on 05/11/2016.
 */
public class TransactionsGames implements Linker {
    private int transactionId;
    private int gameId;

    public TransactionsGames() {}
    public TransactionsGames(int transactionId, int gameId) {
        this.transactionId = transactionId;
        this.gameId = gameId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
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
            this.transactionId = rs.getInt(1);
            this.gameId = rs.getInt(2);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public int getId1() {
        return getTransactionId();
    }

    @Override
    public int getId2() {
        return getGameId();
    }

    @Override
    public String toString() {
        return "TransactionsGames{" +
                "transactionId=" + transactionId +
                ", gameId=" + gameId +
                '}';
    }
}
