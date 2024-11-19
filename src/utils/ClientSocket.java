package utils;

import java.io.*;
import java.net.Socket;

public class ClientSocket {
    private static final String ipAddress = "localhost";
    private static final int port = 8080;

    private final Socket sock;
    private final InputStream in;
    private final OutputStream out;

    public ClientSocket() throws IOException {
        sock = new Socket(ipAddress, port);
        in = sock.getInputStream();
        out = sock.getOutputStream();
    }

    public ObjectInputStream getObjectInputStream() throws IOException {
        return new ObjectInputStream(in);
    }

    public ObjectOutputStream getObjectOutputStream() throws IOException {
        return new ObjectOutputStream(out);
    }
}
