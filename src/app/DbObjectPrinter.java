package app;

import dao.DatabaseAccessObject;
import dbobjects.classes.*;
import dbobjects.interfaces.*;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Collection;

/**
 * Created by Vergir on 01/12/2016.
 */
class DbObjectPrinter {
    static DatabaseAccessObject dao;
    
    //outputs basic info like '<Class>{id:XXX,name:XXX}'
    static void printSummaryOf(DbObject dbObject) {
        System.out.print(dbObject.getClass().getSimpleName());
        System.out.print("{id: " + dbObject.getId().toString(16));
        if (Nameable.class.isAssignableFrom(dbObject.getClass())) {
            String nameFieldName = ((Nameable) dbObject).getNameField();
            String name = null;
            try {
                Field nameField = dbObject.getClass().getDeclaredField(nameFieldName);
                nameField.setAccessible(true);
                name = (String)nameField.get(dbObject);
            } catch (Exception e) {e.printStackTrace();}
            System.out.print(", "+nameFieldName+": "+name);
        }
        else if (dbObject instanceof Comment) {
            String cText = ((Comment) dbObject).getContent();
            if (cText.length() > 16)
                System.out.print(", content: " + cText.substring(0,16)+"...");
            else
                System.out.print(", content: " + cText);
        }
        System.out.println('}');
    }
    
    //outputs full info without(!) unwrapping relations
    static void print(DbObject dbObject) {
        System.out.println(dbObject);
    }
    
    //outputs full info with(!) unwrapping relations
    static void printWithExpansions(DbObject dbObject) {
        System.out.println(dbObject.getClass().getSimpleName());
        System.out.println("ID: " + dbObject.getId().toString(16));

        try {
            for (Field f : dbObject.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (Collection.class.isAssignableFrom(f.getType()))
                    continue;
                if (f.getType().equals(BigInteger.class))
                    continue;
                System.out.println(f.getName() + ": " + f.get(dbObject));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        expandRelationsOf(dbObject);
    }


    private static void expandRelationsOf(DbObject dbObject) {
        if (dbObject instanceof Developer)
            expandDeveloper(((Developer) dbObject));
        else if (dbObject instanceof Publisher)
            expandPublisher(((Publisher) dbObject));
        else if (dbObject instanceof Comment)
            expandComment(((Comment) dbObject));
        else if (dbObject instanceof Transaction)
            expandTransaction(((Transaction) dbObject));
        else if (dbObject instanceof User)
            expandUser(((User) dbObject));
    }

    private static void expandComment(Comment comment) {
        System.out.print("game: ");
        printSummaryOf(dao.getDbObject(Game.class, comment.getGameId()));
        System.out.print("user: ");
        printSummaryOf(dao.getDbObject(User.class, comment.getUserId()));
    }
    private static void expandDeveloper(Developer developer) {
        System.out.println(developer.getGames().size() + " games:");
        for (BigInteger id : developer.getGames()) {
            System.out.print("  ");
            printSummaryOf(dao.getDbObject(Game.class, id));
        }

    }
    private static void expandPublisher(Publisher publisher) {
        System.out.println(publisher.getGames().size() + " games:");
        for (BigInteger id : publisher.getGames()) {
            System.out.print("  ");
            printSummaryOf(dao.getDbObject(Game.class, id));
        }
    }
    private static void expandTransaction(Transaction transaction) {
        System.out.print("user: ");
        printSummaryOf(dao.getDbObject(User.class, transaction.getUserId()));
        System.out.print(transaction.getGames().size() + " games:");
        for (BigInteger id : transaction.getGames()) {
            System.out.print("  ");
            printSummaryOf(dao.getDbObject(Game.class, id));
        }
    }
    private static void expandUser(User user) {
        System.out.println(user.getGames().size() + " games:");
        for (BigInteger id : user.getGames()) {
            System.out.print("  ");
            printSummaryOf(dao.getDbObject(Game.class, id));
        }
    }
}
