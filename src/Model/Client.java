package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Client implements Serializable {
    private String ID;
    private ClientLocation location;

    public Client(String ID) {
        this.ID = ID;
        this.location = setLocation();
    }

    public ClientLocation getLocation() {
        return location;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isAdmin() {
        return ID.charAt(3) == 'A';
    }

    private ClientLocation setLocation() {
        switch (ID.substring(0,3)) {
            case "MTL":
                return ClientLocation.MTL;
            case "QUE":
                return ClientLocation.QUE;
            case "SHE":
                return ClientLocation.SHE;
            default:
                return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return Objects.equals(ID, client.ID) &&
                location == client.location;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, location);
    }
}
