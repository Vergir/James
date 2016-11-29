
package dbobjects.entities;
import dbobjects.DbObject;
import org.bson.Document;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by Stanislav on 31.10.2016.
 * stas33553377@yandex.ru
 */
public class Comment implements Entity {

    private BigInteger id;
    private BigInteger userId;
    private BigInteger gameId;
    private int score;
    private String content;
    private Date writeDate;

    public Comment(){}
    public Comment(BigInteger userId, BigInteger gameId, int score, String content, Date writeDate){
        if (writeDate == null || content == null)
            throw new NullPointerException("initialization info for Comment is invalid");

        this.userId = userId;
        this.gameId = gameId;
        this.score = score;
        this.content = content;
        this.writeDate = writeDate;
    }

    @Override
    public BigInteger getId() {
        return id;
    }

    public BigInteger getUserId() {
        return userId;
    }
    public BigInteger getGameId() {
        return gameId;
    }
    public int getScore() {
        return score;
    }
    public String getContent() {
        return content;
    }
    public Date getWriteDate() {
        return writeDate;
    }

    public void setUserId(BigInteger u_id) {
        this.userId = u_id;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void setGameId(BigInteger gameId) {
        this.gameId = gameId;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setWriteDate(Date writeDate) {
        this.writeDate = writeDate;
    }

    @Override
    public DbObject fromDocument(Document doc) {
        id = new BigInteger(doc.getObjectId("_id").toByteArray());
        userId = new BigInteger(doc.getObjectId("User").toByteArray());
        gameId = new BigInteger(doc.getObjectId("Game").toByteArray());
        content = doc.getString("Content");
        score = doc.getDouble("Score").intValue();
        writeDate = doc.getDate("C_Date");


        return this;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=..." + id.toString(16).substring(18) +
                ", userId=..." + userId.toString(16).substring(18) +
                ", gameId=..." + gameId.toString(16).substring(18) +
                ", score=" + score +
                ", content='" + content + '\'' +
                ", writeDate=" + writeDate +
                '}';
    }
}
