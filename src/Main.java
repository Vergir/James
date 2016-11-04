import dao.DatabaseAccessObject;
import entities.*;
import ui.UserInterface;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main{
    static String username;
    static String password;

    public static void main(String[] arg) throws IOException {
        //if (arg.length != 2) {
        //    System.err.println("2 arguments are necessary: username and password");
        //    return;
        //}
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        if(arg.length == 2 && arg[0] != null && arg[1] !=null)
        {
            username=arg[0];
            password=arg[1];
        }
        else {
            System.out.println("Enter username/password :");
            username = br.readLine();
            password = br.readLine();
        }
        //DatabaseAccessObject dao = DatabaseAccessObject.getInstance(username, password);
        //System.out.println(dao.getSeqValue("Comment"));
        // UI/menu code goes here
        UserInterface.start(username,password);

    }
}