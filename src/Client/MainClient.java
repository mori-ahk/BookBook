package Client;

import Interfaces.*;
import Model.AppointmentType;
import Model.NetworkModel.Result;
import Model.NetworkModel.ResultStatus;

import java.rmi.registry.*;


public class MainClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry(5555);
        String appointmentID = "MTLA100919";
        String ap = "SHEA100919";
        Result result = new Result();
        Admin obj = (Admin) registry.lookup("MTL");

        result = obj.addAppointment(appointmentID, AppointmentType.PHYSICIAN, 2);
        System.out.println(result.getResultStatus() + " " + result.getID());
        result = obj.addAppointment(ap, AppointmentType.PHYSICIAN, 2);
        System.out.println(result.getResultStatus() + " " + result.getID());

    }
}
