package com.trabalho.protocolos.sensor;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by charles on 08/11/15.
 */
public class Controle {

    private int[] possiveisCores;
    private int[][] ranges;

    Context context;

    public Controle(Context context) {
        this.context = context;
        init();
    }


    public void start(Node sensor, Node atuador) {
        TCPClientSocket tcpSocketSensor = new TCPClientSocket(sensor.host, sensor.port);
        TCPClientSocket tcpSocketAtuador = new TCPClientSocket(atuador.host, atuador.port);
        try {
            String text = "";
            int erros = 0;
            long start, end;
            for (int i = 0; i < 700; i++) {
                start = System.nanoTime();
                String sensorVal = tcpSocketSensor.requestValue("0");
                if (sensorVal != null && sensorVal.trim().length() > 0) {
                    tcpSocketAtuador.requestValue(sensorVal);
                    end = System.nanoTime();
                    text += end - start + "\r\n";
                } else {
                    erros++;
                }
            }
            save(text + "\r\n\r\n" + erros, System.currentTimeMillis()+"log.txt");
        } catch (Exception ex) {
        }
    }

    public void save(String sBody, String sFileName) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "TrabProtocolos");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void confControle(int[] cores) {
        this.possiveisCores = cores;
    }

    public void init() {
        possiveisCores = new int[6];
        possiveisCores[0] = Color.BLACK;
        possiveisCores[1] = Color.GRAY;
        possiveisCores[2] = Color.BLUE;
        possiveisCores[3] = Color.GREEN;
        possiveisCores[4] = Color.YELLOW;
        possiveisCores[5] = Color.RED;


        ranges = new int[6][2];
        int initialValue = 90;
        for (int i = 0; i < ranges.length; i++) {
            ranges[i][0] = initialValue;
            initialValue -= 30;
            ranges[i][1] = initialValue;
        }
    }

}
