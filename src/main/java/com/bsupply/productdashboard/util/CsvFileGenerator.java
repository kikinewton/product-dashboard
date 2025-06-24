package com.bsupply.productdashboard.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class CsvFileGenerator {

    CsvFileGenerator() {
    }

    public static ByteArrayInputStream toCSV(List<List<String>> data, String[] header) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT);

        csvPrinter.printRecord(Arrays.asList(header));
        data.forEach(
                d -> {
                    try {
                        csvPrinter.printRecord(d);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        csvPrinter.flush();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
