import dao.MongoDao;
import dbobjects.classes.*;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

public class Main{
    static String username;
    static String password;
    static String dbHost;
    static String redisHost;

    public static void main(String[] arg) throws IOException {
        if(arg.length > 1) {
            username=arg[0];
            password=arg[1];
            if (arg.length > 2) {
                dbHost = arg[2];
                if (arg.length > 3)
                    redisHost = arg[3];
            }
        }
        MongoDao dao = MongoDao.getInstance(username,password, dbHost);

        Set<Game> games = dao.getAll(Game.class);

        Game lol = games.iterator().next();
        Game lulULU = new Game("lmao", "test", 400, new Date());
    }
}