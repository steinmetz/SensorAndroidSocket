package com.trabalho.protocolos.sensor;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by charles on 23/10/15.
 */
public class Sensor implements SensorEventListener {

    private float value = 0;
    private SensorManager sManager;
    private TextView txtLog;
    private TextView txtValue;
    private boolean runningServer = true;


    public Sensor(Context ctx) {
        sManager = (SensorManager) ctx.getSystemService(ctx.SENSOR_SERVICE);
    }

    public Sensor(Context ctx, TextView txtLog, TextView txtValue) {
        sManager = (SensorManager) ctx.getSystemService(ctx.SENSOR_SERVICE);
        this.txtLog = txtLog;
        this.txtValue = txtValue;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Orientation X (Roll)
        if (Math.abs(value - event.values[2]) > 2) {
            value = event.values[2];
            if (txtValue != null) {
                txtValue.setText("X: " + getValue());
            }
        }

    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

    }

    protected void startSensor() {
        sManager.registerListener(this, sManager.getDefaultSensor(android.hardware.Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void stopSensor() {
        sManager.unregisterListener(this);
    }

    public float getValue() {
        return value;
    }

    public void startServer(int port, String type) {
        runningServer = true;
        if (type.equals("TCP")) {
            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                txtLog.setText("Could not listen on port:" + port);
                System.exit(1);
            }

            Socket socket = null;

            try {
                while (runningServer) {
                    socket = serverSocket.accept();

                    PrintWriter out = new PrintWriter(socket.getOutputStream(),
                            true);
                    out.println(getValue());
                    out.close();
                }
            } catch (IOException e) {
                System.exit(1);
            }
            try {
                socket.close();
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // UDP
        } else {
            try {
                DatagramSocket serverSocket = new DatagramSocket(port);
                byte[] receiveData = new byte[10];
                byte[] sendData = new byte[10];
                while(runningServer)
                {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    String sentence = new String( receivePacket.getData());
                    InetAddress IPAddress = receivePacket.getAddress();
                    int portCli = receivePacket.getPort();
                    sendData = String.valueOf(getValue()).getBytes();
                    DatagramPacket sendPacket =
                            new DatagramPacket(sendData, sendData.length, IPAddress, portCli);
                    serverSocket.send(sendPacket);
                }
                serverSocket.close();
            } catch (Exception e) {
                txtLog.setText(e.getMessage());
            }
        }
    }

    public void stopServer() {
        runningServer = false;
    }

}
