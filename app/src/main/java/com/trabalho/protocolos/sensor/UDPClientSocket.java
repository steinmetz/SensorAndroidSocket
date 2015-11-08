/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trabalho.protocolos.sensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 *
 * @author charles
 */
public class UDPClientSocket {
    
    private String serverHostname;
    private int serverPort;
    
    public UDPClientSocket(String serverHostname, int serverPort) {
        this.serverHostname = serverHostname;
        this.serverPort = serverPort;
    }
    
    public String requestValue(String value) throws Exception {
        String receiveMessage = null;
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(serverHostname);
            byte[] sendData = new byte[10];
            byte[] receiveData = new byte[10];            
            sendData = value.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.setSoTimeout(6000);
            clientSocket.receive(receivePacket);
            receiveMessage = new String(receivePacket.getData());
            clientSocket.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return receiveMessage;
    }
}
