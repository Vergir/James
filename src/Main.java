import dao.MongoDao;
import dbobjects.entities.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

public class Main{
    static String username;
    static String password;
    static String dbHost;
    static String redisHost;

    public static void main(String[] arg) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        if(arg.length > 1) {
            username=arg[0];
            password=arg[1];
            if (arg.length > 2) {
                dbHost = arg[2];
                if (arg.length > 3)
                    redisHost = arg[3];
            }
        }
        else {
            System.out.println("Enter username/password :");
            username = br.readLine();
            password = br.readLine();
        }

        //RedisMongoDao dao = RedisMongoDao.getInstance(username, password, "46.101.212.60");
        MongoDao dao = MongoDao.getInstance(username,password, dbHost);

        Set<Game> games = dao.getAll(Game.class);

        Game xcom = dao.getByName(Game.class, "XCOM2");
        User Stas = dao.getByName(User.class, "Lepec1n");
        Publisher twoK = dao.getByName(Publisher.class, "2k");
    }
}