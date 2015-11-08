package com.trabalho.protocolos.sensor;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.trabalho.protocolos.sensor.agents.AgentA;
import com.trabalho.protocolos.sensor.agents.Gateway;

import jade.android.MicroRuntimeService;
import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;
import jade.core.Profile;
import jade.imtp.leap.ConnectionListener;
import jade.util.leap.Properties;

public class MenuInicialActivity extends AppCompatActivity implements ConnectionListener {
    protected PowerManager.WakeLock mWakeLock;


    private MicroRuntimeServiceBinder microRuntimeServiceBinder;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);

        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i("SSS", "ASDF");
                microRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
                agoraVai();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i("SSS", "ASDF");
                microRuntimeServiceBinder = null;
            }
        };

        bindService(new Intent(getApplicationContext(), MicroRuntimeService.class), serviceConnection, Context.BIND_AUTO_CREATE);




    }

    public void agoraVai(){

        Properties pp = new Properties();
        pp.setProperty(Profile.MAIN_HOST, "localhost");
        pp.setProperty(Profile.MAIN_PORT, String.valueOf(9000));
        pp.setProperty(Profile.JVM, Profile.ANDROID);

        microRuntimeServiceBinder.startAgentContainer(pp, new RuntimeCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("SSS", "ASDF");
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.i("SSS", "ASDF");
            }
        });

        microRuntimeServiceBinder.startAgent("foda", "AgentA", new Object[]{getApplicationContext()},
                new RuntimeCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("SSS", "ASDF");
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("SSS", "ASDF");
                    }
                }
        );
    }

    @Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }

    public void sensor(View v) {
        Intent i = new Intent(this, SensorActivity.class);
        startActivity(i);
    }

    public void atuador(View v) {
        Intent i = new Intent(this, AtuadorActivity.class);
        startActivity(i);
    }

    public void trab7(View v) {
        Intent i = new Intent(this, Trab7Activity.class);
        startActivity(i);
    }

    @Override
    public void handleConnectionEvent(int i, Object o) {

    }
}
