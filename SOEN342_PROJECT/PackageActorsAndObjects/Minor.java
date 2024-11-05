package PackageActorsAndObjects;

public class Minor {
    private int id;
    private String name;
    private int guardianId;

    // Constructor
    public Minor(int id, String name, int guardianId) {
        this.id = id;
        this.name = name;
        this.guardianId = guardianId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGuardianId() {
        return guardianId;
    }

    // Setters (if needed)
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGuardianId(int guardianId) {
        this.guardianId = guardianId;
    }

    @Override
    public String toString() {
        return "Minor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", guardianId=" + guardianId +
                '}';
    }
}
