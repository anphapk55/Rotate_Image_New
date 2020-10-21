package com.pka.rotateimage

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.lang.Math.sqrt
import java.util.*


class MainActivity : AppCompatActivity() {

    private var mSensorManager : SensorManager ?= null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    private val SHAKE_SLOP_TIME_MS = 1000
//    private val SHAKE_COUNT_RESET_TIME_MS = 3000


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(mSensorManager)!!.registerListener(
            sensorListener, mSensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH


        val btRotateLeft = findViewById<Button>(R.id.bt_rotate_left)
        btRotateLeft.setOnClickListener {
            val imageView = findViewById<ImageView>(R.id.img)
            imageView.rotation = imageView.rotation + 90 }

        val btRotateRight = findViewById<Button>(R.id.bt_rotate_right)
        btRotateRight.setOnClickListener {
            val imageView = findViewById<ImageView>(R.id.img)
            imageView.rotation = imageView.rotation - 90 }
    }
    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > 12) {

                var now : Long = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                var mShakeTimestamp: Long? = null
                if (mShakeTimestamp != null) {
                    if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                        return;
                    }
                }
                mShakeTimestamp = now

                if (Round(x,4)< -30.0000){
                    val imageView = findViewById<ImageView>(R.id.img)
                    imageView.rotation = imageView.rotation - 90
                    Toast.makeText(applicationContext, "Shake it Right", Toast.LENGTH_SHORT).show()
                }else if (Round(x,4)>30.0000) {
                    val imageView = findViewById<ImageView>(R.id.img)
                    imageView.rotation = imageView.rotation + 90
                    Toast.makeText(applicationContext, "Shake it Left", Toast.LENGTH_SHORT).show()
                }
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }
    override fun onResume() {
        super.onResume()
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager?.registerListener(
            sensorListener, mSensorManager!!.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER
            ), SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager?.unregisterListener(sensorListener)
        super.onPause()
    }
    fun Round(Rval: Float, Rpl: Int): Float {
        var Rval = Rval
        val p = Math.pow(10.0, Rpl.toDouble()).toFloat()
        Rval = Rval * p
        val tmp = Math.round(Rval).toFloat()
        return tmp / p
    }

}


