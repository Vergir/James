import dao.DatabaseAccessObject;
import dao.RedisEnhancedDao;
import dbobjects.entities.*;
import dbobjects.linkers.GamesDevelopers;
import dbobjects.linkers.GamesPublishers;
import dbobjects.linkers.TransactionsGames;
import dbobjects.linkers.UsersGames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Stanislav on 04.11.2016.
 * stas33553377@yandex.ru
 */
public class Application {

    private static String username;
    private static String password;
    private static Scanner reader = new Scanner(System.in);
    private static BufferedReader breader = new BufferedReader(new InputStreamReader(System.in));
    private static DatabaseAccessObject dao;
    private static long begin,end;

    public static void start(DatabaseAccessObject dao) throws IOException {
        boolean redisEnabled = RedisEnhancedDao.class.isAssignableFrom(dao.getClass());
        Application.dao = dao;
        int while_flag =1;
        int input=8, input_flag=1;
        while (while_flag == 1){
            do{
            System.out.println("Choose an option \n" +
                    "1. User \n" +
                    "2. Game \n" +
                    "3. Developer \n" +
                    "4. Publisher \n" +
                    "5. Comment \n" +
                    "6. Transaction \n" +
                    (redisEnabled ? "7. Redis Config\n" : "") +
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
                case 7:
                    if (redisEnabled)
                        callRedisConfig();
                    else
                        System.out.println("Please enter correct option");
                    break;
                case 0:
                    while_flag=0;
                    System.out.println("Thank you for using James!");
                    break;
                default:
                    System.out.println("Please enter a correct option");
                    break;
            }
        }
        System.exit(0);
    }

    //Entities menu
    private static void callUser() throws IOException {
        int input=0;
        System.out.println("Choose an option: \n" +
                "1. Get User by Id \n" +
                "2. Get user by Name \n" +
                "3. Create User \n" +
                "4. Delete User by Name \n" +
                "5. Update User by Id \n" +
                "6. Get all users \n" +
                "0. Back \n");
        try {
            input = reader.nextInt();
        }
        catch (Exception e)
        {
            System.out.println("Wrong input! Enter an integer number next time");
        }
        User u;
        int user_id;
        switch (input){
            case 1:
                System.out.println("Enter id of user:");
                try {
                    user_id = reader.nextInt();
                }
                catch (Exception e){
                    System.out.println("Wrong input! Enter an integer number next time.");
                    return;
                }
                startTimer();
                u = dao.getEntity(User.class,user_id);
                endTimer();
                if(u!=null) {
                    System.out.println(u.toString());
                    readUsersGames(u);
                }
                else
                    System.out.println("There is no User with this id");
                printTimer();
                break;
            case 2:
                System.out.println("Enter name of user:");
                String User_Name = breader.readLine();
                startTimer();
                u = dao.getByName(User.class,User_Name);
                if(u != null) {
                    System.out.println(u.toString());
                    readUsersGames(u);
                    endTimer();
                }
                else {
                    System.out.println("There is no User with this name \n");
                    endTimer();
                }
                printTimer();
                break;
            case 3:
                u = fillUser(null);
                dao.merge(u);
                if(u!=null && u.getId() != 0)
                    System.out.println("User inserted with id: "+u.getId()+"\n");
                else
                    System.out.println("User has not been inserted");
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
                u = dao.getEntity(User.class,user_id);
                u=fillUser(u);
                dao.merge(u);
                break;
            case 6:
                System.out.println("List of Users:");
                startTimer();
                Set<User> us = dao.getAll(User.class);
                endTimer();
                for(User u_i : us){
                    System.out.println("Id: "+u_i.getId()+" Nickname:"+u_i.getName());
                }
                printTimer();
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
                "6. Get all Games \n" +
                "0. Back \n");
        try {
            input = reader.nextInt();
        }
        catch (Exception e)
        {
            System.out.println("Wrong input");
        }
        Game g;
        GamesDevelopers gd = null;
        GamesPublishers gp = null;
        int game_id;
        switch (input){
            case 1:
                System.out.println("Enter id of Game:");
                try{
                    game_id = reader.nextInt();
                }
                catch (Exception e){
                    System.out.println("Wrong input! Enter an integer number next time.");
                    return;
                }
                startTimer();
                g = dao.getEntity(Game.class,game_id);
                if(g!=null) {
                    System.out.println(g.toString());
                    readGamesDevelopers(g);
                    readGamesPublishers(g);
                    endTimer();
                }
                else {
                    endTimer();
                    System.out.println("There is no Game with this id");
                }
                printTimer();
                break;
            case 2:
                System.out.println("Enter title of Game:");
                String game_Title = breader.readLine();
                startTimer();
                g = dao.getByName(Game.class,game_Title);
                if(g!=null) {
                    System.out.println(g.toString());
                    readGamesDevelopers(g);
                    readGamesPublishers(g);
                    endTimer();
                }
                else {
                    System.out.println("There is no Game with this id");
                    endTimer();
                }
                printTimer();
                break;
            case 3:
                g = fillGame(null);
                dao.merge(g);
                if (g != null && g.getId() != 0) {
                    gd = createGamesDevelopers(g);
                    gp = createGamesPublishers(g);
                }
                if(gp!=null)
                    dao.merge(gp);
                if(gd!=null)
                    dao.merge(gd);
                if(g!=null && g.getId() != 0)
                    System.out.println("Game inserted with id: "+g.getId()+"\n");
                else
                    System.out.println("Game has not been inserted");
                break;
            case 4:
                System.out.println("Enter name of Game:");
                String Name_del = breader.readLine();
                g=dao.getByName(Game.class,Name_del);
                deleteGamesDevelopers(g);
                deleteGamesPublishers(g);
                dao.delete(g);
                break;
            case 5:
                System.out.println("Enter id of Game:");
                game_id = reader.nextInt();
                g = dao.getEntity(Game.class,game_id);
                if(g==null) {
                    g = fillGame(null);
                }
                else {
                    g=fillGame(g);
                }
                dao.merge(g);
                gd = updateGamesDevelopers(g);
                gp = updateGamesPublishers(g);
                if(gp!=null)
                    dao.merge(gp);
                if(gd!=null)
                    dao.merge(gd);
                if(g!=null)
                    System.out.println("Game updated with id: "+g.getId()+"\n");
                else
                    System.out.println("Game has not been updated");
                break;
            case 6:
                System.out.println("List of Games:");
                startTimer();
                Set<Game> gs = dao.getAll(Game.class);
                endTimer();
                for(Game g_i : gs)
                    System.out.println("Id: "+g_i.getId()+" Title: "+g_i.getTitle());
                printTimer();
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
                "6. Get all Developers \n" +
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
                try{
                    dev_id = reader.nextInt();
                }
                catch (Exception e){
                    System.out.println("Wrong input! Enter an integer number next time.");
                    return;
                }
                startTimer();
                d = dao.getEntity(Developer.class,dev_id);
                if(d==null) {
                    endTimer();
                    System.out.println("There is no Developer with this id");
                }
                else {
                    System.out.println(d.toString());
                    readGamesDevelopers(d);
                    endTimer();
                }
                printTimer();
                break;
            case 2:
                System.out.println("Enter Name of Developer:");
                String dev_Name = breader.readLine();
                startTimer();
                d = dao.getByName(Developer.class,dev_Name);
                if(d==null) {
                    System.out.println("There is no Developer with this id");
                    endTimer();
                }
                else {
                    System.out.println(d.toString());
                    readGamesDevelopers(d);
                    endTimer();
                }
                printTimer();
                break;
            case 3:
                d=fillDeveloper(null);
                dao.merge(d);
                if(d!=null && d.getId() != 0)
                    System.out.println("Developer inserted with id: "+d.getId()+"\n");
                else
                    System.out.println("Developer has not been inserted");
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
                d = dao.getEntity(Developer.class,dev_id);
                d=fillDeveloper(d);
                dao.merge(d);
                if(d!=null)
                    System.out.println("Developer updated with id: "+d.getId()+"\n");
                else
                    System.out.println("Developer has not been updated");
                break;
            case 6:
                System.out.println("List of Developers:");
                startTimer();
                Set<Developer> ds = dao.getAll(Developer.class);
                endTimer();
                for(Developer d_i : ds)
                    System.out.println("Id: "+d_i.getId()+" Name: "+d_i.getName());
                printTimer();
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
                "6. Get all Publishers \n" +
                "0. Back \n");
        try {
            input = reader.nextInt();
        }
        catch (Exception e)
        {
            System.out.println("Wrong input");
        }
        Publisher p;
        int pub_id;
        switch (input){
            case 1:
                System.out.println("Enter id of Publisher:");
                try{
                    pub_id = reader.nextInt();
                }
                catch(Exception e){
                    System.out.println("Wrong input! Enter an integer number next time.");
                    return;
                }
                startTimer();
                p = dao.getEntity(Publisher.class,pub_id);
                endTimer();
                if(p==null)
                    System.out.println("There is no Publisher with this id");
                else {
                    System.out.println(p.toString());
                    readGamesPublishers(p);
                    endTimer();
                }
                printTimer();
                break;
            case 2:
                System.out.println("Enter Name of Publisher:");
                String pub_Name = breader.readLine();
                startTimer();
                p = dao.getByName(Publisher.class,pub_Name);
                if(p==null) {
                    endTimer();
                    System.out.println("There is no Publisher with this id");
                }
                else {
                    System.out.println(p.toString());
                    readGamesPublishers(p);
                    endTimer();
                }
                printTimer();
                break;
            case 3:
                p=fillPublisher(null);
                dao.merge(p);
                if(p!=null && p.getId() != 0)
                    System.out.println("Publisher inserted with id: "+p.getId()+"\n");
                else
                    System.out.println("Publisher has not been inserted");
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
                p = dao.getEntity(Publisher.class,pub_id);
                p=fillPublisher(p);
                dao.merge(p);
                if(p!=null)
                    System.out.println("Publisher updated with id: "+p.getId()+"\n");
                else
                    System.out.println("Publisher has not been updated");
                break;
            case 6:
                System.out.println("List of Publishers:");
                startTimer();
                Set<Publisher> ps = dao.getAll(Publisher.class);
                endTimer();
                for(Publisher p_i : ps)
                    System.out.println("Id: "+p_i.getId()+" Name: "+p_i.getName());
                printTimer();
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
                "5. Get all Comments \n" +
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
                try{
                    com_id = reader.nextInt();
                }
                catch (Exception e){
                    System.out.println("Wrong input! Enter an integer number next time.");
                    return;
                }
                startTimer();
                c = dao.getEntity(Comment.class,com_id);
                endTimer();
                if(c==null)
                    System.out.println("There is no Comment with this id");
                else
                    System.out.println(c.toString());
                printTimer();
                break;
            case 2:
                c=fillComment(null);
                dao.merge(c);
                if(c!=null && c.getId() != 0)
                    System.out.println("Comment inserted with id: "+c.getId()+"\n");
                else
                    System.out.println("Comment has not been inserted");
                break;
            case 3:
                System.out.println("Enter id of Comment:");
                int id_del = reader.nextInt();
                c=dao.getEntity(Comment.class,id_del);
                dao.delete(c);
                break;
            case 4:
                System.out.println("Enter id of Comment:");
                com_id = reader.nextInt();
                c = dao.getEntity(Comment.class,com_id);
                c=fillComment(c);
                dao.merge(c);
                if(c!=null)
                    System.out.println("Comment updated with id: "+c.getId()+"\n");
                else
                    System.out.println("Comment has not been updated");
                break;
            case 5:
                System.out.println("List of Comments:");
                startTimer();
                Set<Comment> cs = dao.getAll(Comment.class);
                endTimer();
                for(Comment c_i : cs)
                    System.out.println("Id: "+c_i.getId()+" User_id: "+c_i.getUser_id()+" Score: "+c_i.getScore());
                printTimer();
                break;
            case 0:
                break;
            default:
                System.out.println("Incorrect option");
                break;
        }
    }
    private static void callTransaction() throws IOException {
        int input=0;
        System.out.println("Choose an option: \n" +
                "1. Get Transaction by Id \n" +
                "2. Create Transaction \n" +
                "3. Update Transaction by Id \n" +
                "4. Get all Transactions \n" +
                "0. Back \n");
        try {
            input = reader.nextInt();
        }
        catch (Exception e)
        {
            System.out.println("Wrong input");
        }
        Transaction t;
        ArrayList<TransactionsGames> tgs;
        int tra_id;
        switch (input){
            case 1:
                System.out.println("Enter id of Transaction:");
                try{
                    tra_id = reader.nextInt();
                }
                catch(Exception e){
                    System.out.println("Wrong input! Enter an integer number next time.");
                    return;
                }
                startTimer();
                t = dao.getEntity(Transaction.class,tra_id);
                endTimer();
                if(t==null)
                    System.out.println("There is no Transaction with this id");
                else {
                    System.out.println(t.toString());
                    readTransactionGames(t);
                    endTimer();
                }
                printTimer();
                break;
            case 2:
                t = fillTransaction(null);
                dao.merge(t);
                tgs = createTransactionsGames(t);
                for(TransactionsGames tg : tgs)
                    dao.merge(tg);
                if(t!=null && t.getId() != 0)
                    System.out.println("Transaction inserted with id: "+t.getId()+"\n");
                else
                    System.out.println("Transaction has not been inserted");
                break;
            case 3:
                System.out.println("Enter id of Transaction:");
                tra_id = reader.nextInt();
                t = dao.getEntity(Transaction.class,tra_id);
                t=fillTransaction(t);
                dao.merge(t);
                tgs = updateTransactionGames(t);
                for(TransactionsGames tg : tgs)
                    dao.merge(tg);
                if(t!=null)
                    System.out.println("Transaction updated with id: "+t.getId()+"\n");
                else
                    System.out.println("Transaction has not been updated");
                break;
            case 4:
                System.out.println("List of Transactions:");
                startTimer();
                Set<Transaction> ts = dao.getAll(Transaction.class);
                endTimer();
                for(Transaction t_i : ts)
                    System.out.println("Id: "+t_i.getId()+" User_id: "+t_i.getUser_id()+" Sum: "+t_i.getSum());
                printTimer();
                break;
            case 0:
                break;
            default:
                System.out.println("Incorrect option");
                break;
        }
    }
    private static void callRedisConfig() throws IOException{
        RedisEnhancedDao red = (RedisEnhancedDao)dao;

        int input=0;
        System.out.println("Cache Expiration Time: "+red.getExpirationTime()+" minutes");
        System.out.println("Cache Size: "+red.getCacheSize() + " Key-Value Pairs");
        System.out.println("Choose an option: \n" +
                "1. Set Cache Expiration Time \n" +
                "2. Flush Cache \n" +
                "3. Output Cache Keys\n" +
                "0. Back \n");
        try {
            input = reader.nextInt();
        }
        catch (Exception e) {
            System.out.println("Wrong input");
        }
        int value;
        switch (input){
        case 1:
            System.out.println("Enter new Cache Expiration Time value (in minutes)");
            try{
                value = reader.nextInt();
            }
            catch(Exception e){
                System.out.println("Wrong input! Enter an integer number next time.");
                return;
            }
            red.setExpirationTime(value);
            System.out.println("Cache Expiration Timer: "+red.getExpirationTime() + " minutes");
            break;
        case 2:
            red.flushCache();
            System.out.println("Cache Flushed");
            break;
        case 3:
            System.out.println("###Cache Keys Start");
            for (String key : red.getAllKeys())
                System.out.println(key);
            System.out.println("###Cache Keys End");
            break;
        case 0:
            break;
        default:
            System.out.println("Incorrect option");
            break;
        }
    }

    // Entities
    private static User fillUser(User u) throws IOException {
        System.out.println("Enter parameters for User:");
        System.out.println("\nNickname: ");
        String user_nick = breader.readLine();
        System.out.println("\nFirst name: ");
        String user_first = breader.readLine();
        System.out.println("\nLast name: ");
        String user_last = breader.readLine();
        System.out.println("\nEmail name: ");
        String user_email = breader.readLine();
        if(u!=null) {
            u.setNickName(user_nick);
            u.setFirstName(user_first);
            u.setLastName(user_last);
            u.setEmail(user_email);
        }
        else
            u = new User(user_nick,user_first,user_last,user_email);
        return u;
    }
    private static Game fillGame(Game g) throws IOException {
        System.out.println("Enter parameters for Game:");
        System.out.println("\nTitle: ");
        String game_title = breader.readLine();
        System.out.println("\nDescription: ");
        String game_description = breader.readLine();
        System.out.println("\nPrice: ");
        double game_price;
        try{
            game_price = reader.nextDouble();
        }
        catch (Exception e){
            System.out.println("Error: "+e.getClass().getSimpleName()+"\n input double number next time (use '.' instead of ',')");
            return null;
        }
        System.out.println("\nProduct type (\"Game\",\"DLC\" ): ");
        String game_producttype = breader.readLine();
        if (game_producttype.toLowerCase().equals("game"))
            game_producttype = "Game";
        else if (game_producttype.toLowerCase().equals("dlc"))
            game_producttype = "DLC";
        else
            return null;
        if(g!=null){
            g.setPrice(game_price);
            g.setDescription(game_description);
            g.setProduct_type(game_producttype);
            g.setTitle(game_title);
        }
        else
            g = new Game(game_title,game_description,null,game_price,new Date(),game_producttype);
        return g;
    }
    private static Developer fillDeveloper(Developer d) throws IOException {
        System.out.println("Enter parameters for Developer:");
        System.out.println("\nName: ");
        String dev_name = breader.readLine();
        System.out.println("\nAddress: ");
        String dev_address = breader.readLine();
        System.out.println("\nEmail: ");
        String dev_email = breader.readLine();
        if(d!=null){
            d.setEmail(dev_email);
            d.setAddress(dev_address);
            d.setName(dev_name);
        }
        else
            d = new Developer(dev_name,dev_address,dev_email);
        return d;
    }
    private static Publisher fillPublisher(Publisher p) throws IOException {
        System.out.println("Enter parameters for Publisher:");
        System.out.println("\nName: ");
        String pub_name = breader.readLine();
        System.out.println("\nAddress: ");
        String pub_address = breader.readLine();
        System.out.println("\nEmail: ");
        String pub_email = breader.readLine();
        if(p!=null){
            p.setEmail(pub_email);
            p.setName(pub_name);
            p.setAddress(pub_address);
        }
        else
            p = new Publisher(pub_name,pub_address,pub_email);
        return p;
    }
    private static Comment fillComment(Comment c) throws IOException {
        System.out.println("Enter parameters for Comment:");
        System.out.println("\nUser_id: ");
        int com_user_id;
        try{
            com_user_id=reader.nextInt();
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage()+"\n input integer number");
            return null;
        }
        System.out.println("\nGame_id: ");
        int com_game_id;
        try{
            com_game_id=reader.nextInt();
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage()+"\n input integer number");
            return null;
        }
        System.out.println("\nScore: ");
        int com_score;
        try{
            com_score=reader.nextInt();
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage()+"\n input integer number");
            return null;
        }
        System.out.println("\nContent: ");
        String com_content = breader.readLine();
        if(c!=null){
            c.setContent(com_content);
            c.setGame_id(com_game_id);
            c.setUser_id(com_user_id);
            c.setScore(com_score);
        }
        else
            c = new Comment(com_user_id,com_game_id,com_score,com_content,new Date());
        return c;
    }
    private static Transaction fillTransaction(Transaction t){
        System.out.println("Enter parameters for Transaction:");
        System.out.println("\nUser_id: ");
        int tra_user_id;
        try{
            tra_user_id=reader.nextInt();
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage()+"\n input integer number");
            return null;
        }
        System.out.println("\nSum: ");
        int tra_sum;
        try{
            tra_sum=reader.nextInt();
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage()+"\n input integer number");
            return null;
        }
        if(t!=null){
            t.setUser_id(tra_user_id);
            t.setSum(tra_sum);
        }
        else
            t= new Transaction(tra_user_id,tra_sum,new Date());
        return t;
    }

    //Linker GamesDevelopers
    private static GamesDevelopers createGamesDevelopers(Game g) throws IOException {
        if(g==null)
            return null;
        System.out.println("\nDeveloper name: ");
        String dev_name = breader.readLine();
        Developer game_dev = dao.getByName(Developer.class,dev_name);
        if(game_dev != null) {
            GamesDevelopers gd = dao.getLinker(GamesDevelopers.class, g.getId(), game_dev.getId());
            if(gd==null) {
                gd = new GamesDevelopers();
                gd.setDeveloperId(game_dev.getId());
                gd.setGameId(g.getId());
                return gd;
            }
            return gd;
        }
        System.out.println("No Developers with this name");
        return null;
    }
    private static GamesDevelopers updateGamesDevelopers(Game g) throws IOException {
        if(g==null)
            return null;
        Set<GamesDevelopers> gds = dao.getAll(GamesDevelopers.class);
        GamesDevelopers gd = null;
        for(GamesDevelopers i : gds)
            if(i.getId1()==g.getId()){
                gd=i;
                break;
            }
        if(gd == null)
            return createGamesDevelopers(g);
        else{
            dao.delete(gd);
            System.out.println("\nDeveloper name: ");
            String dev_name = breader.readLine();
            Developer game_dev = dao.getByName(Developer.class,dev_name);
            if(game_dev == null)
                System.out.println("No Developers with this name");
            else
                gd.setDeveloperId(game_dev.getId());
            return gd;
        }
    }
    private static void readGamesDevelopers(Game g){
        if(g==null)
            return ;
        Set<GamesDevelopers> gds = dao.getAll(GamesDevelopers.class);
        GamesDevelopers gd = null;
        for(GamesDevelopers i : gds)
            if(i.getId1()==g.getId()){
                gd=i;
                break;
            }
        if(gd==null)
        {
            System.out.println("No dev assigned");
            return;
        }
        Developer dev = dao.getEntity(Developer.class,gd.getId2());
        System.out.println("Developer: "+dev.getName());
    }
    private static void readGamesDevelopers(Developer dev){
        if(dev==null)
            return ;
        System.out.println("List of games by this dev:");
        Set<GamesDevelopers> gds = dao.getAll(GamesDevelopers.class);
        if(gds.size() == 0){
            System.out.println("No games by this developer");
            return;
        }
        for(GamesDevelopers gd: gds){
            if(gd.getId2()==dev.getId()) {
                Game game = dao.getEntity(Game.class,gd.getId1());
                System.out.println(game.getName());
            }
        }
    }
    private static void deleteGamesDevelopers(Developer dev){
        Set<GamesDevelopers> gds = dao.getAll(GamesDevelopers.class);
        for(GamesDevelopers gd : gds){
            if(gd.getId2()==dev.getId())
                dao.delete(gd);
        }
    }
    private static void deleteGamesDevelopers(Game g){
        if(g==null)
            return ;
        Set<GamesDevelopers> gds = dao.getAll(GamesDevelopers.class);
        for(GamesDevelopers gd : gds){
            if(gd.getId1()==g.getId())
                dao.delete(gd);
        }
    }

    //Linker GamesPublishers
    private static GamesPublishers createGamesPublishers(Game g) throws IOException {
        if(g==null)
            return null;
        System.out.println("\nPublisher: ");
        String pub_name = breader.readLine();
        Publisher game_pub = dao.getByName(Publisher.class, pub_name);
        if (game_pub != null) {
            GamesPublishers gp = dao.getLinker(GamesPublishers.class,g.getId(),game_pub.getId());
            if(gp == null){
                gp=new GamesPublishers(g.getId(),game_pub.getId());
                return gp;
            }
            return gp;
        }
        System.out.println("no Publishers with this name");
        return null;
    }
    private static GamesPublishers updateGamesPublishers(Game g) throws IOException {
        if(g==null)
            return null;
        Set<GamesPublishers> gps = dao.getAll(GamesPublishers.class);
        GamesPublishers gp=null;
        for(GamesPublishers i : gps)
            if(i.getId1()==g.getId()) {
                gp = i;
                break;
            }
        if(gp==null)
            return createGamesPublishers(g);
        else{
            dao.delete(gp);
            System.out.println("\nPublisher name:");
            String pub_name=breader.readLine();
            Publisher game_pub = dao.getByName(Publisher.class,pub_name);
            if(game_pub==null){
                System.out.println("No Publishers with this name");
            }
            else
                gp.setPublisherId(game_pub.getId());
            return gp;
        }
    }
    private static void readGamesPublishers(Game g){
        if(g==null)
            return ;
        Set<GamesPublishers> gps = dao.getAll(GamesPublishers.class);
        GamesPublishers gp = null;
        for(GamesPublishers i : gps)
            if(i.getId1()==g.getId()){
                gp=i;
                break;
            }
        if(gp==null)
        {
            System.out.println("No pub assigned");
            return;
        }
        Publisher pub = dao.getEntity(Publisher.class,gp.getId2());
        System.out.println("Publisher: "+pub.getName());
    }
    private static void readGamesPublishers(Publisher pub){
        if(pub==null)
            return ;
        System.out.println("List of games by this pub:");
        Set<GamesPublishers> gps = dao.getAll(GamesPublishers.class);
        for(GamesPublishers gp: gps){
            if(gp.getId2()==pub.getId()) {
                Game game = dao.getEntity(Game.class,gp.getId1());
                System.out.println(game.getName());
            }
        }
    }
    private static void deleteGamesPublishers(Publisher pub){
        Set<GamesPublishers> gps = dao.getAll(GamesPublishers.class);
        if(gps.size() == 0){
            System.out.println("No games by this publishers");
            return;
        }
        for(GamesPublishers gp : gps){
            if(gp.getId2()==pub.getId())
                dao.delete(gp);
        }
    }
    private static void deleteGamesPublishers(Game g){
        if(g==null)
            return ;
        Set<GamesPublishers> gps = dao.getAll(GamesPublishers.class);
        for(GamesPublishers gp : gps){
            if(gp.getId1()==g.getId())
                dao.delete(gp);
        }
    }

    //Linker TransactionsGames
    private static ArrayList<TransactionsGames> createTransactionsGames(Transaction t) throws  IOException{
        if(t==null)
            return null;
        ArrayList<TransactionsGames> tgs = new ArrayList<>();
        System.out.println("Enter count of games in this transaction");
        int count = reader.nextInt();
        for(int i=0; i<count;i++) {
            System.out.println("Enter title of Game");
            String game_name = breader.readLine();
            Game g = dao.getByName(Game.class,game_name);
            if(g==null) {
                System.out.println("There is no Game with this title");
                continue;
            }
            TransactionsGames tg = new TransactionsGames(t.getId(),g.getId());
            UsersGames ug = new UsersGames(t.getUser_id(),g.getId());
            dao.merge(ug);
            tgs.add(tg);
        }
        return tgs;
    }
    private static ArrayList<TransactionsGames> updateTransactionGames(Transaction t) throws IOException{
        if(t==null) return null;
        Set<TransactionsGames> tgs = dao.getAll(TransactionsGames.class);
        readTransactionGames(t);
        ArrayList<TransactionsGames> new_tgs = new ArrayList<>();
        System.out.println("Enter count of update operations:");
        int count = reader.nextInt();
        for(int i=0;i<count;i++){
            System.out.println("Enter title of Game from list above:");
            String game_name = breader.readLine();
            Game g = dao.getByName(Game.class,game_name);
            if(g==null)
                System.out.println("No game with that title");
            else{
                TransactionsGames tg = new TransactionsGames();
                for(TransactionsGames j : tgs){
                    if(j.getId1() == t.getId() && j.getId2()==g.getId()) {
                        dao.delete(j);
                        UsersGames ug = new UsersGames(t.getUser_id(),j.getGameId());
                        dao.delete(ug);
                    }
                }
                tg.setGameId(g.getId());
                tg.setTransactionId(t.getId());
                UsersGames ug = new UsersGames(t.getUser_id(),g.getId());
                dao.merge(ug);
                new_tgs.add(tg);
            }
        }
        return new_tgs;
    }
    private static void readTransactionGames(Transaction t){
        if(t==null)
            return ;
        Set<TransactionsGames> tgs = dao.getAll(TransactionsGames.class);
        if(tgs.size() == 0){
            System.out.println("No games in this transaction 0_o");
            return;
        }
        System.out.println("List of games on this transaction:");
        for(TransactionsGames i : tgs){
            if(i.getId1() == t.getId()) {
                Game g = dao.getEntity(Game.class,i.getId2());
                if(g!=null)
                    System.out.println("\t" +g.getName());
            }
        }
    }

    //Linker UsersGames
    // createUsersGames unnecessary -> put into createTransactionGames
    // updateUsersGames unnecessary -> put into updateTransactionGame
    private static void readUsersGames(User u){
        if(u==null)
            return;
        Set<UsersGames> ugs = dao.getAll(UsersGames.class);
        if(ugs.size() == 0){
            System.out.println("No games owned by this user");
            return;
        }
        System.out.println("List of games in this user's library:");
        for(UsersGames i : ugs)
            if (i.getId1() == u.getId()) {
                Game g = dao.getEntity(Game.class, i.getId2());
                if (g != null)
                    System.out.println("\t" + g.getName());
            }
    }

    private static void startTimer(){
        begin=System.currentTimeMillis();
    }
    private static void endTimer(){
        end=System.currentTimeMillis();
    }
    private static void printTimer(){
        System.out.println("Time spent in millis: "+(end-begin));
    }
    private static void clearTimer(){
        begin=0;
        end=0;
    }
    private static void setUsername(String username) {
        Application.username = username;
    }
    private static void setPassword(String password) {
        Application.password = password;
    }
    public static String getUsername() {
        return username;
    }
    public static String getPassword() {
        return password;
    }
}
