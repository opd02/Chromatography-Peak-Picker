package me.owen;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import me.owen.objects.DataRow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JacksonCSVReader {
    public static List<DataRow> readCSV(File file) throws IOException {
        List<DataRow> returnedPuzzle = new ArrayList<>();

        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.builder()
                .addColumn("time")  // Map the first column
                .addColumn("intensity") // Map the second column
                .setColumnSeparator('\t') // Tab delimiter
                .build();

        try (MappingIterator<DataRow> iterator = csvMapper.readerFor(DataRow.class)
                .with(schema)
                .readValues(file)) {

            while (iterator.hasNext()) {
                DataRow row = iterator.next();
                returnedPuzzle.add(row);
            }
        }
        return returnedPuzzle;
    }
}
