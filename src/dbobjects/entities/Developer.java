package dbobjects.entities;

import dbobjects.DbObject;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Developer implements Entity, Nameable {
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
    public DbObject fromDocument(Document doc) {
        id = new BigInteger(doc.getObjectId("_id").toByteArray());
        name = doc.getString("Name");
        email = doc.getString("Email");
        address = doc.getString("Address");
        games = new HashSet<>();
        for (Object o : (ArrayList<Object>)doc.get("Games"))
            games.add(new BigInteger(((ObjectId)o).toByteArray()));

        return this;
    }

    @Override
    public String toString() {
        return "Developer{" +
                "id=..." + id.toString(16).substring(18) +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", games=" + games +
                '}';
    }
}
