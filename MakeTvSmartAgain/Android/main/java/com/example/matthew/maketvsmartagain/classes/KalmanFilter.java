package com.example.matthew.maketvsmartagain.classes;

/**
 * Created by matthew on 17.12.27.
 */

public class KalmanFilter {

    private float kalmanGain;

    private float estimate;
    private float errorInEstimate;

    private float previousEstimate;
    private float previousErrorInEstimate;

    private float errorInMeasurement;


    public KalmanFilter() {
        kalmanGain = 1;
        estimate = 0;
        errorInEstimate = 0;
        previousEstimate = 0;
        previousErrorInEstimate = 0;
        errorInMeasurement = 0;
    }

    public float getEstimate() {
        return estimate;
    }

    public void initialize(float initialEstimate, float initialErrorInEstimate, float errorInMeasurement) {
        this.estimate = initialEstimate;
        this.errorInEstimate = initialErrorInEstimate;
        this.errorInMeasurement = errorInMeasurement;
    }

    public void calculate(float measurement) {

        previousEstimate = estimate;
        previousErrorInEstimate = errorInEstimate;

        // calculate Kalman gain
        float sumOfEstAndMeaErrors = previousErrorInEstimate + errorInMeasurement;
        kalmanGain = sumOfEstAndMeaErrors != 0 ? previousErrorInEstimate / (sumOfEstAndMeaErrors) : 0;

        // get an estimate of the optimal value provided with a measurement
        estimate = previousEstimate + kalmanGain * (measurement - previousEstimate);

        // calculate error in an estimate
        errorInEstimate = (1 - kalmanGain) * previousErrorInEstimate;
    }

    public void reset() {
        kalmanGain = 1;
        estimate = 0;
        errorInEstimate = 0;
        previousEstimate = 0;
        previousErrorInEstimate = 0;
        errorInMeasurement = 0;
    }
}
