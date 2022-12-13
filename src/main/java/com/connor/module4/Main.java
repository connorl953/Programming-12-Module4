package com.connor.module4;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        String TABLE_NAME = "BASEBALLSTATS";
        DatabaseHandler databaseHandler = new DatabaseHandler();
        File file = new File("OriginalPlayerData.csv");
        ArrayList<String> headers = CSVHandler.parseCSVHeaders(file);
        ArrayList<String> data = CSVHandler.parseCSVContents(file);
        try {
            databaseHandler.createTable(TABLE_NAME);
            System.out.println(databaseHandler.importTableData(TABLE_NAME, headers, data));
            databaseHandler.exportTableToCSV(TABLE_NAME, "out.csv");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
