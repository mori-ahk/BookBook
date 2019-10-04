package Interfaces;

import Model.*;
import Model.NetworkModel.Result;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Patient extends Remote {

    Result bookAppointment (String patientID, String appointmentID, AppointmentType appointmentType) throws RemoteException;
    Result cancelAppointment (String patientID, String appointmentID, AppointmentType appointmentType) throws RemoteException;
    Result getAppointmentSchedule (String patientID) throws  RemoteException;
}
