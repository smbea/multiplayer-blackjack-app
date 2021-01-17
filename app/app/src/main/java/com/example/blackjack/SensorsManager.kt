package com.example.blackjack

import android.app.Service
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.blackjack.models.Game

class SensorsManager(context: Context) : SensorEventListener {

    private var sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
    private val valuesGyroscope = FloatArray(3)
    private val valuesMagnometer = FloatArray(3)
    private var rightEvent = false
    private var leftEvent = false

    init {
        Log.v("aqui", "created")
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.also { gyroscope ->
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_FASTEST, SensorManager.SENSOR_DELAY_GAME)
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magnometer ->
            sensorManager.registerListener(this, magnometer, SensorManager.SENSOR_DELAY_FASTEST, SensorManager.SENSOR_DELAY_GAME)
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {

        when (event?.sensor?.type) {
            Sensor.TYPE_GYROSCOPE -> {
                valuesGyroscope[0] = event.values[0]
                valuesGyroscope[1] = event.values[1]
                valuesGyroscope[2] = event.values[2]

                if (!rightEvent && !leftEvent) {
                    if (valuesGyroscope[1] < 0) {
                        Log.v("r", "tilted left")
                        Game.currentGameController.stand()
                        leftEvent = true
                    } else if (valuesGyroscope[1] > 0) {
                        Log.v("r", "tilted right")
                        Game.currentGameController.hit()
                        rightEvent = true
                    }
                }

            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                valuesMagnometer[0] = event.values[0]
                valuesMagnometer[1] = event.values[1]
                valuesMagnometer[2] = event.values[2]

                if (leftEvent) {
                    if (valuesMagnometer[0] >= 0) {
                        Log.v("r", valuesMagnometer[0].toString())
                        leftEvent = false
                    }
                } else if (rightEvent) {
                    if (valuesMagnometer[0] <= 0) {
                        Log.v("r", valuesMagnometer[0].toString())
                        rightEvent = false
                    }
                }

            }
        }

    }

    fun getSensorManager(): SensorManager {
        return sensorManager
    }


}