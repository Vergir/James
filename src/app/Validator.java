package app;

import dao.DatabaseAccessObject;
import dbobjects.classes.*;
import dbobjects.interfaces.DbObject;

import static app.OperationResult.*;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Vergir on 01/12/2016.
 */
public class Validator {
    static DatabaseAccessObject dao;
    static Scanner reader;
    static OperationResult getValidatedInput(DbObject dbObject, Field field) {
        Class declaringClass = field.getDeclaringClass();

        if (declaringClass.equals(Comment.class))
            return getValidatedInputForComment(dbObject, field);
        if (declaringClass.equals(Developer.class))
            return getValidatedInputForDeveloper(dbObject, field);
        if (declaringClass.equals(Game.class))
            return getValidatedInputForGame(dbObject, field);
        if (declaringClass.equals(Publisher.class))
            return getValidatedInputForPublisher(dbObject, field);
        if (declaringClass.equals(Transaction.class))
            return getValidatedInputForTransaction(dbObject, field);
        if (declaringClass.equals(User.class))
            return getValidatedInputForUser(dbObject, field);

        return ERROR_GENERIC;
    }

    private static OperationResult getValidatedInputForComment(DbObject dbObject, Field field) {
        reader = new Scanner(System.in);

        if (field.getName().equals("userId")) {
            System.out.println("Please input User Id (24-digit hex number)");
            if (!reader.hasNextBigInteger(16))
                return ERROR_VALIDATION;
            BigInteger userId = reader.nextBigInteger();
            if (dao.getDbObject(User.class, userId) == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, userId);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if (field.getName().equals("gameId")) {
            System.out.println("Please input Game Id (24-digit hex number)");
            if (!reader.hasNextBigInteger(16))
                return ERROR_VALIDATION;
            BigInteger gameId = reader.nextBigInteger();
            if (dao.getDbObject(Game.class, gameId) == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, gameId);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if (field.getName().equals("score")) {
            System.out.println("Please input Comment's Score (decimal number from 0 to 10)");
            if (!reader.hasNextDouble())
                return ERROR_VALIDATION;
            Double score = reader.nextDouble();
            if (score < 0 || score > 10 || score.isInfinite() || score.isNaN())
                return ERROR_VALIDATION;
            try {field.set(dbObject, score);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if (field.getName().equals("content")) {
            System.out.println("Please input Comment's Content (Any text)");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String content = reader.next();
            if (content == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, content);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if (field.getName().equals("writeDate")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
            System.out.println("Please input Comment's Write Date in Format DD/MM/YYYY");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String input = reader.next();
            Date writeDate;
            try {writeDate = sdf.parse(input);}catch (Exception e) {return ERROR_VALIDATION;}
            try {field.set(dbObject, writeDate);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        return ERROR_GENERIC;
    }
    private static OperationResult getValidatedInputForDeveloper(DbObject dbObject, Field field) {
        reader = new Scanner(System.in);

        if (field.getName().equals("name")) {
            System.out.println("Please input Developer's name (any text)");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String name = reader.next();
            if (name == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, name);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if (field.getName().equals("address")) {
            System.out.println("Please input Developer's Address (any text)");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String address = reader.next();
            if (address == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, address);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if (field.getName().equals("email")) {
            System.out.println("Please input Developer's email (any text)");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String email = reader.next();
            if (email == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, email);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if (field.getName().equals("games")) {
            System.out.println("Please input COMMA SEPARATED list of IDs (24-digit hex string) of games from this Developer");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            Set<BigInteger> ids = new HashSet<>();
            //may be horrible line...
            Arrays.asList(reader.next().split(",")).forEach((x) -> {try {ids.add(new BigInteger(x, 16));} catch (Exception e) {}});
            for (BigInteger id : ids)
                if (dao.getDbObject(Game.class, id) == null)
                    ids.remove(id);
            System.out.println("There were " + ids.size() + "valid ids. Input 'yes' if you agree with that");
            if (reader.next().toLowerCase().equals("yes")) {
                try {
                    field.set(dbObject, ids);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return SUCCESS;
            }
            return ERROR_VALIDATION;
        }
        return ERROR_GENERIC;
    }
    private static OperationResult getValidatedInputForGame(DbObject dbObject, Field field) {
        return ERROR_GENERIC;
    }
    private static OperationResult getValidatedInputForTransaction(DbObject dbObject, Field field) {
        return ERROR_GENERIC;
    }
    private static OperationResult getValidatedInputForPublisher(DbObject dbObject, Field field) {
        return ERROR_GENERIC;
    }
    private static OperationResult getValidatedInputForUser(DbObject dbObject, Field field) {
        return ERROR_GENERIC;
    }
}
