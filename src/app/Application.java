package app;

import dao.DatabaseAccessObject;
import dao.RedisMongoDao;
import dbobjects.classes.*;
import dbobjects.interfaces.*;
import ui.Menu;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.Set;

import static app.OperationResult.*;
import static app.OperationType.*;

/**
 * Created by Vergir on 01/12/2016.
 */
public class Application {
    private static DatabaseAccessObject dao;
    //Menu objects
    private static Menu mainMenu;
    private static final Menu redisMenu;
    private static Menu classMenu;
    private static Menu selectMenu;
    private static Menu updateMenu;
    //State objects
    private static Class activeClass;
    private static DbObject activeDbObject;
    private static Object activeObject;

    static {
        redisMenu = new Menu("Redis")
                .addItem("Get All Keys", (x) -> {
                    System.out.println("Keys: ");
                    ((RedisMongoDao) dao).getAllKeys().forEach((key) -> System.out.println(key));
                    return null;
                })
                .addItem("Flush Cache", (x) -> {((RedisMongoDao) dao).flushCache(); return null;})
                .addItem("Get Back", (x) -> {return null;})
                .setLastItemIsZero(true)
        ;

        mainMenu = new Menu("Main Menu")
                .addItem("Comments", (x) -> prepareAndStartClassMenu(Comment.class))
                .addItem("Developers", (x) -> prepareAndStartClassMenu(Developer.class))
                .addItem("Games", (x) -> prepareAndStartClassMenu(Game.class))
                .addItem("Publishers", (x) -> prepareAndStartClassMenu(Publisher.class))
                .addItem("Transactions", (x) -> prepareAndStartClassMenu(Transaction.class))
                .addItem("Users", (x) -> prepareAndStartClassMenu(User.class))
                .addItem("Redis", (x) -> tryStartRedis())
                .addItem("Exit", (x) -> {System.exit(0); return null;})
                .setLastItemIsZero(true)
                ;

    }

    public static void start(DatabaseAccessObject dao) {
        Application.dao = dao;
        DbObjectPrinter.dao = dao;
        Validator.dao = dao;

        OperationResult opResult = NONE;
        while (true) {
            System.out.print('\n');
            if (opResult != NONE){
                System.out.println("Last Operation Status: " + opResult.getMessage());
                opResult = NONE;
            }
            activeObject = null;
            if (activeClass != null)
                opResult = (OperationResult) prepareAndStartClassMenu(activeClass);
            else {
                if (dao instanceof RedisMongoDao) {
                    RedisMongoDao rmd = ((RedisMongoDao) dao);
                }
                opResult = (OperationResult) mainMenu.start();
            }
        }
    }

    private static OperationResult doOperation(OperationType opType){
        OperationResult preparationResult = prepareFor(opType);
        if (preparationResult != SUCCESS)
            return preparationResult;
        Object result = null;
        switch (opType) {
            case READ_ALL:
                activeObject = dao.getAll(activeClass);
                printReadResult();
                return SUCCESS;
            case READ_ID:
                result = dao.getDbObject(activeClass, (BigInteger) activeObject);
                if (result != null) {
                    activeDbObject = (DbObject)result;
                    printReadResult();
                    return SUCCESS;
                }
                else
                    return ERROR_NOTFOUND;
            case READ_NAME:
                result = dao.getByName(activeClass, (String) activeObject);
                if (result != null) {
                    activeDbObject = (DbObject)result;
                    printReadResult();
                    return SUCCESS;
                }
                else
                    return ERROR_NOTFOUND;
            case CREATE:
                result = dao.upsert(activeDbObject);
                if (result != null)
                    return SUCCESS;
                else
                    return ERROR_DAO;
            case UPDATE:
                result = dao.upsert(activeDbObject);
                if (result != null)
                    return SUCCESS;
                else
                    return ERROR_DAO;
            case DELETE:
                result = dao.delete(activeDbObject);
                activeDbObject = null;
                if (result != null)
                    return SUCCESS;
                else
                    return ERROR_DAO;
            default:
                return ERROR_GENERIC;
        }
    }
    private static OperationResult prepareFor(OperationType opType) {
        Scanner reader = new Scanner(System.in);
        switch (opType){
            case READ_ALL:
                return SUCCESS;
            case READ_ID:
                System.out.println("Please input ID (24-digit hex number)");
                if (reader.hasNextBigInteger(16)) {
                    activeObject = reader.nextBigInteger(16);
                    return SUCCESS;
                }
                return ERROR_VALIDATION;
            case READ_NAME:
                System.out.println("Please input Name (String)");
                if (reader.hasNext()) {
                    activeObject = reader.next();
                    return SUCCESS;
                }
                return ERROR_VALIDATION;
            case CREATE:
                return getFullObjectInput();
            case UPDATE:
                if (activeDbObject != null)
                    return SUCCESS;
                return ERROR_NOACTIVEOBJECT;
            case DELETE:
                if (activeDbObject != null)
                    return SUCCESS;
                return ERROR_GENERIC;
            default:
                return ERROR_NOACTIVEOBJECT;
        }
    }

