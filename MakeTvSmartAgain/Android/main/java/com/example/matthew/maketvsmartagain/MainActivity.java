package com.example.matthew.maketvsmartagain;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Debug;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matthew.maketvsmartagain.classes.KalmanFilter;

public class MainActivity extends AppCompatActivity {

    private KalmanFilter kalmanFilterX;
    private KalmanFilter kalmanFilterY;
    private float xValue;
    private float yValue;
    private float xValue2;
    private float yValue2;

    private Button button;

    private TextView xText;
    private TextView zText;
    private TextView xPlot;
    private TextView yPlot;

    private FrameLayout frameLayout;
    private ImageView horizontalLine;
    private ImageView verticalLine;

    private int xPlotCompensation;
    private int yPlotCompensation;
    private int plotHeight = -1;
    private int plotWidth = -1;

    private boolean isCalibrated = true;
    private int calibrationValuesCount = 0;
    private float[] calibrationValues = new float[20];
    private float driftCompensation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kalmanFilterX = new KalmanFilter();
        kalmanFilterY = new KalmanFilter();

        xText = (TextView) findViewById(R.id.textView);
        zText = (TextView) findViewById(R.id.textView2);
        xPlot = (TextView) findViewById(R.id.xValueText);
        yPlot = (TextView) findViewById(R.id.yValueText);

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        horizontalLine = (ImageView) findViewById(R.id.imageView);
        verticalLine = (ImageView) findViewById(R.id.imageView2);



        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // calibrate n' shit
                kalmanFilterX.initialize(0, 0.01f, 0.002f);
                kalmanFilterY.initialize(0, 0.01f, 0.002f);
                xValue2 = 0;
                yValue2 = 0;

                isCalibrated = false;

                resetPlot();
                frameLayout.setEnabled(true);
            }
        });


        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                float z = sensorEvent.values[2];

//                xText.setText("X: " + x);
//                zText.setText("Z: " + z);

                xValue += x;
                yValue += z;

                lowPassFilter(x, 0.01f);

//                kalmanFilterX.calculate(x);
//                kalmanFilterY.calculate(z);
//                xValue2 += kalmanFilterX.getEstimate();
//                yValue2 += kalmanFilterY.getEstimate();
//                Log.d("AYY", String.valueOf(x));
                xText.setText("X: " + lowPassFilter(x, 0.01f));
                zText.setText("Z: " + lowPassFilter(z, 0.01f));


//                xText.setTextColor(Color.LTGRAY);
//                zText.setTextColor(Color.LTGRAY);
//                x = Math.abs(x);
//                z = Math.abs(z);
//                if (x > z) {
//                    xText.setTextColor(Color.GREEN);
//                } else {
//                    zText.setTextColor(Color.GREEN);
//                }

                xPlot.setText(String.valueOf(xValue));
                yPlot.setText(String.valueOf(yValue));;
                setPlotXY((int)xValue, (int)yValue);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                // Not in use
            }
        }, mySensor, SensorManager.SENSOR_DELAY_UI);
        frameLayout.setEnabled(false);
    }

    private void setPlotXY(int x, int y) {

        ViewGroup.MarginLayoutParams hllp = (ViewGroup.MarginLayoutParams) horizontalLine.getLayoutParams();
        ViewGroup.MarginLayoutParams vllp = (ViewGroup.MarginLayoutParams) verticalLine.getLayoutParams();
        hllp.bottomMargin = yPlotCompensation + y;
        vllp.leftMargin = xPlotCompensation + x;
        horizontalLine.setLayoutParams(hllp);
        verticalLine.setLayoutParams(vllp);
    }

    private void initializePlot() {

        xPlotCompensation = plotWidth / 2;
        yPlotCompensation = plotHeight / 2;
    }

    private void initializePlot(int x, int y) {

        xPlotCompensation = x;
        yPlotCompensation = y;
    }

    private void resetPlot() {

        plotHeight = frameLayout.getHeight();
        plotWidth = frameLayout.getWidth();
        xValue = 0;
        yValue = 0;
        initializePlot();
        setPlotXY(0, 0);
    }

    private float lowPassFilter(float value, float minValue) {
        if (value < minValue)
            return 0;
        return value;
    }

}
