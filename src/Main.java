
import dao.DatabaseAccessObject;
import entities.User;

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

        for (User u : dao.getAll(User.class))
            System.out.println(u.getName());

        User Nik = dao.getById(User.class, 1);

        System.out.println(Nik.getName() + "  ||| " + Nik.getId());

        // UI/menu code goes here
    }
}