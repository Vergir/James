package dbobjects.entities;

import dbobjects.DbObject;
import org.bson.Document;

import java.math.BigInteger;
import java.util.Date;

public class Game implements Entity, Nameable {
    BigInteger id;
    String title;
    String description;
    double price;
    Date released;

    public Game(){}
    public Game (String title, String description, double price, Date released){
        if(title == null || description == null || released == null)
            throw new NullPointerException("initialization info for Game is invalid");

        this.title=title;
        this.description=description;
        this.price=price;
        this.released=released;
    }

    @Override
    public BigInteger getId() {
        return id;
    }
    @Override
    public String getName() {
        return title;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }
    public Date getReleased() {
        return released;
    }

    public void setTitle(String t) {
        this.title = t;
    }
    public void setDescription(String d) {
        this.description = d;
    }
    public void setPrice(double p) {
        this.price = p;
    }
    public void setReleased(Date r) {
        this.released = r;
    }

    @Override
    public DbObject fromDocument(Document doc) {
        id = new BigInteger(doc.getObjectId("_id").toByteArray());
        title = doc.getString("Title");
        description = doc.getString("Description");
        price = doc.getDouble("Price");
        released = doc.getDate("ReleaseDate");

        return this;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=..." + id.toString(16).substring(18) +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", released=" + released +
                '}';
    }
}