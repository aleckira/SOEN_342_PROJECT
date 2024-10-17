package PackageImportantObjects;

public abstract class Actor {
    private String name;
    public Actor() {}
    public Actor(String name) {
        this.name = name;
    }
    public String getName() {return name;}
}
