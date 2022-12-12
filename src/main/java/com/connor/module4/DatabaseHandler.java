package com.connor.module4;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class DatabaseHandler {
    private static final String DB_url = "jdbc:derby:database/forum;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;
    public static DatabaseHandler handler;

    public DatabaseHandler() {
        createConnection();
    }

    public static DatabaseHandler getHandler(){
        if(handler == null){
            handler = new DatabaseHandler();
            return handler;
        }else{
            return handler;
        }
    }

    /**
     *  Attempts to create a table the database with the specified parameters. Data stored in the headers must be strings in this case.
     * @param TABLE_NAME Name of the table
     */
    public void createTable(String TABLE_NAME) {

        TABLE_NAME = TABLE_NAME.toUpperCase();
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dmn = conn.getMetaData();
            ResultSet tables = dmn.getTables(null, null, TABLE_NAME, null);
            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " exists");
            } else {
                String statement = "CREATE TABLE " + TABLE_NAME + "(\n" +
                        "id varchar(200)" +
                        ")";
                System.out.println(statement);
                stmt.execute(statement);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void exportTableToCSV(String tableName, String fileName) {
        try {
            stmt = conn.createStatement();
            String query = "SELECT * FROM " + tableName;
            ResultSet rs = stmt.executeQuery(query);
            FileWriter fw = new FileWriter(fileName);
            while (rs.next()) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    fw.append(rs.getString(i));
                    if (i < rs.getMetaData().getColumnCount()) {
                        fw.append(",");
                    }
                }
                fw.append("\n");
            }
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(DB_url);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private void createTableColumn(String TABLE_NAME, String COLUMN_NAME) throws SQLException {
        COLUMN_NAME = COLUMN_NAME.toUpperCase();
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getColumns(null, null, TABLE_NAME, COLUMN_NAME);
        if (rs.next()) {
            System.out.println("Column " + COLUMN_NAME + " in table " + TABLE_NAME + " exists");

        } else {
            String statement = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_NAME + " varchar(200) ";
            System.out.println(statement);
            stmt.execute(statement);
            System.out.println("Created column " + COLUMN_NAME + " in table " + TABLE_NAME + ".");
        }

    }

    private void addData(String TABLE_NAME, String data) throws SQLException {
            String statement = "INSERT INTO " + TABLE_NAME + " VALUES " + "(" + data + ")";
            System.out.println(statement);
            stmt.execute(statement);

    }

    public String importTableData(String TABLE_NAME, HashMap<String, ArrayList<String>> dataMap) throws SQLException {
        try {
            createTable(TABLE_NAME);
            stmt = conn.createStatement();
            DatabaseMetaData dmn = conn.getMetaData();
            for (String s : dataMap.keySet()) {
                createTableColumn(TABLE_NAME, s);
            }
            for(String s : buildData(dataMap)){
                addData(TABLE_NAME, s);
            }
            return "Success";
        } catch (SQLException e){
            e.printStackTrace();
            return "Exception caught.";
        }

    }


    public static String setToString(Set<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }
    public ArrayList<String> buildData(HashMap<String, ArrayList<String>> dataMap) {
       int counter = 0;
        ArrayList<String> toReturn = new ArrayList<>();
        for(String s : dataMap.keySet()){
           counter = Math.max(dataMap.get(s).size(), counter);
       }
        while(counter > 0){
            counter--;
            StringBuilder tempString = new StringBuilder();
            tempString.append("'").append(counter).append("',");
            System.out.println(dataMap.keySet());
            for(String s : dataMap.keySet()){

                tempString.append("'").append(dataMap.get(s).get(counter)).append("',");
            }
            toReturn.add(tempString.deleteCharAt(tempString.length() - 1).toString());

        }
        return toReturn;
    }
    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;

        } catch (SQLException throwables) {
            System.out.println(throwables);
            System.out.println("Did not enter data");
        }
        return false;
    }
    public ResultSet execQuery(String query){
        ResultSet resultSet;
        try{
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return resultSet;
    }
}
