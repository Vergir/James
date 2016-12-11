package dbobjects.classes;

import dbobjects.interfaces.DbObject;
import dbobjects.interfaces.Nameable;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Stanislav on 31.10.2016.
 * stas33553377@yandex.ru
 */


public class Publisher implements DbObject, Nameable {
    private BigInteger id;
    private String name;
    private String address;
    private String email;
    private Set<BigInteger> games = new HashSet<>();

    public Publisher(){}
    public Publisher(String name, String address, String email){
        if (name == null || address == null || email == null)
            throw new NullPointerException("initialization info for Publisher is invalid");

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
        StringBuilder gamesSb = new StringBuilder("[");
        for (BigInteger id : games)
            gamesSb.append(id.toString(16)).append(',');
        if (games.size() > 0)
            gamesSb.deleteCharAt(gamesSb.length()-1);
        gamesSb.append(']');

        return "Publisher{" +
                "id=" + id.toString(16) +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", games=" + gamesSb +
                '}';
    }
}

