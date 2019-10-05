package Server;

import Model.*;
import Model.NetworkModel.*;
import Server.InternalCommunication.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private String serverName;
    HashMap<AppointmentType, HashMap<String, Appointment>> database = new HashMap<>();
    ArrayList<Connection> connections;

    public Server(String serverName, ArrayList<Connection> connections) {
        this.serverName = serverName;
        this.connections = connections;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public HashMap<AppointmentType, HashMap<String, Appointment>> getDatabase() {
        return database;
    }

    public void setDatabase(HashMap<AppointmentType, HashMap<String, Appointment>> database) {
        this.database = database;
    }

    public Result addAppointment(Request request) {
        Appointment appointment = request.getAppointment();
        AppointmentType appointmentType = appointment.getAppointmentType();
        if(database.containsKey(appointmentType)) {
            if(database.get(appointmentType).containsKey(appointment.getID())) {
                return new Result(ResultStatus.FAILURE);
            } else {
                database.get(appointmentType).put(appointment.getID(), appointment);
                return new Result(ResultStatus.SUCCESS); }
        } else {
            HashMap<String, Appointment> newValue = new HashMap<>();
            newValue.put(appointment.getID(), appointment);
            database.put(appointmentType, newValue);
            return new Result(ResultStatus.SUCCESS);
        }
    }

    public Result removeAppointment(Request request) {
        return null;
    }

    public Result listAppointmentAvailability(Request request) {
        return null;
    }

    public Result bookAppointment(Request request) {
        return null;
    }

    public Result getAppointmentSchedule(Request request) {
        return null;
    }

    public Result cancelAppointment(Request request) {
        return null;
    }

    public Result handleRequest(Request request) {
        Result toReturn;
        switch (request.getName()) {
            case "addAppointment":
                toReturn = addAppointment(request);
                break;
            case "removeAppointment":
                toReturn = removeAppointment(request);
                break;
            case "listAppointmentAvailability":
                toReturn = listAppointmentAvailability(request);
                break;
            case "bookAppointment":
                toReturn = bookAppointment(request);
                break;
            case "getAppointmentSchedule":
                toReturn = getAppointmentSchedule(request);
                break;
            case "cancelAppointment":
                toReturn = cancelAppointment(request);
                break;
            default:
                toReturn = null;
        }
        return toReturn;
    }
}

