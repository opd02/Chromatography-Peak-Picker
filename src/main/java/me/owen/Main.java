package me.owen;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;
import com.github.freva.asciitable.HorizontalAlign;
import me.owen.objects.DataRow;
import me.owen.objects.GCFeature;
import me.owen.plotting.XYPlot;

import javax.swing.*;
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
    public static double RT_IDENTIFY_ERROR = 0.03;
    public static String executionPath;
    public static File libraryFile;
    public static boolean isCustomThreshold = false;
    public static boolean isCustomPath = false;
    public static HashMap<List<DataRow>, String> datasets = new HashMap<>();
    public static boolean graphical = false;

    public static void main(String[] args) throws IOException {
        System.out.println("Running GC-Peak-Picker...");

        for(int i=0; i<args.length; i+=2){
            String key = args[i];
            String value = args[i+1];

            switch (key) {
                case "-t":
                    Main.INTENSITY_THRESHOLD = Double.parseDouble(value);
                    Main.isCustomThreshold = true;
                    System.out.println("Intensity threshold manually set to " + Main.INTENSITY_THRESHOLD);
                    break;
                case "-e":
                    Main.RT_IDENTIFY_ERROR = Double.parseDouble(value);
                    System.out.println("Allowed retention time identification error set to " + value);
                    break;
                case "-g":
                    System.out.println("Graphical output has been toggled on");
                    graphical = true;
                    break;
                case "-p":
                    Main.isCustomPath = true;
                   // if(value.charAt(0)=='\"'){
                        Main.executionPath = value;
                   // }else{
                       // Main.executionPath = "\"" + value + "\"";
                    //}
                    System.out.println("Custom execution path set to " + value);
                    break;
                case "-l" :
                    Main.libraryFile = new File(value);
                    if(Main.libraryFile.exists()){
                        System.out.println("Library file set to " + Main.libraryFile.getAbsolutePath());
                        RTLibraryManager.loadLibrary();
                        break;
                    }else{
                        System.out.println("Specified library file not found: " + Main.libraryFile.getAbsolutePath());
                    }
            }
        }
        if(Main.executionPath == null){
            Main.executionPath = System.getProperty("user.dir");
        }
        ArrayList<File> filesInFolder = new ArrayList<>();

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(Main.executionPath))){

            for(Path path : stream){
                if(Main.libraryFile != null && Main.libraryFile.getAbsolutePath().equals(path.toString())){
                    continue;
                }
                if(!Files.isDirectory(path) && path.toString().toUpperCase().contains(".CSV")){

                    filesInFolder.add(path.toFile());
                    System.out.println("Opening File: "+path);
                }
            }
        }catch(Exception ignored){
            if(Main.isCustomPath){
                System.out.println("Error reading files in custom path. Make sure to put the target execution path in quotes");
            }else{
                System.out.println("Error searching running directory for .csv files. Make sure they are in the same directory as the jar.");
            }
            return;
        }

        for(File file : filesInFolder) {
            System.out.println("Reading Data from " + file.getName() + " (this make take a moment)...");
            List<DataRow> workingData = JacksonCSVReader.readCSV(file);
            datasets.put(workingData, file.getName());

            System.out.println(workingData.size() + " rows read");
            ArrayList<GCFeature> features = PeakFinder.findPeaks(workingData);
            System.out.println(features.size() + " features found");

            System.out.println(AsciiTable.getTable(features, Arrays.asList(
                    new Column().header("Feature ID").headerAlign(HorizontalAlign.CENTER).dataAlign(HorizontalAlign.LEFT).with(feature -> java.lang.String.valueOf(feature.getFeatureID())),
                    new Column().header("Estimated Identity").with(feature -> java.lang.String.format("%s", feature.getIdentity())),
                    new Column().header("Peak Center").with(feature -> java.lang.String.format("%.03f", feature.getCenterTime())),
                    new Column().header("Peak Area").with(feature -> java.lang.String.format("%.02f", feature.getPeakArea())))));
        }
        if(graphical) {
            SwingUtilities.invokeLater(() -> {
                XYPlot example = new XYPlot(datasets);
                example.setSize(800, 600);
                example.setLocationRelativeTo(null);
                example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                example.setVisible(true);
            });
        }
    }
}