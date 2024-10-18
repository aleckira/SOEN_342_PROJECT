package PackageActorsAndObjects;

public class Admin extends Actor {
    private final String password;
    private static Admin instance;  // Static variable to hold the single instance of Admin

    // Private constructor to prevent instantiation from outside
    private Admin(String name, String password) {
        super(name);
        this.password = password;
    }

    // Public method to provide access to the single instance
    public static Admin getInstance(String name, String password) {
        if (instance == null) {
            instance = new Admin(name, password);
        }
        return instance;
    }

    public String getPassword() {
        return password;
    }
}
