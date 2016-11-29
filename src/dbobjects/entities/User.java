package dbobjects.entities;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import dbobjects.DbObject;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by Vergir on 31/10/2016.
 */
public class User implements Entity, Nameable{
    BigInteger id;
    String nickName;
    String firstName;
    String lastName;
    String email;
    Date registered;
    double balance;
    Set<BigInteger> games;

    public User(){}
    public User(String nick, String first, String last, String email){
        if (nick == null || first == null || last == null || email == null)
            throw new NullPointerException("initialization info for User is invalid");

        nickName = nick;
        firstName = first;
        lastName = last;
        this.email = email;
    }

    @Override
    public BigInteger getId() {
        return id;
    }
    @Override
    public String getName() {
        return nickName;
    }
    public String getNickName() {
        return nickName;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public Date getRegistered() {
        return registered;
    }
    public double getBalance() {
        return balance;
    }
    public Set<BigInteger> getGames() {
        return games;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setRegistered(Date registered) {
        this.registered = registered;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public void setGames(Set<BigInteger> games) {
        this.games = games;
    }

    @Override
    public DbObject fromDocument(Document doc) {
        id = new BigInteger(doc.getObjectId("_id").toByteArray());
        nickName = doc.getString("Nickname");
        firstName = doc.getString("First_Name");
        lastName = doc.getString("Last_Name");
        email = doc.getString("Email");
        registered = doc.getDate("Registration");
        balance = doc.getDouble("balance");
        games = new HashSet<BigInteger>();
        for (Object o : (ArrayList<Object>)doc.get("Games"))
            games.add(new BigInteger(((ObjectId)o).toByteArray()));

        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=..." + id.toString(16).substring(18) +
                ", nickName='" + nickName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", registered=" + registered +
                ", balance=" + balance +
                ", games=" + games +
                '}';
    }
}
