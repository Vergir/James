package ui;
import dao.DatabaseAccessObject;
import entities.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by Stanislav on 04.11.2016.
 * stas33553377@yandex.ru
 */
public class UserInterface {

    private static String username;
    private static String password;
    private static Scanner reader = new Scanner(System.in);
    private static BufferedReader breader = new BufferedReader(new InputStreamReader(System.in));
    private static DatabaseAccessObject dao ;
    private static java.sql.Date today = new java.sql.Date(new java.util.Date().getTime());

    public static void start(String u, String pw) throws IOException {
        setUsername(u);
        setPassword(pw);
        dao = DatabaseAccessObject.getInstance(username,password);
        int while_flag =1;
        int input=7, input_flag=1;
        while (while_flag == 1){
            do{
            System.out.println("Choose an option \n" +
                    "1. User \n" +
                    "2. Game \n" +
                    "3. Developer \n" +
                    "4. Publisher \n" +
                    "5. Comment \n" +
                    "6. Transaction \n" +
                    "0. Exit \n" );
                try {
                    input_flag=0;
                    input = reader.nextInt();
                }
                catch (Exception e) {
                    input_flag =1;
                    reader.nextLine();
                    System.out.println("Wrong input");
                }
            }while(input_flag ==1);
            switch (input){
                case 1:
                    callUser();
                    break;
                case 2:
                    callGame();
                    break;
                case 3 :
                    callDeveloper();
                    break;
                case 4:
                    callPublisher();
                    break;
                case 5:
                    callComment();
                    break;
                case 6:
                    callTransaction();
                    break;
                case 0:
                    while_flag=0;
                    System.out.println("Thank you for using James!");
                    break;
                default:
                    System.out.println("Please, enter a correct option" +
                            "\n P.S. a number from 1 to 6 \n");
                    break;
            }
        }
    }
    private static void callUser() throws IOException {
        int input=0;
        System.out.println("Choose an option: \n" +
                "1. Get User by Id \n" +
                "2. Get user by Name \n" +
                "3. Create User \n" +
                "4. Delete User by Name \n" +
                "5. Update User by Id" +
                "0. Back \n");
        try {
            input = reader.nextInt();
        }
        catch (Exception e)
        {
            System.out.println("Wrong input");
        }
        User u;
        int user_id;
        switch (input){
            case 1:
                System.out.println("Enter id of user:");
                user_id = reader.nextInt();
                u = dao.getById(User.class,user_id);
                if(u!=null)
                    System.out.println(u.toString());
                else
                    System.out.println("There is no User with this id");
                break;
            case 2:
                System.out.println("Enter name of user:");
                String User_Name = breader.readLine();
                u = dao.getByName(User.class,User_Name);
                if(u != null)
                    System.out.println(u.toString());
                else
                    System.out.println("There is no User with this name \n");
                break;
            case 3:
                u = createUser();
                dao.merge(u);
                System.out.println("\n");
                break;
            case 4:
                System.out.println("Enter name of user:");
                String Name_del = breader.readLine();
                u=dao.getByName(User.class,Name_del);
                dao.delete(u);
                System.out.println("\n");
                break;
            case 5:
                System.out.println("Enter id of user:");
                user_id = reader.nextInt();
                u = dao.getById(User.class,user_id);
                if(u==null)
                    u=createUser();
                else {
                    int id=u.getId();
                    u=createUser();
                    u.setId(id);
                }
                dao.merge(u);
                System.out.println("\n");
                break;
            case 0:
                break;
            default:
                System.out.println("Incorrect option");
                break;
        }
    }
    private static void callGame() throws IOException {
        int input=0;
        System.out.println("Choose an option: \n" +
                "1. Get Game by Id \n" +
                "2. Get Game by Name \n" +
                "3. Create Game \n" +
                "4. Delete Game by Name \n" +
                "5. Update Game by Id \n" +
                "0. Back \n");
        try {
            input = reader.nextInt();
        }
        catch (Exception e)
        {
            System.out.println("Wrong input");
        }
        Game g;
        int game_id;
        switch (input){
            case 1:
                System.out.println("Enter id of Game:");
                game_id = reader.nextInt();
                g = dao.getById(Game.class,game_id);
                if(g!=null)
                    System.out.println(g.toString());
                else
                    System.out.println("There is no User with this id");
                break;
            case 2:
                System.out.println("Enter title of Game:");
                String game_Title = breader.readLine();
                g = dao.getByName(Game.class,game_Title);
                if(g!=null)
                    System.out.println(g.toString());
                else
                    System.out.println("There is no User with this id");
                break;
            case 3:
                g = createGame();
                dao.merge(g);
                break;
            case 4:
                System.out.println("Enter name of Game:");
                String Name_del = breader.readLine();
                g=dao.getByName(Game.class,Name_del);
                dao.delete(g);
                break;
            case 5:
                System.out.println("Enter id of Game:");
                game_id = reader.nextInt();
                g = dao.getById(Game.class,game_id);
                if(g==null)
                    g=createGame();
                else {
                    int id=g.getId();
                    g=createGame();
                    g.setId(id);
                }
                dao.merge(g);
                System.out.println("\n");
                break;
            case 0:
                break;
            default:
                System.out.println("Incorrect option");
                break;
        }
    }
    private static void callDeveloper() throws IOException {
        int input=0;
        System.out.println("Choose an option: \n" +
                "1. Get Developer by Id \n" +
                "2. Get Developer by Name \n" +
                "3. Create Developer \n" +
                "4. Delete Developer by Name \n" +
                "5. Update Developer by Id \n" +
                "0. Back \n");
        try {
            input = reader.nextInt();
        }
        catch (Exception e)
        {
            System.out.println("Wrong input");
        }
        Developer d;
        int dev_id;
        switch (input){
            case 1:
                System.out.println("Enter id of Developer:");
                dev_id = reader.nextInt();
                d = dao.getById(Developer.class,dev_id);
                if(d==null)
                    System.out.println("There is no Developer with this id");
                else
                    System.out.println(d.toString());
                break;
            case 2:
                System.out.println("Enter Name of Developer:");
                String dev_Name = breader.readLine();
                d = dao.getByName(Developer.class,dev_Name);
                if(d==null)
                    System.out.println("There is no Developer with this id");
                else
                    System.out.println(d.toString());
                break;
            case 3:
                d=createDeveloper();
                dao.merge(d);
                break;
            case 4:
                System.out.println("Enter name of Developer:");
                String Name_del = breader.readLine();
                d=dao.getByName(Developer.class,Name_del);
                dao.delete(d);
                break;
            case 5:
                System.out.println("Enter id of Developer:");
                dev_id = reader.nextInt();
                d = dao.getById(Developer.class,dev_id);
                if(d==null)
                    d=createDeveloper();
                else {
                    int id=d.getId();
                    d=createDeveloper();
                    d.setId(id);
                }
                dao.merge(d);
                System.out.println("\n");
                break;
            case 0:
                break;
            default:
                System.out.println("Incorrect option");
                break;
        }
    }
    private static void callPublisher() throws IOException {
        int input=0;
        System.out.println("Choose an option: \n" +
                "1. Get Publisher by Id \n" +
                "2. Get Publisher by Name \n" +
                "3. Create Publisher \n" +
                "4. Delete Publisher by Name \n" +
                "5. Update Publisher by Id \n" +
                "0. Back \n");
        try {
            input = reader.nextInt();
        }
        catch (Exception e)
        {
            System.out.println("Wrong input");
        }
        Publisher p;
        switch (input){
            case 1:
                System.out.println("Enter id of Publisher:");
                int pub_id = reader.nextInt();
                p = dao.getById(Publisher.class,pub_id);
                if(p==null)
                    System.out.println("There is no Publisher with this id");
                else
                    System.out.println(p.toString());
                break;
            case 2:
                System.out.println("Enter Name of Publisher:");
                String pub_Name = breader.readLine();
                p = dao.getByName(Publisher.class,pub_Name);
                if(p==null)
                    System.out.println("There is no Publisher with this id");
                else
                    System.out.println(p.toString());
                break;
            case 3:
                p=createPublisher();
                dao.merge(p);
                break;
            case 4:
                System.out.println("Enter name of Publisher:");
                String Name_del = breader.readLine();
                p=dao.getByName(Publisher.class,Name_del);
                dao.delete(p);
                break;
            case 5:
                System.out.println("Enter id of Publisher:");
                pub_id = reader.nextInt();
                p = dao.getById(Publisher.class,pub_id);
                if(p==null)
                    p=createPublisher();
                else {
                    int id=p.getId();
                    p=createPublisher();
                    p.setId(id);
                }
                dao.merge(p);
                System.out.println("\n");
                break;
            case 0:
                break;
            default:
                System.out.println("Incorrect option");
                break;
        }
    }
    private static void callComment() throws IOException {
        int input=0;
        System.out.println("Choose an option: \n" +
                "1. Get Comment by Id \n" +
                "2. Create Comment \n" +
                "3. Delete Comment by Id \n" +
                "4. Update Comment by Id \n" +
                "0. Back \n");
        try {
            input = reader.nextInt();
        }
        catch (Exception e)
        {
            System.out.println("Wrong input");
        }
        Comment c;
        int com_id;
        switch (input){
            case 1:
                System.out.println("Enter id of Comment:");
                com_id = reader.nextInt();
                c = dao.getById(Comment.class,com_id);
                if(c==null)
                    System.out.println("There is no Comment with this id");
                else
                    System.out.println(c.toString());
                break;
            case 2:
                c=createComment();
                dao.merge(c);
                break;
            case 3:
                System.out.println("Enter id of Comment:");
                int id_del = reader.nextInt();
                c=dao.getById(Comment.class,id_del);
                dao.delete(c);
                break;
            case 5:
                System.out.println("Enter id of Comment:");
                com_id = reader.nextInt();
                c = dao.getById(Comment.class,com_id);
                if(c==null)
                    c=createComment();
                else {
                    int id=c.getId();
                    c=createComment();
                    c.setId(id);
                }
                dao.merge(c);
                System.out.println("\n");
                break;
            case 0:
                break;
            default:
                System.out.println("Incorrect option");
                break;
        }
    }
    private static void callTransaction(){
        int input=0;
        System.out.println("Choose an option: \n" +
                "1. Get Transaction by Id \n" +
                "2. Create Transaction \n" +
                "3. Delete Transaction by Id \n" +
                "4. Update Transaction by Id \n" +
                "0. Back \n");
        try {
            input = reader.nextInt();
        }
        catch (Exception e)
        {
            System.out.println("Wrong input");
        }
        Transaction t;
        int tra_id;
        switch (input){
            case 1:
                System.out.println("Enter id of Transaction:");
                tra_id = reader.nextInt();
                t = dao.getById(Transaction.class,tra_id);
                if(t==null)
                    System.out.println("There is no Transaction with this id");
                else
                    System.out.println(t.toString());
                break;
            case 2:
                t = createTransaction();
                dao.merge(t);
                break;
            case 3:
                System.out.println("Enter id of Transaction:");
                int id_del = reader.nextInt();
                t=dao.getById(Transaction.class,id_del);
                dao.delete(t);
                break;
            case 4:
                System.out.println("Enter id of Transaction:");
                tra_id = reader.nextInt();
                t = dao.getById(Transaction.class,tra_id);
                if(t==null)
                    t=createTransaction();
                else {
                    int id=t.getId();
                    t=createTransaction();
                    t.setId(id);
                }
                dao.merge(t);
                System.out.println("\n");
                break;
            case 0:
                break;
            default:
                System.out.println("Incorrect option");
                break;
        }
    }

