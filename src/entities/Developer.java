package entities;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Developer implements Entity, Nameable {
    private int id;
    private String name;
    private String address;
    private String email;

    public Developer(){}

    public Developer(String name, String address, String email){
        if (name == null || address == null || email == null)
            throw new NullPointerException("initialization info for Developer is invalid");

        this.name = name;
        this.address = address;
        this.email = email;
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
        return "Game: \n" +
                " Name: "+name+
                "\n Address: "+address+
                "\n Email: "+email+"\n";
    }
}
