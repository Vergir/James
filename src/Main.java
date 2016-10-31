import com.jcraft.jsch.*;
import dao.DatabaseAccessObject;
import entities.Developer;

import javax.xml.crypto.Data;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.System.in;


public class Main{

    static String username;
    static String password;

    public static void main(String[] arg) {
        if (arg.length != 2) {
            System.err.println("2 arguments are necessary: username and password");
            return;
        }
        username = arg[0];
        password = arg[1];

        DatabaseAccessObject dao = DatabaseAccessObject.getInstance(username, password);

        for (Developer d : dao.getAll(Developer.class))
            System.out.println(d.getName());
    }
}