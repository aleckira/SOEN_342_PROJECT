package PackageActorsAndObjects;

public class ClientBooking {
    private int id;
    private int offeringId;
    private int clientId;
    public ClientBooking(int offeringId, int clientId) {
        this.id = id;
        this.offeringId = offeringId;
        this.clientId = clientId;
    }
    public ClientBooking(int id, int offeringId, int clientId) {
        this.id = id;
        this.offeringId = offeringId;
        this.clientId = clientId;
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
    public int getClientId() {
        return clientId;
    }
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
