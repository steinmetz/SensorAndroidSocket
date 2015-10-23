package com.trabalho.protocolos.sensor;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by charles on 23/10/15.
 */
public class Sensor implements SensorEventListener {

    private float value = 0;
    private SensorManager sManager;

    public Sensor(Context ctx){
        sManager = (SensorManager) ctx.getSystemService(ctx.SENSOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Orientation X (Roll)
        value = event.values[2];
    }

    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {

    }

    protected void start() {
        sManager.registerListener(this, sManager.getDefaultSensor(android.hardware.Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void stop() {
        sManager.unregisterListener(this);
    }

    public float getValue(){
        return value;
    }


}
