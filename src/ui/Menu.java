package ui;

import java.security.InvalidParameterException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Created by Vergir on 30/11/2016.
 */
public class Menu {
    private Scanner reader;

    private String name;
    private LinkedHashMap<String, Function<Object, Object>> items;
    private Object functionArgument;
    private boolean lastItemIsZero;

    {
        name = "";
        items = new LinkedHashMap<>();
    }

    public Menu(String name, LinkedHashMap<String, Function<Object, Object>> items, Object functionArgument, boolean lastItemIsZero) {
        this.name = (name == null) ? "" : name;
        this.items = (items != null) ? items: this.getItems();
        this.functionArgument = functionArgument;
        this.lastItemIsZero = lastItemIsZero;
    }
    public Menu(String itemDescription, Function<Object, Object> function) {
        this.items.put(itemDescription, function);
    }

    public String getName() {
        return name;
    }
    public LinkedHashMap<String, Function<Object, Object>> getItems() {
        return items;
    }
    public Object getFunctionArgument() {
        return functionArgument;
    }
    public boolean isLastItemIsZero() {
        return lastItemIsZero;
    }

    public Menu setName(String name) {
        this.name = (name == null) ? "" : name;
        return this;
    }
    public void setItems(LinkedHashMap<String, Function<Object, Object>> items) {
        this.items = items;
    }

    public Menu setFunctionArgument(Object functionArgument) {
        this.functionArgument = functionArgument;
        return this;
    }
    public Menu setLastItemIsZero(boolean lastItemIsZero) {
        this.lastItemIsZero = lastItemIsZero;
        return this;
    }
    public Menu addItem(String itemDesc, Function<Object, Object> func) {
        items.put(itemDesc, func);
        return this;
    }

    public Object start() {
        if (items.size() < 1)
            throw new InvalidParameterException("0 menu items");
        int input;
        while (true) {
            printMenu();
            reader = new Scanner(System.in);
            if (reader.hasNextInt()) {
                input = reader.nextInt();
                if (isInsidePossibleRange(input))
                    if (input == 0)
                        return items.get(items.keySet().toArray()[items.size()-1]).apply(functionArgument);
                    else
                        return items.get(items.keySet().toArray()[input - 1]).apply(functionArgument);
            }
            System.out.println("Wrong input, please try again");
        }
    }

    @Override
    public String toString() {
        return "Menu{" +
                "name='" + name + '\'' +
                ", items.size()=" + items.size() +
                ", functionArgument=" + (functionArgument == null ? "null" : functionArgument.hashCode()) +
                ", lastItemIsZero=" + lastItemIsZero +
                '}';
    }

    private void printMenu() {
        System.out.println(name);
        String[] keys = items.keySet().toArray(new String[]{});
        int penultimateIndex = items.size()-1;
        int last = lastItemIsZero ? 0 : items.size();
        for (int i = 0; i < penultimateIndex; i++)
            System.out.printf("%d. %s%n", i+1, keys[i]);
        System.out.printf("%d. %s%n", last, keys[items.size()-1]);
    }
    private boolean isInsidePossibleRange(int input) {
        int rangeMin = lastItemIsZero ? 0 : 1;
        int rangeMax = lastItemIsZero ? items.size()-1 : items.size();

        return (input >= rangeMin) && (input <= rangeMax);
    }
}
