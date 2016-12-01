package dbobjects.classes;

import dbobjects.interfaces.DbObject;
import dbobjects.interfaces.Nameable;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Developer implements DbObject, Nameable {
    private BigInteger id;
    private String name;
    private String address;
    private String email;
    private Set<BigInteger> games;

    public Developer(){}
    public Developer(String name, String address, String email){
        if (name == null || address == null || email == null)
            throw new NullPointerException("initialization info for Developer is invalid");

        this.name = name;
        this.address = address;
        this.email = email;
    }


    @Override
    public BigInteger getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getEmail() {
        return email;
    }
    public Set<BigInteger> getGames() {
        return games;
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
    public void setGames(Set<BigInteger> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        return "Developer{" +
                "id=" + id.toString(16) +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", games=" + games +
                '}';
    }
}
