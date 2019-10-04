package Model;

import java.io.Serializable;

public class Client implements Serializable {
    String ID;

    public Client(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isAdmin() {
        return ID.charAt(4) == 'A';
    }
}
