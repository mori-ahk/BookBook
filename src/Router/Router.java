package Router;

import Interfaces.*;
import Logger.Logger;
import Model.Appointment;
import Model.NetworkModel.*;
import Server.InternalCommunication.*;
import Model.AppointmentType;
import Server.Server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Router extends UnicastRemoteObject implements Admin, Patient, Helper {
    private Server server;
    private UDPServer udpServer;
    private ArrayList<Server> servers = new ArrayList<Server>();

    public Router(Server server) throws RemoteException {
        super();
        this.server = server;
    }

    @Override
    public Result addAppointment(String appointmentID, AppointmentType appointmentType, int capacity) throws RemoteException {
        Request clientRequest = new Request("addAppointment", new Appointment(appointmentType, appointmentID, capacity));
        return hitProperServer(clientRequest);
    }

    @Override
    public Result removeAppointment(String appointmentID, AppointmentType appointmentType) throws RemoteException {
        Request clientRequest = new Request("removeAppointment", new Appointment(appointmentType, appointmentID));
        if(server.getServerName().equals(clientRequest.getDestination().name())) {
            //call the local server
        } else {
            //call udpServer
        }
        return null;
    }

    @Override
    public Result listAppointmentAvailability(AppointmentType appointmentType) throws RemoteException {
        Request clientRequest = new Request("listAppointmentAvailability", new Appointment(appointmentType));
        Result result;
        ArrayList<Result> results = new ArrayList<>();
        //call the local server
        results.add(server.handleRequest(clientRequest));
        //call upd servers
        for(Server s : servers) {
            if(s.getServerName().equals(server.getServerName())) continue;
            udpServer = new UDPServer(getProperConnection(s), clientRequest, s);
            results.add(udpServer.sendRequest(clientRequest));
        }

        ArrayList<Appointment> allAppointments = new ArrayList<>();
        for(Result r : results) {
            allAppointments.addAll(r.getPayload());
        }

        result = new Result(ResultStatus.SUCCESS, allAppointments);
        result.setMessage("we found " + allAppointments.size() + " appointments for appointment type " + appointmentType);
        return result;
    }

    @Override
    public Result getID(String ID) throws RemoteException {
        return null;
    }

    @Override
    public Result bookAppointment(String patientID, String appointmentID, AppointmentType appointmentType) throws RemoteException {
        Request clientRequest = new Request("bookAppointment", new Appointment(appointmentType, appointmentID), patientID);
        return hitProperServer(clientRequest);
    }

    @Override
    public Result cancelAppointment(String patientID, String appointmentID, AppointmentType appointmentType) throws RemoteException {
        Request clientRequest = new Request("cancelAppointment", new Appointment(appointmentType, appointmentID), patientID);
        return hitProperServer(clientRequest);
    }

    @Override
    public Result getAppointmentSchedule(String patientID) throws RemoteException {
        Request clientRequest = new Request("getAppointmentSchedule", patientID);
        clientRequest.putDestination(patientID);
        Result result = null;
        ArrayList<Result> results = new ArrayList<>();
        //call the local server
        results.add(server.handleRequest(clientRequest));
        //call upd servers
        for(Server s : servers) {
            if(s.getServerName().equals(server.getServerName())) continue;
            udpServer = new UDPServer(getProperConnection(s), clientRequest, s);
            results.add(udpServer.sendRequest(clientRequest));
        }

        ArrayList<Appointment> allAppointments = new ArrayList<>();
        for(Result r : results) {
            allAppointments.addAll(r.getPayload());
        }

        result = new Result(ResultStatus.SUCCESS, allAppointments);
        result.setMessage("we found " + allAppointments.size() + " appointments for patient ID: " + patientID);
        return result;
    }


    private Connection getProperConnection(Request clientRequest){
        for (Connection c: server.getConnections()) {
            if(c.getName().equals(clientRequest.getDestination().name())) {
                return c;
            }
        }
        return null;
    }

    private Connection getProperConnection(Server s) {
        for(Connection c : server.getConnections()) {
            if (c.getName().equals(s.getServerName())) {
                return c;
            }
        }
        return null;
    }

    private Server getProperServer(Connection connection) {
        for(Server s : servers) {
            if(s.getServerName().equals(connection.getName())) {
                return s;
            }
        }
        return null;
    }

    private Result hitProperServer(Request clientRequest) {
        Result result = null;
        if(server.getServerName().equals(clientRequest.getDestination().name())) {
            //call the local server
            result = server.handleRequest(clientRequest);
        } else {
            //call udp server
            setupUDPServer(clientRequest);
            result = udpServer.sendRequest(clientRequest);
        }
        try {
            Logger.getInstance().log(result, clientRequest, "Client.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void setupUDPServer(Request clientRequest) {
        Connection properConnection = getProperConnection(clientRequest);
        Server properServer = getProperServer(properConnection);
        udpServer = new UDPServer(properConnection, clientRequest, properServer);
    }

    public ArrayList<Server> getServers() {
        return servers;
    }

    public void setServers(ArrayList<Server> servers) {
        this.servers = servers;
    }


}
