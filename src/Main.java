import app.Application;
import dao.MongoDao;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Main{
    static final PrintStream err = System.err;
    static String dbHost;
    static String redisHost;

    static void toggleErrorStream() {
        if (System.err.equals(err))
            System.setErr(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {

                }
            }));
        else
            System.setErr(err);
    }

    public static void main(String[] arg) throws IOException {
        if(arg.length > 0) {
            dbHost=arg[0];
            if (arg.length > 1)
                redisHost = arg[1];
        }

        toggleErrorStream();
        MongoDao dao = MongoDao.getInstance(dbHost);
        toggleErrorStream();
        Application.start(dao);
    }
}