package dbobjects.entities;

import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Created by Stanislav on 31.10.2016.
 * stas33553377@yandex.ru
 */


public class Publisher implements Entity, Nameable {
    private int id;
    private String name;
    private String address;
    private String email;

    public Publisher(){}

    public Publisher(String name, String address, String email){
        if (name == null || address == null || email == null)
            throw new NullPointerException("initialization info for Publisher is invalid");

        this.name = name;
        this.address = address;
        this.email = email;
    }

    private Publisher(int id, String name, String address, String email) {
        this(name, address, email);
        this.id = id;
    }

    @Override
    public Entity fromResultSet(ResultSet rs) {
        try {
            this.id = rs.getInt("Id");
            this.name = rs.getString("Name");
            this.address = rs.getString("Address");
            this.email = rs.getString("Email");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString(){
        return "Publisher: \n" +
                " Name: "+name+
                "\n Address: "+address+
                "\n Email: "+email+"\n";
    }

}

