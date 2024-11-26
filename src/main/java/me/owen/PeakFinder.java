package me.owen;

import me.owen.objects.DataRow;
import me.owen.objects.GCFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PeakFinder {

    public static ArrayList<GCFeature> findPeaks(List<DataRow> list){
        ArrayList<GCFeature> peaks = new ArrayList<>();

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
