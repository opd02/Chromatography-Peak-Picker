package me.owen;

import java.io.IOException;
import java.util.HashMap;

public class RTLibraryManager {
    public static HashMap<Double, String> knownRetentionTimes =  new HashMap<>();

    public static void loadLibrary() throws IOException {
        RTLibraryManager.knownRetentionTimes = JacksonCSVReader.readLibrary(Main.libraryFile);
    }
}
