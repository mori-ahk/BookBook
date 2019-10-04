package Client;

import Interfaces.*;
import Model.AppointmentType;
import Model.Client;
import Model.NetworkModel.Result;
import java.rmi.registry.*;
import java.util.Scanner;


public class MainClient {
    public static void main(String[] args) throws Exception {

        Registry registry = LocateRegistry.getRegistry(5555);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your ID:");
        String clientID = scanner.next();
        Client client = new Client(clientID);
        int option;

        while(true) {
            System.out.println("Please choose an option from the list below:");
            System.out.println("1) Add appointment\n 2) Remove appointment\n 3) List of appointment availability\n" +
                    "4) Book appointment\n 5) Cancel appointment\n 6) Get appointment schedule");
            option = scanner.nextInt();
            if(option < 1 || option > 6) System.out.println("Please choose a valid option");
            else if(!authenticateClient(client, option)) System.out.println("Sorry! you need admin access for this option");
            else break;
        }

        String appointmentID = "MTLA100919";
        String ap = "SHEA100919";
        Result result;
        Admin admin;
        Patient patient;

        switch (client.getLocation()) {
            case MTL:
                admin = (Admin) registry.lookup("MTL");
                patient = (Patient) registry.lookup("MTL");
                break;
            case QUE:
                admin = (Admin) registry.lookup("QUE");
                patient = (Patient) registry.lookup("QUE");
                break;
            case SHE:
                admin = (Admin) registry.lookup("SHE");
                patient = (Patient) registry.lookup("SHE");
                break;
            default:
                admin = null;
                patient = null;
        }


        result = admin.addAppointment(appointmentID, AppointmentType.PHYSICIAN, 2);
        System.out.println(result.getResultStatus() + " " + result.toString());
        result = admin.addAppointment(ap, AppointmentType.PHYSICIAN, 2);
        System.out.println(result.getResultStatus() + " " + result.toString());

    }

    private static boolean authenticateClient(Client client, int option) {
        if (option == 1 || option == 2 || option == 3) {
            if (client.isAdmin()) return true;
            else return false;
        }
        return true;
    }


}
