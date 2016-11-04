package entities;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Game implements Entity,Nameable {
    int id;
    String title;
    String description;
    Blob cover;
    int price;
    Date released;
    String product_type;

    public Game(){}

    public Game (String title, String description, Blob cover, int price, Date released, String product_type){
        if(title == null || description == null|| released == null || product_type == null)
            throw new NullPointerException("initialization info for Game is invalid");

        this.title=title;
        this.description=description;
        this.cover=cover;
        this.price=price;
        this.cover=cover;
        this.released=released;
        this.product_type=product_type;
    }

    private Game (int id, String title, String description, Blob cover, int price, Date released, String product_type){
        this(title,description,cover,price,released,product_type);
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
            this.title = rs.getString("Title");
            this.description = rs.getString("Description");
            this.cover = rs.getBlob("Cover");
            this.price = rs.getInt("Price");
            this.released = rs.getDate("Released");
            this.product_type = rs.getString("Product_type");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public String getName() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public Blob getcover() {
        return cover;
    }
    public int getPrice() {
        return price;
    }
    public Date getReleased() {
        return released;
    }
    public String getProduct_type() {
        return product_type;
    }

    public void setTitle(String t) {
        this.title = t;
    }
    public void setDescription(String d) {
        this.description = d;
    }
    public void setCover(Blob c) {
        this.cover = c;
    }
    public void setPrice(int p) {
        this.price = p;
    }
    public void setReleased(Date r) {
        this.released = r;
    }
    public void setProduct_type(String p) {
        this.product_type = p;
    }

    @Override
    public String toString(){
        return "Game: \n" +
                " Title: "+title+
                "\n Description: "+description+
                "\n Price: "+price+
                "\n Released: "+released+
                "\n Product type: "+product_type+"\n";
    }
}