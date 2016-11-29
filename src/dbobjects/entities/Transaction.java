package dbobjects.entities;
import dbobjects.DbObject;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Stanislav on 31.10.2016.
 * stas33553377@yandex.ru
 */
public class Transaction implements Entity{

    private BigInteger id;
    private BigInteger userId;
    private double sum;
    private Date transactionDate;
    private Set<BigInteger> games;

    public Transaction(){}
    public Transaction(BigInteger userId, int sum, Date transactionDate){
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
    public double getSum() {
        return sum;
    }
    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setUserId(BigInteger u_id) {
        this.userId = u_id;
    }
    public void setSum(double sum) {
        this.sum = sum;
    }
    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public DbObject fromDocument(Document doc) {
        id = new BigInteger(doc.getObjectId("_id").toByteArray());
        userId = new BigInteger(doc.getObjectId("User").toByteArray());
        sum = doc.getDouble("Sum");
        transactionDate = doc.getDate("T_DATE");
        games = new HashSet<>();
        for (Object o : (ArrayList<Object>)doc.get("Games"))
            games.add(new BigInteger(((ObjectId)o).toByteArray()));

        return this;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=..." + id.toString(16).substring(18) +
                ", userId=..." + userId.toString(16).substring(18) +
                ", sum=" + sum +
                ", transactionDate=" + transactionDate +
                ", games=" + games +
                '}';
    }
}
