import app.Application;
import dao.MongoDao;
import ui.Menu;

import java.io.IOException;

public class Main{
    static String username;
    static String password;
    static String dbHost;
    static String redisHost;

    public static void main(String[] arg) throws IOException {
        //TODO: make DbObjects' toString() show collections of Ids as hex numbers, not 10-base numbers
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
        //Set<Game> games = dao.getAll(Game.class);

        Application.start(dao);
    }
}