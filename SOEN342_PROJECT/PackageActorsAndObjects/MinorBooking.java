package PackageActorsAndObjects;

public class MinorBooking {
    private int id;
    private int offeringId;
    private int minorId;
    public MinorBooking(int offeringId, int minorId) {
        this.id = id;
        this.offeringId = offeringId;
        this.minorId = minorId;
    }
    public MinorBooking(int id, int offeringId, int minorId) {
        this.id = id;
        this.offeringId = offeringId;
        this.minorId = minorId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getOfferingId() {
        return offeringId;
    }
    public void setOfferingId(int offeringId) {
        this.offeringId = offeringId;
    }
    public int getminorId() {
        return minorId;
    }
    public void setminorId(int minorId) {
        this.minorId = minorId;
    }
}
