import dao.BareboneDao;
import dao.RedisEnhancedDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main{
    static String username;
    static String password;

    public static void main(String[] arg) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        if(arg.length == 2) {
            username=arg[0];
            password=arg[1];
        }
        else {
            System.out.println("Enter username/password :");
            username = br.readLine();
            password = br.readLine();
        }

        RedisEnhancedDao dao = RedisEnhancedDao.getInstance(username, password, "46.101.212.60");
        //BareboneDao dao = BareboneDao.getInstance(username,password);
        Application.start(dao);
    }
}