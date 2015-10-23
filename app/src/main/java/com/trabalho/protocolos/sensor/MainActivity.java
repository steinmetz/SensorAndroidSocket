package com.trabalho.protocolos.sensor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.trabalho.protocolos.sensor.socket.TCPServerSocket;

public class MainActivity extends AppCompatActivity  {

    private TextView tv;
    private TextView txtStatus;

    Button btnStart;
    Spinner spnTCPUPD;
    EditText edtPorta;


    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        btnStart = (Button) findViewById(R.id.btnStart);
        spnTCPUPD = (Spinner) findViewById(R.id.spnTCPUPD);
        edtPorta = (EditText) findViewById(R.id.edtPorta);


        sensor = new Sensor(this);


    }

    public void start(View v){
        int porta = 9000;
        try {
            porta = Integer.parseInt(edtPorta.getText().toString());
        }catch (Exception e){
            txtStatus.setText("Porta inválida");
            return;
        }
        if(spnTCPUPD.getSelectedItem().toString().equals("TCP")){
            TCPServerSocket socket = new TCPServerSocket(porta);
            //socket.start(sensor);
        }else {

        }
    }

}