    private static User createUser() throws IOException {
        System.out.println("Enter parameters for User:");
        System.out.println("\nNickname: ");
        String user_nick = breader.readLine();
        System.out.println("\nFirst name: ");
        String user_first = breader.readLine();
        System.out.println("\nLast name: ");
        String user_last = breader.readLine();
        System.out.println("\nEmail name: ");
        String user_email = breader.readLine();

        return new User(user_nick,user_first,user_last,user_email);
    }
    private static Game createGame() throws IOException {
        System.out.println("Enter parameters for Game:");
        System.out.println("\nTitle: ");
        String game_title = breader.readLine();
        System.out.println("\nDescription: ");
        String game_description = breader.readLine();
        System.out.println("\n Price: ");
        int game_price = reader.nextInt();
        System.out.println("\nProduct type (\"Game\",\"DLC\" ): ");
        String game_producttype = breader.readLine();
        return new Game(game_title,game_description,null,game_price,today,game_producttype);
    }
    private static Developer createDeveloper() throws IOException {
        System.out.println("Enter parameters for Developer:");
        System.out.println("\nName: ");
        String dev_name = breader.readLine();
        System.out.println("\nAddress: ");
        String dev_address = breader.readLine();
        System.out.println("\nEmail: ");
        String dev_email = breader.readLine();
        return new Developer(dev_name,dev_address,dev_email);
    }
    private static Publisher createPublisher() throws IOException {
        System.out.println("Enter parameters for Publisher:");
        System.out.println("\nName: ");
        String pub_name = breader.readLine();
        System.out.println("\nAddress: ");
        String pub_address = breader.readLine();
        System.out.println("\nEmail: ");
        String pub_email = breader.readLine();
        return new Publisher(pub_name,pub_address,pub_email);
    }
    private static Comment createComment() throws IOException {
        System.out.println("Enter parameters for Comment:");
        System.out.println("\nUser_id: ");
        int com_user_id = reader.nextInt();
        System.out.println("\nGame_id: ");
        int com_game_id = reader.nextInt();
        System.out.println("\nScore: ");
        int com_score = reader.nextInt();
        System.out.println("\nContent: ");
        String com_content = breader.readLine();
        return new Comment(com_user_id,com_game_id,com_score,com_content,today);
    }
    private static Transaction createTransaction(){
        System.out.println("Enter parameters for Transaction:");
        System.out.println("\nUser_id: ");
        int tra_user_id = reader.nextInt();
        System.out.println("\nSum: ");
        int tra_sum = reader.nextInt();
        return new Transaction(tra_user_id,tra_sum,today);
    }

    public static void setUsername(String username) {
        UserInterface.username = username;
    }
    public static void setPassword(String password) {
        UserInterface.password = password;
    }
    public static String getUsername() {
        return username;
    }
    public static String getPassword() {
        return password;
    }
}
