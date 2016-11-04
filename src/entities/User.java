package entities;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vergir on 31/10/2016.
 */
public class User implements Entity, Nameable{
    int id;
    String nickname;
    String first_name;
    String last_name;
    String email;
    Date registration;
    int balance;

    public User(){}

    public User(String nick, String first, String last, String email, Date reg, int balance){
        if (nick == null || first == null || last == null || email == null || reg == null)
            throw new NullPointerException("initialization info for User is invalid");

        this.nickname = nick;
        this.first_name = first;
        this.last_name = last;
        this.email = email;
        this.registration = reg;
        this.balance = balance;
    }

    public User (int i, String nick, String first, String last, String email, Date reg, int balance){
        this(nick,first,last,email,reg,balance);
        this.id=i;
    }


    @Override
    public int getId() {
        return id;
    }

    @Override
    public Entity fromResultSet(ResultSet rs) {
        try {
            this.id = rs.getInt("Id");
            this.nickname = rs.getString("Nickname");
            this.first_name = rs.getString("First_name");
            this.last_name = rs.getString("Last_name");
            this.email = rs.getString("Email");
            this.registration = rs.getDate("Registration");
            this.balance = rs.getInt("Balance");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public String getName() {
        return nickname;
    }
    public String getnickname (){
        return nickname;
    }
    public String getfirst_name (){
        return first_name;
    }
    public String getlast_name (){
        return last_name;
    }
    public String getemail (){
        return email;
    }
    public Date getregistration(){
        return registration;
    }
    public int getBalance(){
        return balance;
    }

    public void setnickname(String n){
        this.nickname=n;
    }
    public void setfirst_name(String n){
        this.first_name=n;
    }
    public void setlast_name(String n){
        this.last_name=n;
    }
    public void setemail(String e){
        this.email=e;
    }
    public void setregistration(Date r){
        this.registration=r;
    }
    public void setBalance(int b){
        this.balance=b;
    }

    @Override
    public String toString(){
        return "";
    }
}
