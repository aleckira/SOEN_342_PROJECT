package PackageActors;

public class Admin extends Actor {
    private String password;
    public Admin(String name, String password) {
        super(name);
        this.password = password;
    }
    public String getName() {
        return this.getName();
    }
    public String getPassword() {
        return password;
    }

}
