package SOEN342_PROJECT.PackageActorsAndObjects;

import java.util.ArrayList;

public abstract class Actor {
    private String name;
    public Actor() {}
    public Actor(String name) {
        this.name = name;
    }
    public String getName() {return name;}
    //abstract method that will get the corresponding correct offerings for each actor type
    public abstract ArrayList<Offering> getOfferingsForViewing();
}
