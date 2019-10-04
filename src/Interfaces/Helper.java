package Interfaces;

import Model.NetworkModel.Result;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Helper extends Remote {
    Result getID(String ID) throws RemoteException;
}
