package entities;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Stanislav on 31.10.2016.
 * stas33553377@yandex.ru
 */
public class Transaction implements Entity{

    private int id;
    private int user_id;
    private int sum;
    private Date t_Date;

    public Transaction(){}
    public Transaction(int user_id, int sum, Date t_Date){
        if (t_Date == null)
            throw new NullPointerException("initialization info for Transaction is invalid");

        this.user_id = user_id;
        this.sum = sum;
        this.t_Date = t_Date;
    }
    private Transaction(int id, int user_id, int sum, Date t_Date) {
        this(user_id,sum,t_Date);
        this.id = id;
    }

    @Override
    public Entity fromResultSet(ResultSet rs) {
        try {
            this.id = rs.getInt("Id");
            this.user_id = rs.getInt("User_id");
            this.sum = rs.getInt("Sum");
            this.t_Date = rs.getDate("T_DATE");
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
    public int getSum() {
        return sum;
    }
    public Date getT_Date() {
        return t_Date;
    }

    public void setUser_id(int u_id) {
        this.user_id = u_id;
    }
    public void setSum(int sum) {
        this.sum = sum;
    }
    public void setT_Date(Date t_Date) {
        this.t_Date = t_Date;
    }
}
