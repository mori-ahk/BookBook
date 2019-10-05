package Model.NetworkModel;

import Model.Appointment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Request implements Serializable {
    private String name;
    private RequestDestination destination;
    private Appointment appointment;
    private String patientID;

    public Appointment getAppointment() {
        return appointment;
    }

    public Request(String name, Appointment appointment) {
        this.name = name;
        this.appointment = appointment;
        this.destination = setDestination(appointment.getID());
    }

    public Request(String name, Appointment appointment, String patientID) {
        this.name = name;
        this.appointment = appointment;
        this.destination = setDestination(appointment.getID());
        this.patientID = patientID;
    }

    public Request(String name) {
        this.name = name;
        this.destination = RequestDestination.ALL;
    }

    public Request(String name, String patientID) {
        this.name = name;
        this.patientID = patientID;
        this.destination = RequestDestination.ALL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RequestDestination getDestination() {
        return destination;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public void putDestination(String patientID) {
        switch (patientID.substring(0, 3)) {
            case "MTL":
                this.destination = RequestDestination.MTL;
            case "QUE":
                this.destination = RequestDestination.QUE;
            case "SHE":
                this.destination = RequestDestination.SHE;
            default:
                this.destination = RequestDestination.ALL;
        }
    }

    public RequestDestination setDestination(String appointmentID) {
        if(appointmentID == null) return RequestDestination.ALL;
        switch (appointmentID.substring(0, 3)) {
            case "MTL":
                return RequestDestination.MTL;
            case "QUE":
                return RequestDestination.QUE;
            case "SHE":
                return RequestDestination.SHE;
            default:
                return RequestDestination.ALL;
        }
    }

    public byte[] getBytes(Request request) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(request);
        oos.flush();
        byte[] bytes = bos.toByteArray();
        return bytes;
    }

    @Override
    public String toString() {
        return "Request {" +
                "name = '" + name + '\'' +
                ", destination = " + destination +
                ", appointment = " + appointment +
                ", patientID = '" + patientID + '\'' +
                '}';
    }
}
