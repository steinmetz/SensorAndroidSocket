/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trabalho.protocolos.sensor.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
 

public class TCPServerSocket {

    private int port = 9000;
    private boolean listening = false;

    public TCPServerSocket(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        listening = true;
        do {
            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                System.err.println("Could not listen on port:" + port);
                System.exit(1);
            }

            Socket socket = null;

            try {
                socket = serverSocket.accept();

                PrintWriter out = new PrintWriter(socket.getOutputStream(),
                        true);
                out.println(2);
                out.close();
            } catch (IOException e) {
                System.exit(1);
            }
            socket.close();
            serverSocket.close();
        } while (listening);

    }

    public void stop() {
        listening = false;
    }
}
