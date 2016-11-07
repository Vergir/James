import dao.DatabaseAccessObject;
import dbobjects.DbObject;
import dbobjects.linkers.GamesDevelopers;
import dbobjects.linkers.Linker;

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
        
        Application.start(username,password);

    }
}