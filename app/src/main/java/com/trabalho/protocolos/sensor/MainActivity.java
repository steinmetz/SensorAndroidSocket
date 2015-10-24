package com.trabalho.protocolos.sensor;

import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {

    private TextView txtValue;
    private TextView txtStatus;

    Button btnStart;
    Spinner spnTCPUPD;
    EditText edtPorta;


    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtValue = (TextView) findViewById(R.id.txtValue);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        btnStart = (Button) findViewById(R.id.btnStart);
        spnTCPUPD = (Spinner) findViewById(R.id.spnTCPUPD);
        edtPorta = (EditText) findViewById(R.id.edtPorta);


        sensor = new Sensor(this,txtStatus, txtValue);
        sensor.startSensor();

        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        TextView txtIP = (TextView) findViewById(R.id.txtIP);
        txtIP.setText(ip);

    }

    public void start(View v){
        if (v.getTag().equals("start")){
            try {
                final int porta = Integer.parseInt(edtPorta.getText().toString());
                final String type = spnTCPUPD.getSelectedItem().toString();
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        sensor.startServer(porta, type);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtStatus.setText("Servidor fechou.");
                                setEnabledViews(true);
                                btnStart.setEnabled(true);
                            }
                        });
                    }
                };
                Thread t = new Thread(run);
                txtStatus.setText("Servidor rodando...");
                t.start();
                setEnabledViews(false);
            }catch (Exception e){
                txtStatus.setText("Porta inválida");
                return;
            }
        }else{
            sensor.stopServer();
            txtStatus.setText("Servidor ainda está rodando até a próxima conexão.");
            btnStart.setEnabled(false);
        }

    }

    public void setEnabledViews(boolean isEnabled){
        spnTCPUPD.setEnabled(isEnabled);
        edtPorta.setEnabled(isEnabled);
        if (isEnabled){
            btnStart.setText("Start");
            btnStart.setTag("start");
        }else{
            btnStart.setText("Stop");
            btnStart.setTag("stop");
        }
    }


}
