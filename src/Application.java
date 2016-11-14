import dao.BareboneDao;
import dao.DatabaseAccessObject;
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

    public static void start(DatabaseAccessObject dao) throws IOException {
        Application.dao = dao;
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

    //Entities menu
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
                u = dao.getEntity(User.class,user_id);
                if(u!=null) {
                    System.out.println(u.toString());
                    readUsersGames(u);
                }
                else
                    System.out.println("There is no User with this id");
                break;
            case 2:
                System.out.println("Enter name of user:");
                String User_Name = breader.readLine();
                u = dao.getByName(User.class,User_Name);
                if(u != null) {
                    System.out.println(u.toString());
                    readUsersGames(u);
                }
                else
                    System.out.println("There is no User with this name \n");
                break;
            case 3:
                u = fillUser(null);
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
                u = dao.getEntity(User.class,user_id);
                u=fillUser(u);
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
        GamesDevelopers gd;
        GamesPublishers gp;
        int game_id;
        switch (input){
            case 1:
                System.out.println("Enter id of Game:");
                game_id = reader.nextInt();
                g = dao.getEntity(Game.class,game_id);
                if(g!=null) {
                    System.out.println(g.toString());
                    readGamesDevelopers(g);
                    readGamesPublishers(g);
                }
                else
                    System.out.println("There is no User with this id");
                break;
            case 2:
                System.out.println("Enter title of Game:");
                String game_Title = breader.readLine();
                g = dao.getByName(Game.class,game_Title);
                if(g!=null) {
                    System.out.println(g.toString());
                    readGamesDevelopers(g);
                    readGamesPublishers(g);
                }
                else
                    System.out.println("There is no User with this id");
                break;
            case 3:
                g = fillGame(null);
                dao.merge(g);
                gd = createGamesDevelopers(g);
                gp = createGamesPublishers(g);
                if(gp!=null)
                    dao.merge(gp);
                if(gd!=null)
                    dao.merge(gd);
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
                d = dao.getEntity(Developer.class,dev_id);
                if(d==null)
                    System.out.println("There is no Developer with this id");
                else {
                    System.out.println(d.toString());
                    readGamesDevelopers(d);
                }
                break;
            case 2:
                System.out.println("Enter Name of Developer:");
                String dev_Name = breader.readLine();
                d = dao.getByName(Developer.class,dev_Name);
                if(d==null)
                    System.out.println("There is no Developer with this id");
                else {
                    System.out.println(d.toString());
                    readGamesDevelopers(d);
                }
                break;
            case 3:
                d=fillDeveloper(null);
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
                d = dao.getEntity(Developer.class,dev_id);
                d=fillDeveloper(d);
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
                p = dao.getEntity(Publisher.class,pub_id);
                if(p==null)
                    System.out.println("There is no Publisher with this id");
                else {
                    System.out.println(p.toString());
                    readGamesPublishers(p);
                }
                break;
            case 2:
                System.out.println("Enter Name of Publisher:");
                String pub_Name = breader.readLine();
                p = dao.getByName(Publisher.class,pub_Name);
                if(p==null)
                    System.out.println("There is no Publisher with this id");
                else {
                    System.out.println(p.toString());
                    readGamesPublishers(p);
                }
                break;
            case 3:
                p=fillPublisher(null);
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
                p = dao.getEntity(Publisher.class,pub_id);
                p=fillPublisher(p);
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
                c = dao.getEntity(Comment.class,com_id);
                if(c==null)
                    System.out.println("There is no Comment with this id");
                else
                    System.out.println(c.toString());
                break;
            case 2:
                c=fillComment(null);
                dao.merge(c);
                break;
            case 3:
                System.out.println("Enter id of Comment:");
                int id_del = reader.nextInt();
                c=dao.getEntity(Comment.class,id_del);
                dao.delete(c);
                break;
            case 5:
                System.out.println("Enter id of Comment:");
                com_id = reader.nextInt();
                c = dao.getEntity(Comment.class,com_id);
                c=fillComment(c);
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
    private static void callTransaction() throws IOException {
        int input=0;
        System.out.println("Choose an option: \n" +
                "1. Get Transaction by Id \n" +
                "2. Create Transaction \n" +
                "3. Update Transaction by Id \n" +
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
                tra_id = reader.nextInt();
                t = dao.getEntity(Transaction.class,tra_id);
                if(t==null)
                    System.out.println("There is no Transaction with this id");
                else {
                    System.out.println(t.toString());
                    readTransactionGames(t);
                }
                break;
            case 2:
                t = fillTransaction(null);
                dao.merge(t);
                tgs = createTransactionsGames(t);
                for(TransactionsGames tg : tgs)
                    dao.merge(tg);
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
                System.out.println("\n");
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
        System.out.println("\n Price: ");
        double game_price = reader.nextDouble();
        System.out.println("\nProduct type (\"Game\",\"DLC\" ): ");
        String game_producttype = breader.readLine();
        if(g!=null){
            g.setPrice(game_price);
            g.setDescription(game_description);
            g.setProduct_type(game_producttype);
            g.setTitle(game_title);

        }
        else
            g= new Game(game_title,game_description,null,game_price,new Date(),game_producttype);
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
        int com_user_id = reader.nextInt();
        System.out.println("\nGame_id: ");
        int com_game_id = reader.nextInt();
        System.out.println("\nScore: ");
        int com_score = reader.nextInt();
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
        int tra_user_id = reader.nextInt();
        System.out.println("\nSum: ");
        int tra_sum = reader.nextInt();
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
        System.out.println("List of games by this dev:");
        Set<GamesDevelopers> gds = dao.getAll(GamesDevelopers.class);
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
        Set<GamesDevelopers> gds = dao.getAll(GamesDevelopers.class);
        for(GamesDevelopers gd : gds){
            if(gd.getId1()==g.getId())
                dao.delete(gd);
        }
    }

    //Linker GamesPublishers
    private static GamesPublishers createGamesPublishers(Game g) throws IOException {
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
        for(GamesPublishers gp : gps){
            if(gp.getId2()==pub.getId())
                dao.delete(gp);
        }
    }
    private static void deleteGamesPublishers(Game g){
        Set<GamesPublishers> gps = dao.getAll(GamesPublishers.class);
        for(GamesPublishers gp : gps){
            if(gp.getId1()==g.getId())
                dao.delete(gp);
        }
    }

    //Linker TransactionsGames
    private static ArrayList<TransactionsGames> createTransactionsGames(Transaction t) throws  IOException{
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
        Set<TransactionsGames> tgs = dao.getAll(TransactionsGames.class);
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
        Set<UsersGames> ugs = dao.getAll(UsersGames.class);
        System.out.println("List of games in this user's library:");
        for(UsersGames i : ugs)
            if (i.getId1() == u.getId()) {
                Game g = dao.getEntity(Game.class, i.getId2());
                if (g != null)
                    System.out.println("\t" + g.getName());
            }
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
