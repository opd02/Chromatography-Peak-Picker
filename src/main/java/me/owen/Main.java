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
import java.util.List;

public class Main {

    public static double INTENSITY_THRESHOLD = 50;
    public static double RT_IDENTIFY_ERROR = 0.03;
    public static File libraryFile;
    public static boolean isCustomThreshold = false;

    public static void main(String[] args) throws IOException {
        System.out.println("Running GC-Peak-Picker...");

        for(int i=0; i<args.length; i+=2){
            String key = args[i];
            String value = args[i+1];

            switch (key) {
                case "-t" :
                    Main.INTENSITY_THRESHOLD = Double.parseDouble(value);
                    Main.isCustomThreshold = true;
                    System.out.println("Intensity threshold manually set to " + Main.INTENSITY_THRESHOLD);
                    break;
                case "-e":
                    Main.RT_IDENTIFY_ERROR = Double.parseDouble(value);
                    System.out.println("Allowed retention time identification error set to " + value);
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

        String runningDir = System.getProperty("user.dir");
        ArrayList<File> filesInFolder = new ArrayList<>();

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(runningDir))){

            for(Path path : stream){
                if(Main.libraryFile != null && Main.libraryFile.getAbsolutePath().equals(path.toString())){
                    continue;
                }
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
}