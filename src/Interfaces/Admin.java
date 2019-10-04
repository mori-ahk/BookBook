package Interfaces;

import Model.*;
import Model.NetworkModel.Result;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Admin extends Remote {

    Result addAppointment(String appointmentID, AppointmentType appointmentType, int capacity) throws RemoteException;
    Result removeAppointment(String appointmentID, AppointmentType appointmentType) throws RemoteException;
    Result listAppointmentAvailability (AppointmentType appointmentType) throws RemoteException;
}
