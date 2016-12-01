package dbobjects.classes;

import dbobjects.interfaces.DbObject;
import dbobjects.interfaces.Nameable;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by Vergir on 31/10/2016.
 */
public class User implements DbObject, Nameable {
    BigInteger id;
    String nickName;
    String firstName;
    String lastName;
    String email;
    Date registrationDate;
    Double balance;
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
    public Date getRegistrationDate() {
        return registrationDate;
    }
    public Double getBalance() {
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
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public void setGames(Set<BigInteger> games) {
        this.games = games;
    }

    @Override
    public String getNameField() {
        return "nickName";
    }

    @Override
    public String toString() {
        StringBuilder gamesSb = new StringBuilder("[");
        for (BigInteger id : games)
            gamesSb.append(id.toString(16)).append(',');
        if (games.size() > 0)
            gamesSb.deleteCharAt(gamesSb.length()-1);
        gamesSb.append(']');

        return "User{" +
                "id=" + id.toString(16) +
                ", nickName='" + nickName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                ", balance=" + balance +
                ", games=" + gamesSb +
                '}';
    }
}
