package PackageActorsAndObjects;

public class Admin extends Actor {
    private final String password;
    public Admin(String name, String password) {
        super(name);
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

}
