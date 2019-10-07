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
                return new Result(ResultStatus.FAILURE, "There is already an appointment with this appointment ID");
            } else {
                database.get(appointmentType).put(appointment.getID(), appointment);
                return new Result(ResultStatus.SUCCESS, "Appointment has been successfully added"); }
        } else {
            HashMap<String, Appointment> newValue = new HashMap<>();
            newValue.put(appointment.getID(), appointment);
            database.put(appointmentType, newValue);
            return new Result(ResultStatus.SUCCESS, "Appointment has been successfully added");
        }
    }

    public Result removeAppointment(Request request) {
        ArrayList<Client> clients = new ArrayList<>();
        Appointment appointment = request.getAppointment();
        AppointmentType appointmentType = appointment.getAppointmentType();
        String appointmentID = appointment.getID();
        if(database.containsKey(appointmentType)) {
            if(database.get(appointmentType).containsKey(appointmentID)) {
                clients = database.get(appointmentType).get(appointmentID).getPatients();
                database.get(appointmentType).remove(appointmentID);
                HashMap<String, Appointment> appointmentHashMap = database.get(appointmentType);

                for (HashMap.Entry<String, Appointment> appointmentEntry : appointmentHashMap.entrySet()) {
                    if (isInFutre(appointmentEntry.getKey(), appointmentID)) {
                        if (appointmentEntry.getValue().doesHaveCapacity()) {
                            for(int i = 0; i < appointmentEntry.getValue().getCurrentCapacity() && clients.size() != 0; i++) {
                                appointmentEntry.getValue().addPatient(clients.get(clients.size() - 1));
                                clients.remove(clients.size() - 1);
                                clients.trimToSize();
                            }
                        }
                    }
                }

                return new Result(ResultStatus.SUCCESS, "The appointment has been removed successfully");
            } else {  return new Result(ResultStatus.FAILURE, "There is no appointment with this appointment ID"); }
        } else { return new Result(ResultStatus.FAILURE, "There is no appointment with this appointment type"); }
    }

    public Result listAppointmentAvailability(Request request) {
        Result result = new Result();
        HashMap<String, Appointment> appointmentHashMap = database.get(request.getAppointment().getAppointmentType());
        ArrayList<Appointment> appointments = new ArrayList<>();
        for(HashMap.Entry<String, Appointment> a : appointmentHashMap.entrySet()) {
            appointments.add(a.getValue());
        }
        result.setPayload(appointments);
        result.setResultStatus(ResultStatus.SUCCESS);
        return result;
    }

    public synchronized Result bookAppointment(Request request) {
        Client patient = new Client(request.getPatientID());
        Appointment appointment = request.getAppointment();
        AppointmentType appointmentType = appointment.getAppointmentType();
        String appointmentID = appointment.getID();
        if(database.containsKey(appointmentType)) {
            if (database.get(appointmentType).containsKey(appointmentID)) {
                if (!database.get(appointmentType).get(appointmentID).getPatients().contains(patient)) {
                    if (database.get(appointmentType).get(appointmentID).getMaxCapacity() >
                            database.get(appointmentType).get(appointmentID).getPatients().size()) {
                        database.get(appointmentType).get(appointmentID).addPatient(patient);
                        return new Result(ResultStatus.SUCCESS, "Appointment has been successfully booked.");
                    } else { return new Result(ResultStatus.FAILURE, "There is no enough capacity for that appointment."); }
                } else { return new Result(ResultStatus.FAILURE, "There is already a patient with the same appointment type and ID."); }
            } else { return new Result(ResultStatus.FAILURE, "There is no appointment matching this appointment ID."); }
        } else { return new Result(ResultStatus.FAILURE, "There is no appointment matching with this appointment type."); }
    }

    public Result getAppointmentSchedule(Request request) {
        Client patient = new Client(request.getPatientID());
        ArrayList<Appointment> appointmentList = new ArrayList<>();

        for(HashMap.Entry<AppointmentType, HashMap<String, Appointment>> db : database.entrySet()) {
            HashMap<String, Appointment> appointmentHashMap = db.getValue();
            for(HashMap.Entry<String, Appointment> appointments : appointmentHashMap.entrySet()) {
                for(Client c : appointments.getValue().getPatients()) {
                    if(c.equals(patient)) appointmentList.add(appointments.getValue());
                }
            }
        }

        Result result = new Result();
        result.setPayload(appointmentList);
        result.setResultStatus(ResultStatus.SUCCESS);
        if(appointmentList.isEmpty()) result.setMessage("We did not find any appointments for this patient ID.");
        else result.setMessage("We found" + appointmentList.size() + " appointments for this patient ID");
        return result;
    }

    public Result cancelAppointment(Request request) {
        Client patient = new Client(request.getPatientID());
        HashMap<String, Appointment> appointmentHashMap = database.get(request.getAppointment().getAppointmentType());
        Appointment appointment = appointmentHashMap.get(request.getAppointment().getID());
        for(Client c : appointment.getPatients()) {
            if(c.equals(patient)) {
                appointment.removePatient(patient);
                return new Result(ResultStatus.SUCCESS, "Appointment has been canceled for this patient ID.");
            }
        }

        return new Result(ResultStatus.FAILURE, "We did not find any appointment for this patient ID.");
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

    private boolean isInFutre(String toAdd, String toRemove) {
        int toAddYear = Integer.parseInt(toAdd.substring(toAdd.length() - 2));
        int toAddMonth = Integer.parseInt(toAdd.substring(toAdd.length() - 4, toAdd.length() - 2));
        int toAddDay = Integer.parseInt(toAdd.substring(toAdd.length() - 6, toAdd.length() - 4));

        int toRemoveYear = Integer.parseInt(toRemove.substring(toRemove.length() - 2));
        int toRemoveMonth = Integer.parseInt(toRemove.substring(toRemove.length() - 4, toRemove.length() - 2));
        int toRemoveDay = Integer.parseInt(toRemove.substring(toRemove.length() - 6, toRemove.length() - 4));

        if(toAddYear < toRemoveYear) return false;
        else {
            if(toAddMonth < toRemoveMonth) return false;
            else {
                if(toAddDay < toRemoveDay) return false;
                else return true;
            }
        }
    }
}

