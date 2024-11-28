package me.owen;

import me.owen.objects.DataRow;
import me.owen.objects.GCFeature;

import java.util.ArrayList;
import java.util.List;

public class PeakFinder {

    public static ArrayList<GCFeature> findPeaks(List<DataRow> list){
        ArrayList<GCFeature> peaks = new ArrayList<>();

        if(!Main.isCustomThreshold){
            double sum = 0;
            for(int i = 0; i < 100 ; i++){
                sum += list.get(i).getIntensity();
            }
            Main.INTENSITY_THRESHOLD = (sum/100) + 2;
            System.out.println("No custom intensity threshold detected, automatic threshold set to " + Main.INTENSITY_THRESHOLD);
        }

        int id = 1;
        GCFeature feature = new GCFeature();
        for(DataRow row : list){
            if(row.getIntensity() > Main.INTENSITY_THRESHOLD){
                feature.addData(row);
            }else{
                if(!feature.isEmpty()){
                    feature.setFeatureID(id++);
                    peaks.add(feature);
                    feature = new GCFeature();
                }
            }
        }
        return peaks;
    }
}
