
import dao.DatabaseAccessObject;
import entities.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;



public class Main{

    static String username;
    static String password;

    public static void main(String[] arg) throws IOException {
        //if (arg.length != 2) {
        //    System.err.println("2 arguments are necessary: username and password");
        //    return;
        //}
        Scanner reader = new Scanner(System.in);
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
        DatabaseAccessObject dao = DatabaseAccessObject.getInstance(username, password);

        // UI/menu code goes here

        int flag =1;
        while(flag==1){
            System.out.println("Choose an option \n" +
                    "1. User \n" +
                    "2. Game \n" +
                    "3. Developer \n" +
                    "4. Publisher \n" +
                    "5. Comment \n" +
                    "6. Transaction \n" +
                    "0. Exit \n" );
            int input = reader.nextInt();
            String Name_del;
            java.sql.Date today = new java.sql.Date(new java.util.Date().getTime());
            switch (input){
                case 1 :
                    System.out.println("Choose an option: \n" +
                            "1. Get User by Id \n" +
                            "2. Get user by Name \n" +
                            "3. Create User \n" +
                            "4. Delete User by Name \n" +
                            "0. Back \n");
                    input = reader.nextInt();
                    User u;
                    switch (input){
                        case 1:
                            System.out.println("Enter id of user:");
                            int user_id = reader.nextInt();
                            u = dao.getById(User.class,user_id);
                            System.out.println(u.toString());
                            break;
                        case 2:
                            System.out.println("Enter name of user:");
                            String User_Name = br.readLine();
                            u = dao.getByName(User.class,User_Name);
                            System.out.println(u.toString());
                            break;
                        case 3:
                            System.out.println("Enter parameters for User:");
                            System.out.println("\nNickname: ");
                            String user_nick = br.readLine();
                            System.out.println("\nFirst name: ");
                            String user_first = br.readLine();
                            System.out.println("\nLast name: ");
                            String user_last = br.readLine();
                            System.out.println("\nEmail name: ");
                            String user_email = br.readLine();

                            u=new User(user_nick,user_first,user_last,user_email, today,0);
                            dao.merge(u);
                            break;
                        case 4:
                            System.out.println("Enter name of user:");
                            Name_del = br.readLine();
                            u=dao.getByName(User.class,Name_del);
                            dao.delete(u);
                            break;
                        case 0:
                            continue;
                        default:
                            System.out.println("Incorrect option");
                            break;
                    }
                    break;
                case 2:
                    System.out.println("Choose an option: \n" +
                            "1. Get Game by Id \n" +
                            "2. Get Game by Name \n" +
                            "3. Create Game \n" +
                            "4. Delete Game by Name \n" +
                            "0. Back \n");
                    input = reader.nextInt();
                    Game g;
                    switch (input){
                        case 1:
                            System.out.println("Enter id of Game:");
                            int game_id = reader.nextInt();
                            g = dao.getById(Game.class,game_id);
                            System.out.println(g.toString());
                            break;
                        case 2:
                            System.out.println("Enter title of Game:");
                            String game_Title = br.readLine();
                            g = dao.getByName(Game.class,game_Title);
                            System.out.println(g.toString());
                            break;
                        case 3:
                            System.out.println("Enter parameters for Game:");
                            System.out.println("\nTitle: ");
                            String game_title = br.readLine();
                            System.out.println("\nDescription: ");
                            String game_description = br.readLine();
                            System.out.println("\n Price: ");
                            int game_price = reader.nextInt();
                            System.out.println("\nProduct type (\"Game\",\"DLC\" ): ");
                            String game_producttype = br.readLine();
                            g=new Game(game_title,game_description,null,game_price,today,game_producttype);
                            dao.merge(g);
                            break;
                        case 4:
                            System.out.println("Enter name of Game:");
                            Name_del = br.readLine();
                            u=dao.getByName(User.class,Name_del);
                            dao.delete(u);
                            break;
                        case 0:
                            continue;
                        default:
                            System.out.println("Incorrect option");
                            break;
                    }
                    break;
                case 3:
                    System.out.println("Choose an option: \n" +
                            "1. Get Developer by Id \n" +
                            "2. Get Developer by Name \n" +
                            "3. Create Developer \n" +
                            "4. Delete Developer by Name \n" +
                            "0. Back \n");
                    input = reader.nextInt();
                    Developer d;
                    switch (input){
                        case 1:
                            System.out.println("Enter id of Developer:");
                            int dev_id = reader.nextInt();
                            d = dao.getById(Developer.class,dev_id);
                            System.out.println(d.toString());
                            break;
                        case 2:
                            System.out.println("Enter Name of Developer:");
                            String dev_Name = br.readLine();
                            d = dao.getByName(Developer.class,dev_Name);
                            System.out.println(d.toString());
                            break;
                        case 3:
                            System.out.println("Enter parameters for Developer:");
                            System.out.println("\nName: ");
                            String dev_name = br.readLine();
                            System.out.println("\nAddress: ");
                            String dev_address = br.readLine();
                            System.out.println("\nEmail: ");
                            String dev_email = br.readLine();
                            d=new Developer(dev_name,dev_address,dev_email);
                            dao.merge(d);
                            break;
                        case 4:
                            System.out.println("Enter name of Developer:");
                            Name_del = br.readLine();
                            d=dao.getByName(Developer.class,Name_del);
                            dao.delete(d);
                            break;
                        case 0:
                            continue;
                        default:
                            System.out.println("Incorrect option");
                            break;
                    }
                    break;
                case 4:
                    System.out.println("Choose an option: \n" +
                            "1. Get Publisher by Id \n" +
                            "2. Get Publisher by Name \n" +
                            "3. Create Publisher \n" +
                            "4. Delete Publisher by Name \n" +
                            "0. Back \n");
                    input = reader.nextInt();
                    Publisher p;
                    switch (input){
                        case 1:
                            System.out.println("Enter id of Publisher:");
                            int pub_id = reader.nextInt();
                            p = dao.getById(Publisher.class,pub_id);
                            System.out.println(p.toString());
                            break;
                        case 2:
                            System.out.println("Enter Name of Publisher:");
                            String pub_Name = br.readLine();
                            p = dao.getByName(Publisher.class,pub_Name);
                            System.out.println(p.toString());
                            break;
                        case 3:
                            System.out.println("Enter parameters for Publisher:");
                            System.out.println("\nName: ");
                            String pub_name = br.readLine();
                            System.out.println("\nAddress: ");
                            String pub_address = br.readLine();
                            System.out.println("\nEmail: ");
                            String pub_email = br.readLine();
                            p=new Publisher(pub_name,pub_address,pub_email);
                            dao.merge(p);
                            break;
                        case 4:
                            System.out.println("Enter name of Comment:");
                            Name_del = br.readLine();
                            p=dao.getByName(Publisher.class,Name_del);
                            dao.delete(p);
                            break;
                        case 0:
                            continue;
                        default:
                            System.out.println("Incorrect option");
                            break;
                    }
                    break;
                case 5:
                    System.out.println("Choose an option: \n" +
                            "1. Get Comment by Id \n" +
                            "2. Create Comment \n" +
                            "3. Delete Comment by Id \n" +
                            "0. Back \n");
                    input = reader.nextInt();
                    Comment c;
                    switch (input){
                        case 1:
                            System.out.println("Enter id of Comment:");
                            int com_id = reader.nextInt();
                            c = dao.getById(Comment.class,com_id);
                            System.out.println(c.toString());
                            break;
                        case 2:
                            System.out.println("Enter parameters for Comment:");
                            System.out.println("\nUser_id: ");
                            int com_user_id = reader.nextInt();
                            System.out.println("\nGame_id: ");
                            int com_game_id = reader.nextInt();
                            System.out.println("\nScore: ");
                            int com_score = reader.nextInt();
                            System.out.println("\nContent: ");
                            String com_content = br.readLine();
                            c=new Comment(com_user_id,com_game_id,com_score,com_content,today);
                            dao.merge(c);
                            break;
                        case 3:
                            System.out.println("Enter id of Comment:");
                            int id_del = reader.nextInt();
                            c=dao.getById(Comment.class,id_del);
                            dao.delete(c);
                            break;
                        case 0:
                            continue;
                        default:
                            System.out.println("Incorrect option");
                            break;
                    }
                    break;
                case 6:
                    System.out.println("Choose an option: \n" +
                            "1. Get Transactiont by Id \n" +
                            "2. Create Transaction \n" +
                            "3. Delete Transaction by Id \n" +
                            "0. Back \n");
                    input = reader.nextInt();
                    Transaction t;
                    switch (input){
                        case 1:
                            System.out.println("Enter id of Transaction:");
                            int tra_id = reader.nextInt();
                            t = dao.getById(Transaction.class,tra_id);
                            System.out.println(t.toString());
                            break;
                        case 2:
                            System.out.println("Enter parameters for Transaction:");
                            System.out.println("\nUser_id: ");
                            int tra_user_id = reader.nextInt();
                            System.out.println("\nSum: ");
                            int tra_sum = reader.nextInt();
                            t=new Transaction(tra_user_id,tra_sum,today);
                            dao.merge(t);
                            break;
                        case 3:
                            System.out.println("Enter id of Transaction:");
                            int id_del = reader.nextInt();
                            t=dao.getById(Transaction.class,id_del);
                            dao.delete(t);
                            break;
                        case 0:
                            continue;
                        default:
                            System.out.println("Incorrect option");
                            break;
                    }
                    break;
                case 0:
                    flag=0;
                    System.out.println("thank you for using James!");
                    break;
                default:
                    System.out.println("Please, enter a correct option" +
                            "\n P.S. a number from 1 to 6 \n");
                    break;
            }
        }
    }
}