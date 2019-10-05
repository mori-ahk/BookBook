package Server.InternalCommunication;

import Model.Appointment;
import Model.NetworkModel.Request;
import Model.NetworkModel.Result;
import Server.Server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer {

    private Connection connection;
    private Request clientRequest;
    private Server server;

    Runnable receiver = () -> {
        receive();
    };

    Runnable sender = () -> {
//        sendRequest(clientRequest);
    };

    Thread receiverThread = new Thread(receiver);
    Thread senderThread = new Thread(sender);

    public UDPServer(Connection connection, Request clientRequest, Server server) {
        this.connection = connection;
        this.clientRequest = clientRequest;
        this.server = server;
        receiverThread.start();
        senderThread.start();
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Request getClientRequest() {
        return clientRequest;
    }

    public void setClientRequest(Request clientRequest) {
        this.clientRequest = clientRequest;
    }

    public Result sendRequest(Request clientRequest) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            byte[] data = clientRequest.getBytes(clientRequest);
            DatagramPacket request = new DatagramPacket(data, data.length, connection.getIP(), connection.getPort());
            socket.send(request);
            System.out.println("Request message sent from the client to server with port number " + connection.getPort());
            byte[] buffer = new byte[1024];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            socket.receive(reply);
            Result result = parseResult(reply);
            System.out.println("Reply received from the server with port number " + connection.getPort());
            return result;
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (socket != null)
                socket.close();
        }
        return null;
    }

    public void receive() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(connection.getPort());
            setupReceiver(socket);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (socket != null)
                socket.close();
        }
    }


    private void setupReceiver(DatagramSocket socket) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            socket.receive(request);
            Request parsedRequest = parseRequest(request);
            Result response = server.handleRequest(parsedRequest);
            byte[] replyBytes = response.getBytes(response);
            DatagramPacket reply = new DatagramPacket(replyBytes, replyBytes.length, request.getAddress(),
                    request.getPort());
            socket.send(reply);
        }
    }

    private Request parseRequest(DatagramPacket request) throws IOException {
        byte[] data = request.getData();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(in);
        try {
            Request parsedRequest = (Request) ois.readObject();
//            System.out.println("Request object received = " + parsedRequest.toString());
            return parsedRequest;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
        return null;
    }

    private Result parseResult(DatagramPacket result) throws IOException {
        byte[] data = result.getData();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(in);
        try {
            Result parsedResult = (Result) ois.readObject();
//            System.out.println("Result object received = " + parsedResult.toString());
            return parsedResult;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
        return null;
    }
}
