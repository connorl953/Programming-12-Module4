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


    /**
     * This method takes in a File object containg a CSV file and parses the headers of the CSV file. It returns an ArrayList of Strings containing the headers.
     *
     * @param csvFile The File object to be parsed
     * @return An ArrayList of Strings containing the headers of the CSV file
     */
    public static ArrayList<String> parseCSVHeaders(File csvFile){
        ArrayList<String> list = new ArrayList<>();
        CSVParser csvParser;
        FileReader fileReader;
        try {
            fileReader = new FileReader(csvFile);
            csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (CSVRecord csvRecord : csvParser) {
            if(csvRecord.getRecordNumber() == 1){
                csvRecord.stream().forEach(list::add);
            }
        }

        return list;
    }

    /**
     * This method takes in a File object of a CSV file and parses the contents of the CSV file. It returns an ArrayList of Strings containing the contents in single quotes, separated by commas.
     *
     * @param csvFile The File object to be parsed
     * @return An ArrayList of Strings containing the contents of the CSV file
     */
    public static ArrayList<String> parseCSVContents(File csvFile){
        ArrayList<String> list = new ArrayList<>();
        CSVParser csvParser;
        FileReader fileReader;
        try {
            fileReader = new FileReader(csvFile);
            csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
            for (CSVRecord csvRecord : csvParser) {
                if(csvRecord.getRecordNumber() != 1){
                    StringBuilder temp = new StringBuilder();
                    temp.append("'").append(csvRecord.getRecordNumber()-1).append("',");
                    for(String s : csvRecord){
                        temp.append("'").append(s).append("',");
                    }
                    list.add(temp.substring(0, temp.length()-1));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    }

