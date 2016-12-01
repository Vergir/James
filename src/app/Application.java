package app;

import dao.DatabaseAccessObject;
import dbobjects.classes.*;
import dbobjects.interfaces.DbObject;
import dbobjects.interfaces.Nameable;
import ui.Menu;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static app.OperationResult.*;

/**
 * Created by Vergir on 01/12/2016.
 */
public class Application {
    private static DatabaseAccessObject dao;
    //Menu objects
    private static final Menu mainMenu;
    private static Menu classMenu;
    private static Menu selectMenu;
    //State objects
    private static Class activeClass;
    private static DbObject activeDbObject;
    private static Object activeObject;

    static {
        mainMenu = new Menu("Main Menu")
                .addItem("Comments", (x) -> prepareAndStartClassMenu(Comment.class))
                .addItem("Developers", (x) -> prepareAndStartClassMenu(Developer.class))
                .addItem("Games", (x) -> prepareAndStartClassMenu(Game.class))
                .addItem("Publishers", (x) -> prepareAndStartClassMenu(Publisher.class))
                .addItem("Transactions", (x) -> prepareAndStartClassMenu(Transaction.class))
                .addItem("Users", (x) -> prepareAndStartClassMenu(User.class))
                .addItem("Exit", (x) -> {System.exit(0); return null;})
                .setLastItemIsZero(true)
                ;
    }

    public static void start(DatabaseAccessObject dao) {
        Application.dao = dao;
        OperationResult opResult = NONE;
        while (true) {
            System.out.println('\n');
            if (opResult != NONE){
                System.out.println("Last Operation Status: " + opResult);
                opResult = NONE;
            }
            activeObject = null;
            if (activeClass != null) {
                opResult = (OperationResult) prepareAndStartClassMenu(activeClass);
            }
            else
                opResult = (OperationResult) mainMenu.start();
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
                    return ERROR;
            case READ_NAME:
                result = dao.getByName(activeClass, (String) activeObject);
                if (result != null) {
                    activeDbObject = (DbObject)result;
                    printReadResult();
                    return SUCCESS;
                }
                else
                    return ERROR;
            case CREATE:
                result = dao.upsert(activeDbObject);
                if (result != null)
                    return SUCCESS;
                else
                    return ERROR;
            case UPDATE:
                result = dao.upsert(activeDbObject);
                if (result != null)
                    return SUCCESS;
                else
                    return ERROR;
            case DELETE:
                result = dao.delete(activeDbObject);
                if (result != null)
                    return SUCCESS;
                else
                    return ERROR;
            default:
                return ERROR;
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
                return ERROR;
            case READ_NAME:
                System.out.println("Please input Name (String)");
                if (reader.hasNext()) {
                    activeObject = reader.next();
                    return SUCCESS;
                }
                return ERROR;
            default:
                return ERROR;
        }
    }

    private static <T extends DbObject> Object prepareAndStartClassMenu(Class<T> c) {
        activeClass = c;
        createClassMenu();
        printActiveDbObject();
        return classMenu.start();
    }
    private static Object prepareAndStartSelectMenu() {
        createSelectMenu();
        return selectMenu.start();
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
            classMenu.addItem("Update", (x) -> doOperation(OperationType.UPDATE))
                    .addItem("Delete", (x) -> doOperation(OperationType.DELETE))
                    ;
        classMenu.addItem("Get Back", (x) -> {activeClass=null;activeDbObject=null;return NONE;}).setLastItemIsZero(true);
    }

    private static void printActiveDbObject() {
        System.out.print("Selected Object: ");
        if (activeDbObject == null) {
            System.out.println("none (Update and Delete Disabled)");
            return;
        }
        System.out.print(activeDbObject.getClass().getSimpleName());
        System.out.print("{id: " + activeDbObject.getId().toString(16));
        if (Nameable.class.isAssignableFrom(activeDbObject.getClass())) {
            String nameFieldName = ((Nameable) activeDbObject).getNameField();
            String name = null;
            try {
                Field nameField = activeDbObject.getClass().getDeclaredField(nameFieldName);
                nameField.setAccessible(true);
                name = (String)nameField.get(activeDbObject);
            } catch (Exception e) {e.printStackTrace();}
            System.out.print(", "+nameFieldName+": "+name);
        }
        System.out.println("}");
    }
    private static void printReadResult() {
        if (activeObject instanceof Set)
            for (DbObject o : (Set<DbObject>)activeObject)
                System.out.println(o);
        else
            prettyPrintActiveDbObject();
    }
    private static void prettyPrintActiveDbObject() {
        System.out.println(activeDbObject.getClass().getSimpleName());
        System.out.println("ID: " + activeDbObject.getId().toString(16));

        try {
            for (Field f : activeDbObject.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (Collection.class.isAssignableFrom(f.getType()))
                    continue;
                if (f.getType().equals(BigInteger.class))
                    continue;
                System.out.println(f.getName() + ": " + f.get(activeDbObject));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        printForeignFieldsOfActiveDbObject();
    }
    private static void printForeignFieldsOfActiveDbObject() {
        if (activeDbObject.getClass().equals(Developer.class)) {
            Developer devOrPub = (Developer)activeDbObject;
            System.out.println("games:");
            Set<Game> games = new HashSet<>();
            for (BigInteger id : devOrPub.getGames())
                games.add(dao.getDbObject(Game.class, id));
            for (Game g : games)
                System.out.println("  id: " + g.getId().toString(16) + "; name: " + g.getName());
            if (games.size() == 0)
                System.out.println("  No games found");
        }
    }
}
