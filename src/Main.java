import dao.DatabaseAccessObject;
import dbobjects.DbObject;
import dbobjects.linkers.GamesDevelopers;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

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

        DatabaseAccessObject dao = DatabaseAccessObject.getInstance(username, password);

        Set<GamesDevelopers> gds = dao.getAll(GamesDevelopers.class);

        GamesDevelopers gd = (GamesDevelopers) gds.toArray()[0];

        gd.setDeveloperId(6);

        dao.delete(gd);

        Application.start(username,password);

    }
}