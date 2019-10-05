import Model.Appointment;
import Model.AppointmentType;
import Model.NetworkModel.Request;
import Router.Router;
import Server.InternalCommunication.Connection;
import Server.Server;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;

public class MainServer {
    public static ArrayList<Server> servers = new ArrayList<Server>();

    public static void main(String[] args) throws Exception {
        ArrayList<Connection> connections = initConnections();
        Server MTLServer = new Server("MTL", connections);
        Server QUEServer = new Server("QUE", connections);
        Server SHEServer = new Server("SHE", connections);

        servers.add(MTLServer);
        servers.add(QUEServer);
        servers.add(SHEServer);

        MTLServer.setDatabase(initMTLDatabase());
        QUEServer.setDatabase(initQUEDatabase());
        SHEServer.setDatabase(initSHEDatabase());

        Router MTLRouter = new Router(MTLServer);
        Router QUERouter = new Router(QUEServer);
        Router SHERouter = new Router(SHEServer);

        MTLRouter.setServers(servers);
        QUERouter.setServers(servers);
        SHERouter.setServers(servers);

        Registry registry = LocateRegistry.createRegistry(5555);
        registry.bind("MTL", MTLRouter);
        registry.bind("QUE", QUERouter);
        registry.bind("SHE", SHERouter);

        System.out.println("Server is up & running");
    }

    private static ArrayList<Connection> initConnections() throws UnknownHostException {
        ArrayList<Connection> connections = new ArrayList<>();
        connections.add(new Connection("MTL", InetAddress.getLocalHost(), 5000));
        connections.add(new Connection("QUE", InetAddress.getLocalHost(), 5005));
        connections.add(new Connection("SHE", InetAddress.getLocalHost(), 5010));
        return connections;
    }

    private static HashMap<AppointmentType, HashMap<String, Appointment>> initMTLDatabase() {
        HashMap<AppointmentType, HashMap<String, Appointment>> database = new HashMap<>();
        HashMap<String, Appointment> value = new HashMap<>();
        value.put("MTLA100919", new Appointment(AppointmentType.PHYSICIAN, "MTLA100919", 3));
        database.put(AppointmentType.PHYSICIAN, value);
        value = new HashMap<>();
        value.put("MTLE101119", new Appointment(AppointmentType.DENTAL, "MTLE101119", 1));
        database.put(AppointmentType.DENTAL, value);
        value = new HashMap<>();
        value.put("MTLM051019", new Appointment(AppointmentType.SURGEON, "MTLM051019", 2));
        database.put(AppointmentType.SURGEON, value);
        return database;
    }

    private static HashMap<AppointmentType, HashMap<String, Appointment>> initQUEDatabase() {
        HashMap<AppointmentType, HashMap<String, Appointment>> database = new HashMap<>();
        HashMap<String, Appointment> value = new HashMap<>();
        value.put("QUEM100919", new Appointment(AppointmentType.DENTAL, "QUEA100919", 5));
        database.put(AppointmentType.DENTAL, value);
        value = new HashMap<>();
        value.put("QUEA091119", new Appointment(AppointmentType.PHYSICIAN, "QUEA091119", 2));
        database.put(AppointmentType.PHYSICIAN, value);
        value = new HashMap<>();
        value.put("QUEM051019", new Appointment(AppointmentType.SURGEON, "QUEM051019", 2));
        database.put(AppointmentType.SURGEON, value);
        value = new HashMap<>();

        return database;
    }

    private static HashMap<AppointmentType, HashMap<String, Appointment>> initSHEDatabase() {
        HashMap<AppointmentType, HashMap<String, Appointment>> database = new HashMap<>();
        HashMap<String, Appointment> value = new HashMap<>();
        value.put("SHEM051019", new Appointment(AppointmentType.SURGEON, "SHEM051019", 2));
        database.put(AppointmentType.SURGEON, value);
        value = new HashMap<>();
        value.put("SHEM051019", new Appointment(AppointmentType.DENTAL, "SHEM051019", 2));
        database.put(AppointmentType.DENTAL, value);
        value = new HashMap<>();
        value.put("SHEM051019", new Appointment(AppointmentType.PHYSICIAN, "SHEE150219", 2));
        database.put(AppointmentType.PHYSICIAN, value);
        return database;
    }
}

