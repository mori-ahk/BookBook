package Server.InternalCommunication;

import java.net.InetAddress;

public class Connection {
    private String name;
    private InetAddress IP;
    private int port;


    public Connection(String name, InetAddress IP, int port) {
        this.name = name;
        this.IP = IP;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InetAddress getIP() {
        return IP;
    }

    public void setIP(InetAddress IP) {
        this.IP = IP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
