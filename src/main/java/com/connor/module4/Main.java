package com.connor.module4;

import java.io.File;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {


        String TABLE_NAME = "BASEBALLSTATS";
        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.createTable(TABLE_NAME);
        File file = new File("PlayerData.csv");
        try {
            System.out.println(databaseHandler.importTableData(TABLE_NAME, CSVHandler.parseCSV(file)));
            databaseHandler.exportTableToCSV(TABLE_NAME, "out.csv");
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        CSVHandler.parseCSV(file);

    }
}
