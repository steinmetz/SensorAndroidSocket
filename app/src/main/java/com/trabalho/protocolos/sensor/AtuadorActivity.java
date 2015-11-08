package com.trabalho.protocolos.sensor;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AtuadorActivity extends AppCompatActivity {

    TextView txtAtuacao;
    private TextView txtStatus;

    Button btnStart;
    Spinner spnTCPUPD;
    EditText edtPorta;

    Atuador atuador;

    protected PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atuador);

        txtAtuacao = (TextView) findViewById(R.id.txtAtuacao);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        btnStart = (Button) findViewById(R.id.btnStart);
        spnTCPUPD = (Spinner) findViewById(R.id.spnTCPUPD);
        edtPorta = (EditText) findViewById(R.id.edtPorta);


        atuador = new Atuador(this, txtStatus, txtAtuacao);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtAtuacao.setBackgroundColor(atuador.getColor());
                        }
                    });
                    SystemClock.sleep(500);
                }
            }
        }).start();


        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        TextView txtIP = (TextView) findViewById(R.id.txtIP);
        txtIP.setText(ip);


        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();
    }

    @Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }

    public void start(View v) {
        if (v.getTag().equals("start")) {
            try {
                final int porta = Integer.parseInt(edtPorta.getText().toString());
                final String type = spnTCPUPD.getSelectedItem().toString();


                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        atuador.startServer(porta, type);
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
                t.start();
                txtStatus.setText("Servidor rodando...");
                setEnabledViews(false);
            } catch (Exception e) {
                txtStatus.setText("Porta inválida");
                return;
            }
        } else {
            atuador.stopServer();
            txtStatus.setText("Servidor ainda está rodando até a próxima conexão.");
            btnStart.setEnabled(false);
        }
    }

    public void setEnabledViews(boolean isEnabled) {
        spnTCPUPD.setEnabled(isEnabled);
        edtPorta.setEnabled(isEnabled);
        if (isEnabled) {
            btnStart.setText("Start");
            btnStart.setTag("start");
        } else {
            btnStart.setText("Stop");
            btnStart.setTag("stop");
        }
    }
}
