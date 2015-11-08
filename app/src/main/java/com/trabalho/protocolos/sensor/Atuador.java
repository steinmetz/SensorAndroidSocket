package com.trabalho.protocolos.sensor;

import android.content.Context;
import android.graphics.Color;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by charles on 23/10/15.
 */
public class Atuador {

    private TextView txtLog;
    private TextView txtValue;
    private boolean runningServer = true;
    private int color = Color.WHITE;


    public Atuador(Context ctx, TextView txtLog, TextView txtValue) {
        this.txtLog = txtLog;
        this.txtValue = txtValue;
    }

    public Atuador() {
    }


    public void startServer(int port, String type) {
        runningServer = true;
        if (type.equals("TCP")) {

            try {
                while (runningServer) {
                    ServerSocket serverSocket = null;

                    serverSocket = new ServerSocket(port);
                    Socket clientSocket = null;

                    clientSocket = serverSocket.accept();

                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
                            true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));

                    String sentence = in.readLine();
                    out.println(sentence);
                    atuar(sentence);

                    out.close();
                    in.close();
                    clientSocket.close();
                    serverSocket.close();
                }

            } catch (Exception e) {
                Log.i("charles", e.getMessage());
            }

            // UDP
        } else {
            try {

                byte[] receiveData = new byte[10];
                byte[] sendData = new byte[10];
                DatagramSocket serverSocket = new DatagramSocket(port);
                while (runningServer) {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    String sentence = new String(receivePacket.getData());

                    atuar(sentence);
                    InetAddress IPAddress = receivePacket.getAddress();
                    int portCli = receivePacket.getPort();
                    sendData = sentence.getBytes();
                    DatagramPacket sendPacket =
                            new DatagramPacket(sendData, sendData.length, IPAddress, portCli);
                    serverSocket.send(sendPacket);
                }
                serverSocket.close();

            } catch (Exception e) {
                Log.i("charles", e.getMessage());
            }
        }
    }

    public void stopServer() {
        runningServer = false;
    }


    public void atuar(String value) {
        try {
            color = Integer.parseInt(value);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public int getColor() {
        return color;
    }
}
