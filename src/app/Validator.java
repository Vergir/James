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
        reader = new Scanner(System.in).useDelimiter("\n");

        if ("userId".equals(field.getName())) {
            System.out.println("Please input User Id (24-digit hex number)");
            if (!reader.hasNextBigInteger(16))
                return ERROR_VALIDATION;
            BigInteger userId = reader.nextBigInteger();
            if (dao.getDbObject(User.class, userId) == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, userId);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if ("gameId".equals(field.getName())) {
            System.out.println("Please input Game Id (24-digit hex number)");
            if (!reader.hasNextBigInteger(16))
                return ERROR_VALIDATION;
            BigInteger gameId = reader.nextBigInteger();
            if (dao.getDbObject(Game.class, gameId) == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, gameId);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if ("score".equals(field.getName())) {
            System.out.println("Please input Comment's Score (decimal number from 0 to 10)");
            if (!reader.hasNextDouble())
                return ERROR_VALIDATION;
            Double score = reader.nextDouble();
            if (score < 0 || score > 10 || score.isInfinite() || score.isNaN())
                return ERROR_VALIDATION;
            try {field.set(dbObject, score);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if ("content".equals(field.getName())) {
            System.out.println("Please input Comment's Content (Any text)");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String content = reader.next();
            if (content == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, content);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if ("writeDate".equals(field.getName())) {
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
        reader = new Scanner(System.in).useDelimiter("\n");

        if ("name".equals(field.getName())) {
            System.out.println("Please input Developer's name (any text)");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String name = reader.next();
            if (name == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, name);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if ("address".equals(field.getName())) {
            System.out.println("Please input Developer's Address (any text)");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String address = reader.next();
            if (address == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, address);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if ("email".equals(field.getName())) {
            System.out.println("Please input Developer's email (any text)");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String email = reader.next();
            if (email == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, email);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if ("games".equals(field.getName())) {
            System.out.println("Please input COMMA SEPARATED list of IDs (24-digit hex string) of games from this Developer");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            Set<BigInteger> ids = new HashSet<>();
            //may be horrible line...
            Arrays.asList(reader.next().split(",")).forEach((x) -> {try {ids.add(new BigInteger(x, 16));} catch (Exception e) {}});
            for (BigInteger id : ids)
                if (dao.getDbObject(Game.class, id) == null)
                    ids.remove(id);
            System.out.println("There were " + ids.size() + " valid ids. Input 'yes' if you agree with that");
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
        reader = new Scanner(System.in).useDelimiter("\n");

        if("title".equals(field.getName())){
            System.out.println("Please input Game's Title");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String title = reader.next();
            if (title == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, title);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if("description".equals(field.getName())){
            System.out.println("Please input Game's description");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String description = reader.next();
            if (description == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, description);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if ("price".equals(field.getName())) {
            System.out.println("Please input Game's Price (decimal number higher than 0$)");
            if (!reader.hasNextDouble())
                return ERROR_VALIDATION;
            Double price = reader.nextDouble();
            if (price < 0 || price.isInfinite() || price.isNaN())
                return ERROR_VALIDATION;
            try {field.set(dbObject, price);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if ("releaseDate".equals(field.getName())) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
            System.out.println("Please input Game's Release Date in Format DD/MM/YYYY");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String input = reader.next();
            Date releaseDate;
            try {releaseDate = sdf.parse(input);}catch (Exception e) {return ERROR_VALIDATION;}
            try {field.set(dbObject, releaseDate);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        return ERROR_GENERIC;
    }
    private static OperationResult getValidatedInputForTransaction(DbObject dbObject, Field field) {
        reader = new Scanner(System.in).useDelimiter("\n");

        if ("userId".equals(field.getName())) {
            System.out.println("Please input User Id for transaction (24-digit hex number)");
            if (!reader.hasNextBigInteger(16))
                return ERROR_VALIDATION;
            BigInteger userId = reader.nextBigInteger();
            if (dao.getDbObject(User.class, userId) == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, userId);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if ("sum".equals(field.getName())) {
            System.out.println("Please input Transaction's sum of money (decimal number higher than 0$)");
            if (!reader.hasNextDouble())
                return ERROR_VALIDATION;
            Double sum = reader.nextDouble();
            if (sum < 0 || sum.isInfinite() || sum.isNaN())
                return ERROR_VALIDATION;
            try {field.set(dbObject, sum);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if("transactionDate".equals(field.getName())){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
            System.out.println("Please input Transaction's Date in Format DD/MM/YYYY");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String input = reader.next();
            Date transactionDate;
            try {transactionDate = sdf.parse(input);}catch (Exception e) {return ERROR_VALIDATION;}
            try {field.set(dbObject, transactionDate);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if (field.getName().equals("games")) {
            System.out.println("Please input COMMA SEPARATED list of IDs (24-digit hex string) of games purchased in this transaction");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            Set<BigInteger> ids = new HashSet<>();
            //may be horrible line... // u sure ? =^
            Arrays.asList(reader.next().split(",")).forEach((x) -> {try {ids.add(new BigInteger(x, 16));} catch (Exception e) {}});
            for (BigInteger id : ids)
                if (dao.getDbObject(Game.class, id) == null)
                    ids.remove(id);
            System.out.println("There were " + ids.size() + " valid ids. Input 'yes' if you agree with that");
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
    private static OperationResult getValidatedInputForPublisher(DbObject dbObject, Field field) {
        reader = new Scanner(System.in).useDelimiter("\n");

        if (field.getName().equals("name")) {
            System.out.println("Please input Publisher's name (any text)");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String name = reader.next();
            if (name == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, name);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if (field.getName().equals("address")) {
            System.out.println("Please input Publisher's Address (any text)");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String address = reader.next();
            if (address == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, address);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if (field.getName().equals("email")) {
            System.out.println("Please input Publisher's email (any text)");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String email = reader.next();
            if (email == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, email);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if (field.getName().equals("games")) {
            System.out.println("Please input COMMA SEPARATED list of IDs (24-digit hex string) of games from this Publisher");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            Set<BigInteger> ids = new HashSet<>();
            //may be horrible line...
            Arrays.asList(reader.next().split(",")).forEach((x) -> {try {ids.add(new BigInteger(x, 16));} catch (Exception e) {}});
            for (BigInteger id : ids)
                if (dao.getDbObject(Game.class, id) == null)
                    ids.remove(id);
            System.out.println("There were " + ids.size() + " valid ids. Input 'yes' if you agree with that");
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
    private static OperationResult getValidatedInputForUser(DbObject dbObject, Field field) {
        reader = new Scanner(System.in).useDelimiter("\n");

        if("nickName".equals(field.getName())){
            System.out.println("Please input User's Nickname");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String nickName = reader.next();
            if (nickName == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, nickName);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if("firstName".equals(field.getName())){
            System.out.println("Please input User's first name");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String firstName = reader.next();
            if (firstName == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, firstName);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if("lastName".equals(field.getName())){
            System.out.println("Please input User's last name");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String lastName = reader.next();
            if (lastName == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, lastName);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if("email".equals(field.getName())){
            System.out.println("Please input User's email");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String email = reader.next();
            if (email == null)
                return ERROR_VALIDATION;
            try {field.set(dbObject, email);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if (field.getName().equals("registrationDate")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
            System.out.println("Please input User's Registration Date in Format DD/MM/YYYY");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            String input = reader.next();
            Date registrationDate;
            try {registrationDate = sdf.parse(input);}catch (Exception e) {return ERROR_VALIDATION;}
            try {field.set(dbObject, registrationDate);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }

        if (field.getName().equals("balance")) {
            System.out.println("Please input User's balance (decimal number higher than 0)");
            if (!reader.hasNextDouble())
                return ERROR_VALIDATION;
            Double balance = reader.nextDouble();
            if (balance < 0 || balance.isInfinite() || balance.isNaN())
                return ERROR_VALIDATION;
            try {field.set(dbObject, balance);}catch (Exception e) {e.printStackTrace();}
            return SUCCESS;
        }


        if (field.getName().equals("games")) {
            System.out.println("Please input COMMA SEPARATED list of IDs (24-digit hex string) of this User's games");
            if (!reader.hasNext())
                return ERROR_VALIDATION;
            Set<BigInteger> ids = new HashSet<>();
            //may be horrible line...
            Arrays.asList(reader.next().split(",")).forEach((x) -> {try {ids.add(new BigInteger(x, 16));} catch (Exception e) {}});
            for (BigInteger id : ids)
                if (dao.getDbObject(Game.class, id) == null)
                    ids.remove(id);
            System.out.println("There were " + ids.size() + " valid ids. Input 'yes' if you agree with that");
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
}
