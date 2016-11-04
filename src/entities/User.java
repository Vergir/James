package entities;

import java.sql.*;
import java.util.Date;

/**
 * Created by Vergir on 31/10/2016.
 */
public class User implements Entity, Nameable{


    int id;
    String nickName;
    String firstName;
    String lastName;
    String email;
    Date registered;
    int balance;

    public User(){}

    public User(String nick, String first, String last, String email){
        if (nick == null || first == null || last == null || email == null)
            throw new NullPointerException("initialization info for User is invalid");

        nickName = nick;
        firstName = first;
        lastName = last;
        this.email = email;
    }

    private User (int id, String nick, String first, String last, String email){
        this(nick,first,last,email);
        this.id=id;
    }


    @Override
    public int getId() {
        return id;
    }

    @Override
    public Entity fromResultSet(ResultSet rs) {
        try {
            this.id = rs.getInt("Id");
            this.nickName = rs.getString(2);
            this.firstName = rs.getString(3);
            this.lastName = rs.getString(4);
            this.email = rs.getString(5);
            this.registered = rs.getDate(6);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public String getName() {
        return nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString(){
        return "User: \n" +
                " Nickname: "+nickName+
                "\n First Name: "+firstName+
                "\n Last Name: "+ lastName+
                "\n Email: "+email+
                "\n Regestered: "+registered+
                "\n Balance: "+balance +"\n";
    }
}
