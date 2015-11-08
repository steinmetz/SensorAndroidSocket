/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trabalho.protocolos.sensor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClientSocket {

    private String serverHostname;
    private int serverPort;
    Socket echoSocket = null;

    public TCPClientSocket(String serverHostname, int serverPort) {
        this.serverHostname = serverHostname;
        this.serverPort = serverPort;
    }

    public String requestValue(String value) throws IOException {
        String receiveMessage = null;
        try {
            BufferedReader in = null;
            PrintWriter out = null;

            echoSocket = new Socket(serverHostname, serverPort);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    echoSocket.getInputStream()));

            out.println(value);
            receiveMessage = in.readLine();
            out.close();
            in.close();
            echoSocket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return receiveMessage;
    }

}
