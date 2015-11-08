package com.trabalho.protocolos.sensor;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private int valueIndex = 2;


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
        if (Math.abs(value - event.values[valueIndex]) > 2) {
            value = event.values[valueIndex];
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

            Gson gson = new Gson();
            try {
                while (runningServer) {
                    serverSocket = new ServerSocket(port);

                    Socket socket = null;

                    socket = serverSocket.accept();

                    PrintWriter out = new PrintWriter(socket.getOutputStream(),
                            true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

                    String sentence = in.readLine();
                    Message msg = gson.fromJson(sentence, Message.class);
                    if(msg.type == Message.CONTROLE){
                        try {
                            setValueIndex(new Double((Double) msg.value).intValue());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    Message<Message.Value> response = new Message();
                    response.setValue(valueIndex, getValue());
                    out.println(gson.toJson(response));
                    out.close();
                    socket.close();
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
                    InetAddress IPAddress = receivePacket.getAddress();
                    int portCli = receivePacket.getPort();
                    sendData = String.valueOf(getValue()).getBytes();
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

    public void setValueIndex(int valueIndex){
        this.valueIndex = valueIndex;
        value =0;
    }

    public void stopServer() {
        runningServer = false;
    }

}
