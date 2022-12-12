package com.connor.module4;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class CSVHandler {


    public static HashMap<String, ArrayList<String>> parseCSV(File csvFile){
        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        CSVParser csvParser;
        FileReader fileReader;
        try {
            fileReader = new FileReader(csvFile);
            csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (CSVRecord csvRecord : csvParser) {
            String key = csvRecord.get(0);
            ArrayList<String> values = new ArrayList<>();
            for (int i = 1; i < csvRecord.size(); i++) {
                values.add(csvRecord.get(i));
            }
            map.put(key, values);
        }

        return map;
    }
    }

