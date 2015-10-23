/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trabalho.protocolos.sensor.socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author charles
 */
public class UDPServerSocket {

    private int port = 9000;

    public UDPServerSocket(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        try {
            DatagramSocket serverSocket = new DatagramSocket(port);

            byte[] receiveData;
            byte[] sendData;

            while (true) {

                receiveData = new byte[10];

                DatagramPacket receivePacket
                        = new DatagramPacket(receiveData, receiveData.length);

                serverSocket.receive(receivePacket);


                String sentence = new String(receivePacket.getData());


                InetAddress IPAddress = receivePacket.getAddress();

                int port = receivePacket.getPort();

                sendData = sentence.getBytes();


                DatagramPacket sendPacket
                        = new DatagramPacket(sendData, sendData.length, IPAddress,
                        port);

                serverSocket.send(sendPacket);

            }

        } catch (SocketException ex) {
            System.exit(1);
        }

    }
}
