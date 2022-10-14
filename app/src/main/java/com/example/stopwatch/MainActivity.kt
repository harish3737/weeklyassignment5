package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {

    // variables
    lateinit var stopwatch: Chronometer //the stopwatch
    var running =  false // is stopwatch running?
    var offset: Long = 0 //the base offset for the stopwatch
    var stepCount: Int = 0 //the base offset for the stopwatch

    //key strings f0r use with the bundle
    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stopwatch = findViewById<Chronometer>(R.id.stopwatch)

        // Restore the previous state
        if(savedInstanceState !== null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if(running) {
                stopwatch.base =  savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            } else  {
                setBaseTime()
            }
        }

        val startButton = findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener{
            if(!running && stepCount > 0) {
                setBaseTime()
                stopwatch.start()
                running = true;
            }
        }

        val pauseButtton = findViewById<Button>(R.id.pause_button)
        pauseButtton.setOnClickListener{
            if(running) {
                saveOffset();
                stopwatch.stop();
                running = false;
            }
        }

        // reset button sets the offset and stopwatch to 0
        val resetButton = findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            offset = 0
            stepCount= 0
            setBaseTime()
            if(running){
                stopwatch.stop();
                running = false;
            }
        }


        // increase countdown by 1 second
        val stepUp = findViewById<Button>(R.id.step_up_button)
        stepUp.setOnClickListener {
            if(!running) {
                stepCount += 5;
                setCountDown()
            }
        }

        // decreases countdown by 1 second
        val stepDown = findViewById<Button>(R.id.step_down_button)
        stepDown.setOnClickListener {
            if(!running && stepCount > 0){
                stepCount -= 5
                setCountDown()
            }
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.getLong(OFFSET_KEY,offset)
        savedInstanceState.getBoolean(RUNNING_KEY,running)
        savedInstanceState.getLong(BASE_KEY,stopwatch.base)
        super.onSaveInstanceState(savedInstanceState)
    }

    override  fun onPause() {
        super.onPause()
        if(running){
            saveOffset()
            stopwatch.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        if(running){
            offset = 0
            setBaseTime()
            stopwatch.start()
        }
    }

    private fun setCountDown(){
        stopwatch.base = SystemClock.elapsedRealtime() + stepCount * 1000;
        saveOffset()
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }

    private fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() -  offset
    }
}