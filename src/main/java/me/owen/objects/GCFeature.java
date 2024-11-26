package me.owen.objects;

import me.owen.Main;

import java.util.ArrayList;
import java.util.HashMap;

public class GCFeature {
    ArrayList<DataRow> data;
    double startTime;
    double endTime;
    int featureID;

    public GCFeature(){
        data = new ArrayList<>();
    }

    public void addData(DataRow row){
        data.add(row);
    }

    public double getStartTime() {
        return data.getFirst().getTime();
    }

    public double getEndTime() {
        return data.getLast().getTime();
    }

    public double getPeakArea(){
        double area = 0.0;

        for (int i = 0; i < data.size() - 1; i++) {
            double x1 = data.get(i).getTime();
            double y1 = data.get(i).getIntensity();
            double x2 = data.get(i + 1).getTime();
            double y2 = data.get(i + 1).getIntensity();

            area += (x2 - x1) * (y1 + y2) / 2.0;
        }
        return area;
    }

    public double getCenterTime(){
        double weightedSum = 0.0; // Sum of x_i * y_i
        double intensitySum = 0.0; // Sum of y_i

        for (DataRow point : data) {
            double x = point.getTime();
            double y = point.getIntensity();

            weightedSum += x * y;
            intensitySum += y;
        }

        if (intensitySum == 0) {
            throw new IllegalArgumentException("Intensity sum is zero, cannot determine peak center.");
        }

        return weightedSum / intensitySum;
    }

    public boolean isEmpty(){
        return data.isEmpty();
    }

    public String getIdentity(){
        for(double d : Main.knownRetentionTimes.keySet()){
            if(Math.abs(this.getCenterTime() - d)/d < 0.01){
                return Main.knownRetentionTimes.get(d);
            }
        }
        return "UNKNOWN";
    }

    public int getFeatureID() {
        return featureID;
    }

    public void setFeatureID(int featureID) {
        this.featureID = featureID;
    }
}
