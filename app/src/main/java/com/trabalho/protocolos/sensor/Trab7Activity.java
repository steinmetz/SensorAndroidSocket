package com.trabalho.protocolos.sensor;

import android.graphics.Color;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Trab7Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trab7);
        LinearLayout llcores = (LinearLayout) findViewById(R.id.llcores);
        for (int i = 0; i < 100; i+=10){
            TextView tv = new TextView(this);
            tv.setText(""+getTrafficlightColor(i));
            tv.setBackgroundColor(getTrafficlightColor(i));
            llcores.addView(tv);
            Log.i("SSS", getTrafficlightColor(i)+"");
        }
    }

    public void sensor(){
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Sensor sensor = new Sensor(Trab7Activity.this);
                sensor.startSensor();
                sensor.startServer(9003, "TCP");
            }
        };

        Thread t = new Thread(run);
        t.start();
    }

    public void startAtuador(View v){
        atuador();
    }

    public void startSensor(View v){
        sensor();
    }

    public void atuador(){
        EditText edtT7PortaAt = (EditText) findViewById(R.id.edtT7PortaAt);
        final TextView txtT7Atuacao = (TextView) findViewById(R.id.txtT7Atuacao);
        final Atuador atuador = new Atuador();
        try {
            final int porta = Integer.parseInt(edtT7PortaAt.getText().toString());

            Runnable run = new Runnable() {
                @Override
                public void run() {

                    atuador.startServer(porta, "TCP");
                }
            };
            Thread t = new Thread(run);
            t.start();
            Toast.makeText(this, "Atuador rodando", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Porta inválida", Toast.LENGTH_LONG).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtT7Atuacao.setBackgroundColor(atuador.getColor());
                        }
                    });
                    SystemClock.sleep(500);
                }
            }
        }).start();
    }

    public void startTCP(final View v) {
        Runnable run = null;
        v.setEnabled(false);
        run = new Runnable() {

            @Override
            public void run() {

                startTCP();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        v.setEnabled(true);
                    }
                });
            }
        };
        new Thread(run).start();
    }

    public void startUPD() {
        UDPClientSocket udpSocketSensor = new UDPClientSocket("177.143.193.64", 9000);
        UDPClientSocket udpSocketAtuador = new UDPClientSocket("187.183.5.216", 9002);
        try {
            String text = "";
            int erros = 0;
            long start, end;
            for (int i = 0; i < 500; i++) {
                start = System.nanoTime();
                String sensorVal = udpSocketSensor.requestValue("0");
                if (sensorVal != null && sensorVal.trim().length() > 0) {
                    udpSocketAtuador.requestValue(sensorVal);
                    end = System.nanoTime();
                    text += end - start + "\r\n";
                } else {
                    erros++;
                }
            }
            save(text + "\r\n\r\n" + erros, "udpLGG5.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void startTCP() {
        TCPClientSocket tcpSocketSensor = new TCPClientSocket("177.143.193.64", 9000);
        TCPClientSocket tcpSocketAtuador = new TCPClientSocket("187.183.5.216", 9002);
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
            save(text + "\r\n\r\n" + erros, "tcpLGG5.txt");
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
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getTrafficlightColor(int n){
        int r = (255 * n) / 100;
        int g = (255 * (100 - n)) / 100;
        int b = 0;
        return Color.rgb(r, g, b);
    }


}