    private static <T extends DbObject> Object prepareAndStartClassMenu(Class<T> c) {
        activeClass = c;
        createClassMenu();
        System.out.print("Active Object: ");
        if (activeDbObject == null)
            System.out.println("none (Update and Delete Disabled)");
        else
            DbObjectPrinter.printSummaryOf(activeDbObject);
        return classMenu.start();
    }
    private static Object prepareAndStartSelectMenu() {
        createSelectMenu();
        return selectMenu.start();
    }
    private static Object prepareAndStartUpdateMenu() {
        System.out.println("Object to update");
        DbObjectPrinter.print(activeDbObject);
        createUpdateMenu();
        return updateMenu.start();
    }

    private static void createSelectMenu() {
        selectMenu = new Menu(activeClass.getSimpleName()+'s')
                .addItem("Select All", (x) -> doOperation(OperationType.READ_ALL))
                .addItem("Select by Id", (x) -> doOperation(OperationType.READ_ID))
                ;
        if (Nameable.class.isAssignableFrom(activeClass))
            selectMenu.addItem("Select by Name", (x) -> doOperation(OperationType.READ_NAME));
        selectMenu.addItem("Get Back", (x) -> classMenu.start()).setLastItemIsZero(true);
    }
    private static void createClassMenu() {
        classMenu = new Menu(activeClass.getSimpleName()+"s")
                .addItem("Select", (x) -> prepareAndStartSelectMenu())
                .addItem("Create", (x) -> doOperation(OperationType.CREATE))
                ;
        if (activeDbObject != null)
            classMenu.addItem("Update", (x) -> prepareAndStartUpdateMenu())
                    .addItem("Delete", (x) -> doOperation(OperationType.DELETE))
                    ;
        classMenu.addItem("Get Back", (x) -> {activeClass=null;activeDbObject=null;return NONE;}).setLastItemIsZero(true);
    }
    private static void createUpdateMenu() {

        updateMenu = new Menu("Fields to Update").addItem("Update All Fields", (x -> getFullObjectInput()));
        try {
            for (Field f : activeClass.getDeclaredFields()) {
                f.setAccessible(true);
                if (f.getName().toLowerCase().equals("id"))
                    continue;
                updateMenu.addItem("Update " + f.getName(), (x) -> getValidatedInputAndUpdate(f));
            }
        } catch (Exception e) { e.printStackTrace();}
        updateMenu.addItem("Get back", (x) -> prepareAndStartClassMenu(activeClass)).setLastItemIsZero(true);
    }

    private static void printReadResult() {
        if (activeObject instanceof Set)
            for (DbObject o : (Set<DbObject>)activeObject)
                DbObjectPrinter.print(o);
        else
                DbObjectPrinter.printWithExpansions(activeDbObject);
    }

    private static OperationResult getFullObjectInput() {
        OperationResult opResult = NONE;
        DbObject newDbObject = null;
        try {
            newDbObject = (DbObject)activeClass.newInstance();
            for (Field f : activeClass.getDeclaredFields()) {
                f.setAccessible(true);
                if (f.getName().toLowerCase().equals("id"))
                    continue;
                opResult = Validator.getValidatedInput(newDbObject, f);
                if (opResult != SUCCESS)
                    return opResult;
            }
        } catch (Exception e) { e.printStackTrace();}
        activeDbObject = newDbObject;
        return SUCCESS;
    }

    private static OperationResult getValidatedInputAndUpdate(Field f) {
        OperationResult opResult = Validator.getValidatedInput(activeDbObject, f);
        if (opResult != SUCCESS)
            return opResult;
        else
            return doOperation(UPDATE);
    }

    private static OperationResult tryStartRedis() {
        if (!(dao instanceof RedisMongoDao))
            return INFO_REDISNOTCONNECTED;
        System.out.println(((RedisMongoDao) dao).getCacheSize() + " Keys");
        redisMenu.start();
        return SUCCESS;
    }
}
