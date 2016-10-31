package entities;

public class Developer implements Entity, Nameable {
    private int id;
    private String name;
    private String address;
    private String email;

    public Developer(String name, String address, String email){
        if (name == null || address == null || email == null)
            throw new NullPointerException("initialization info for Developer is invalid");

        this.name = name;
        this.address = address;
        this.email = email;
    }

    public Developer(int id, String name, String address, String email) {
        this(name, address, email);
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
