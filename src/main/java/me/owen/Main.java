package me.owen;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.HorizontalAlign;
import me.owen.objects.DataRow;
import me.owen.objects.GCFeature;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static double INTENSITY_THRESHOLD = 50;
    public static HashMap<Double, String> knownRetentionTimes =  new HashMap<>();

    public static void main(String[] args) throws IOException {
        if(args.length != 0 && args[0].equals("-t")) {
            Main.INTENSITY_THRESHOLD = Double.parseDouble(args[1]);
            System.out.println("Intensity Threshold set to " + Main.INTENSITY_THRESHOLD);
        }
        fillInKnownTable();

        System.out.println("Running GC-Peak-Picker...");
        String runningDir = System.getProperty("user.dir");
        ArrayList<File> filesInFolder = new ArrayList<File>();

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(runningDir))){

            for(Path path : stream){
                if(!Files.isDirectory(path) && path.toString().contains(".CSV")){
                    filesInFolder.add(path.toFile());
                    System.out.println("Opening File: "+path);
                }
            }
        }catch(Exception ignored){

        }

        for(File file : filesInFolder) {
            System.out.println("Reading Data from " + file.getName() + " (this make take a moment)...");
            List<DataRow> workingData = JacksonCSVReader.readCSV(file);
            System.out.println(workingData.size() + " rows read");
            ArrayList<GCFeature> features = PeakFinder.findPeaks(workingData);
            System.out.println(features.size() + " features found");

            System.out.println(AsciiTable.getTable(features, Arrays.asList(
                    new Column().header("Feature ID").headerAlign(HorizontalAlign.CENTER).dataAlign(HorizontalAlign.LEFT).with(feature -> String.valueOf(feature.getFeatureID())),
                    new Column().header("Estimated Identity").with(feature -> String.format("%s", feature.getIdentity())),
                    new Column().header("Peak Center").with(feature -> String.format("%.03f", feature.getCenterTime())),
                    new Column().header("Peak Area").with(feature -> String.format("%.02f", feature.getPeakArea())))));
        }
    }

    private static void fillInKnownTable(){
        Main.knownRetentionTimes.put(1.729, "CYCLOHEXENE");
        Main.knownRetentionTimes.put(3.55, "DECANE");
        Main.knownRetentionTimes.put(4.941, "STERIC PRODUCT");
        Main.knownRetentionTimes.put(5.034, "DIRECTED PRODUCT");
        Main.knownRetentionTimes.put(5.25, "TERPINEN-4-ol");
    }
}