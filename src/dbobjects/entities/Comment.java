
package dbobjects.entities;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Stanislav on 31.10.2016.
 * stas33553377@yandex.ru
 */
public class Comment implements Entity {

    private int id;
    private int user_id;
    private int game_id;
    private int score;
    private String content;
    private Date c_Date;

    public Comment(){}
    public Comment(int user_id, int game_id, int score, String content, Date c_Date){
        if (c_Date == null || content == null)
            throw new NullPointerException("initialization info for Comment is invalid");

        this.user_id = user_id;
        this.game_id = game_id;
        this.score = score;
        this.content = content;
        this.c_Date = c_Date;
    }

    @Override
    public Entity fromResultSet(ResultSet rs) {
        try {
            this.id = rs.getInt("Id");
            this.user_id = rs.getInt("User_id");
            this.game_id = rs.getInt("Game_id");
            this.score = rs.getInt("Score");
            this.content = rs.getString("Content");
            this.c_Date = rs.getDate("C_Date");
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

    public int getUser_id() {
        return user_id;
    }
    public int getGame_id() {
        return game_id;
    }
    public int getScore() {
        return score;
    }
    public String getContent() {
        return content;
    }
    public Date getC_Date() {
        return c_Date;
    }

    public void setUser_id(int u_id) {
        this.user_id = u_id;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setC_Date(Date c_Date) {
        this.c_Date = c_Date;
    }

    @Override
    public String toString(){
        return "Comment: \n" +
                " User_id: "+user_id+
                "\n Game_id: "+game_id+
                "\n Score: "+score+
                "\n Content: "+content+
                "\n Email: "+c_Date+"\n";
    }
}
