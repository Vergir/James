package dbobjects.interfaces;

public interface Nameable {
    default String getNameField() {
        return "name";
    }
}
