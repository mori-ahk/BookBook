package Client;

import Interfaces.*;
import Model.Appointment;
import Model.AppointmentType;
import Model.Client;
import Model.NetworkModel.Result;
import java.rmi.registry.*;
import java.sql.SQLOutput;
import java.util.Scanner;


public class MainClient {
    public static String patientID;
    public static String appointmentID;
    public static AppointmentType appointmentType;
    public static int capacity;
    public static Result result;
    public static void main(String[] args) throws Exception {

        Registry registry = LocateRegistry.getRegistry(5555);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your ID: ");
        String clientID = scanner.next();
        Client client = new Client(clientID);

        Admin admin;
        Patient patient;
        int option;
        while (true) {
            while (true) {
                System.out.println("Please choose an option from the list below:");
                System.out.println("1) Add appointment\n2) Remove appointment\n3) List of appointment availability\n" +
                        "4) Book appointment\n5) Get appointment schedule \n6) Cancel appointment");
                option = scanner.nextInt();
                if (option < 1 || option > 6) System.out.println("Please choose a valid option");
                else if (!authenticateClient(client, option))
                    System.out.println("Sorry! you need admin access for this option");
                else break;
            }

            handleOption(option, scanner);

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

            callProperFunction(option, admin, patient);
            parseResult(result);
        }

    }

    private static boolean authenticateClient(Client client, int option) {
        if (option == 1 || option == 2 || option == 3) {
            if (client.isAdmin()) return true;
            else return false;
        }
        return true;
    }

    private static void handleOption(int option, Scanner scanner) {
        switch (option) {
            case 1:
                System.out.print("Please enter the appointment ID: ");
                appointmentID = scanner.next();
                handleAppointmentType(scanner);
                System.out.print("Please enter the capacity: ");
                capacity = scanner.nextInt();
                break;
            case 2:
                System.out.print("Please enter the appointment ID: ");
                appointmentID = scanner.next();
                handleAppointmentType(scanner);
                break;
            case 3:
                handleAppointmentType(scanner);
                break;
            case 4:
            case 6:
                System.out.print("Please enter the patient ID: ");
                patientID = scanner.next();
                System.out.print("Please enter the appointment ID: ");
                appointmentID = scanner.next();
                handleAppointmentType(scanner);
                break;
            case 5:
                System.out.print("Please enter the patient ID: ");
                patientID = scanner.next();
                break;
        }
    }

    private static void handleAppointmentType(Scanner scanner) {
        System.out.println("Please choose a appointment type:");
        System.out.println("1) Physician\n2) Surgeon\n3) Dental");
        switch (scanner.nextInt()) {
            case 1:
                appointmentType = AppointmentType.PHYSICIAN;
                break;
            case 2:
                appointmentType = AppointmentType.SURGEON;
                break;
            case 3:
                appointmentType = AppointmentType.DENTAL;
                break;
        }
    }

    private static Result callProperFunction(int option, Admin admin, Patient patient) throws Exception {
        switch (option) {
            case 1:
                result = admin.addAppointment(appointmentID, appointmentType, capacity);
                break;
            case 2:
                result = admin.removeAppointment(appointmentID, appointmentType);
                break;
            case 3:
                result = admin.listAppointmentAvailability(appointmentType);
                break;
            case 4:
                result = patient.bookAppointment(patientID, appointmentID, appointmentType);
                break;
            case 5:
                result = patient.getAppointmentSchedule(patientID);
                break;
            case 6:
                result = patient.cancelAppointment(patientID, appointmentID, appointmentType);
                break;
            default:
                return null;
        }
        return null;
    }

    private static void parseResult(Result result) {
        if(result == null) return;
        System.out.println(result.getMessage());
        System.out.println(result.toString());
        if(result.getPayload() != null) {
            for(Appointment appointment : result.getPayload()) {
                System.out.println(appointment.toString());
            }
        }
    }


}
