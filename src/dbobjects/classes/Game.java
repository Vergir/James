package dbobjects.classes;

import dbobjects.interfaces.DbObject;
import dbobjects.interfaces.Nameable;

import java.math.BigInteger;
import java.util.Date;

public class Game implements DbObject, Nameable {
    BigInteger id;
    String title;
    String description;
    Double price;
    Date releaseDate;

    public Game(){}
    public Game (String title, String description, double price, Date releaseDate){
        if(title == null || description == null || releaseDate == null)
            throw new NullPointerException("initialization info for Game is invalid");

        this.title=title;
        this.description=description;
        this.price=price;
        this.releaseDate = releaseDate;
    }

    @Override
    public BigInteger getId() {
        return id;
    }
    public String getName() {
        return title;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public Double getPrice() {
        return price;
    }
    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setTitle(String t) {
        this.title = t;
    }
    public void setDescription(String d) {
        this.description = d;
    }
    public void setPrice(Double p) {
        this.price = p;
    }
    public void setReleaseDate(Date r) {
        this.releaseDate = r;
    }

    @Override
    public String getNameField() {
        return "title";
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id.toString(16) +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", releaseDate=" + releaseDate +
                '}';
    }
}