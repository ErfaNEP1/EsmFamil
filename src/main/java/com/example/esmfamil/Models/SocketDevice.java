package com.example.esmfamil.Models;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class SocketDevice {
    private final Socket socket;
    private String session_id;
    private final String IP = "localhost:";
    private final int PORT = 9999;

    private SocketDevice() throws URISyntaxException {
        socket = IO.socket("http://" + IP + PORT);
    }

    public static SocketDevice getInstance() throws URISyntaxException {
        if (ref == null)
            ref = new SocketDevice();
        return ref;
    }

    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }

    public Socket getSocket() {
        return socket;
    }

    public void open(){
        this.socket.open();
    }

    public void close(){
        this.socket.close();
    }

    private static SocketDevice ref;

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
