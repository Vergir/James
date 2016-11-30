import ui.Menu;

import java.io.IOException;

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
        //MongoDao dao = MongoDao.getInstance(username,password, dbHost);
        //Set<Game> games = dao.getAll(Game.class);

        Menu lul = new Menu("LMAO", null, null, true);
        lul.addItem("nope", (x) -> {System.out.print("nope"); return null;});
        lul.addItem("yeah", (x) -> {System.out.print("yeah"); return null;});
        lul.start();
    }
}