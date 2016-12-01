package dbobjects.classes;
import dbobjects.interfaces.DbObject;

import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

/**
 * Created by Stanislav on 31.10.2016.
 * stas33553377@yandex.ru
 */
public class Transaction implements DbObject{
    private BigInteger id;
    private BigInteger userId;
    private Double sum;
    private Date transactionDate;
    private Set<BigInteger> games;

    public Transaction(){}
    public Transaction(BigInteger userId, Double sum, Date transactionDate){
        if (transactionDate == null)
            throw new NullPointerException("initialization info for Transaction is invalid");

        this.userId = userId;
        this.sum = sum;
        this.transactionDate = transactionDate;
    }

    @Override
    public BigInteger getId() {
        return id;
    }
    public BigInteger getUserId() {
        return userId;
    }
    public Double getSum() {
        return sum;
    }
    public Date getTransactionDate() {
        return transactionDate;
    }
    public Set<BigInteger> getGames() {
        return games;
    }

    public void setUserId(BigInteger u_id) {
        this.userId = u_id;
    }
    public void setSum(Double sum) {
        this.sum = sum;
    }
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
    public void setGames(Set<BigInteger> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        StringBuilder gamesSb = new StringBuilder("[");
        for (BigInteger id : games)
            gamesSb.append(id.toString(16)).append(',');
        if (games.size() > 0)
            gamesSb.deleteCharAt(gamesSb.length()-1);
        gamesSb.append(']');

        return "Transaction{" +
                "id=" + id.toString(16) +
                ", userId=" + userId.toString(16) +
                ", sum=" + sum +
                ", transactionDate=" + transactionDate +
                ", games=" + gamesSb +
                '}';
    }
}
