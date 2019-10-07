package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Appointment implements Serializable {
    AppointmentType appointmentType;
    String ID;
    ArrayList<Client> patients;
    int maxCapacity;
    int currentCapacity;

    private String location;
    private AppointmentStatus appointmentStatus;
    private String date;

    public Appointment(AppointmentType appointmentType, String ID, int maxCapacity) {
        this.appointmentType = appointmentType;
        this.ID = ID;
        this.maxCapacity = maxCapacity;
        this.location = ID.substring(0, 3);
        this.appointmentStatus = initAppointmentStatus(ID);
        this.date = ID.substring(4);
        this.patients = new ArrayList<>();
        this.currentCapacity = maxCapacity - patients.size();
    }

    public Appointment(AppointmentType appointmentType, String ID) {
        this.appointmentType = appointmentType;
        this.ID = ID;
        this.location = ID.substring(0, 3);
        this.appointmentStatus = initAppointmentStatus(ID);
        this.date = ID.substring(4);
        this.patients = new ArrayList<>();
        this.currentCapacity = maxCapacity - patients.size();
    }

    public Appointment(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
        this.patients = new ArrayList<>();
        this.currentCapacity = maxCapacity - patients.size();
    }

    public String getLocation() {
        return location;
    }

    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    public String getDate() {
        return date;
    }

    public AppointmentType getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<Client> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<Client> patients) {
        this.patients = patients;
    }

    public void addPatient(Client patient) {
        patients.add(patient);
        currentCapacity = maxCapacity - patients.size();
    }

    public void removePatient(Client patient) {
        patients.remove(patient);
        currentCapacity = maxCapacity - patients.size();
    }

    public boolean doesHaveCapacity() {
        return maxCapacity > patients.size();
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    private AppointmentStatus initAppointmentStatus(String ID) {
        switch (ID.charAt(3)) {
            case 'M':
                return AppointmentStatus.MORNING;
            case 'A':
                return AppointmentStatus.AFTERNOON;
            case 'E':
                return AppointmentStatus.EVENING;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "Appointment { " +
                "appointmentType = " + appointmentType +
                ", ID = '" + ID + '\'' +
                ", maxCapacity = " + maxCapacity +
                ", currentCapacity = " + currentCapacity +
                ", appointmentStatus = " + appointmentStatus +
                '}';
    }


}
