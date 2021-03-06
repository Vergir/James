
package dbobjects.classes;
import dbobjects.interfaces.DbObject;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by Stanislav on 31.10.2016.
 * stas33553377@yandex.ru
 */
public class Comment implements DbObject {
    private BigInteger id;
    private BigInteger userId;
    private BigInteger gameId;
    private Double score;
    private String content;
    private Date writeDate;

    public Comment(){}
    public Comment(BigInteger userId, BigInteger gameId, Double score, String content, Date writeDate){
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
    public Double getScore() {
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
    public void setScore(Double score) {
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
    public String toString() {
        return "Comment{" +
                "id=" + id.toString(16) +
                ", userId=" + userId.toString(16) +
                ", gameId=" + gameId.toString(16) +
                ", score=" + score +
                ", content='" + content + '\'' +
                ", writeDate=" + writeDate +
                '}';
    }
}
