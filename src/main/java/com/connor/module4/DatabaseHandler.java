package com.connor.module4;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;



public class DatabaseHandler {
    private static final String DB_url = "jdbc:derby:database/forum;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;

    public DatabaseHandler() {
        createConnection();
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
                        "id varchar(200) PRIMARY KEY " +
                        ")";
                System.out.println(statement);
                stmt.execute(statement);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * This method imports table data into a database.
     *
     * @param TABLE_NAME The name of the table to be imported.
     * @param headers An ArrayList of the headers for the table.
     * @param data An ArrayList of the data for the table.
     * @return A String indicating whether the import was successful or not.
     * @throws SQLException If an error occurs while importing the data.
     */
    public String importTableData(String TABLE_NAME, ArrayList<String> headers, ArrayList<String> data) throws SQLException {
        try {
            createTable(TABLE_NAME);
            for (String s : headers) {
                createTableColumn(TABLE_NAME, s);
            }
            for(String s : data){
                addData(TABLE_NAME, s);
            }
            return "Success";
        } catch (SQLException e){
            e.printStackTrace();
            return "Exception caught.";
        }

    }

    /**
     * exports a table from a database to a CSV file.
     *
     * @param tableName The name of the table to be exported.
     * @param fileName The name of the CSV file to be created.
     */
    public void exportTableToCSV(String tableName, String fileName) {
        try {
            stmt = conn.createStatement();
            String query = "SELECT * FROM " + tableName;
            ResultSet rs = stmt.executeQuery(query);
            FileWriter fw = new FileWriter(fileName);
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                fw.append(rs.getMetaData().getColumnName(i));
                if (i < rs.getMetaData().getColumnCount()) {
                    fw.append(",");
                }
            }
            fw.append("\n");
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

    /**
     * Creates a connection to the database using the org.apache.derby.jdbc.ClientDriver.
     */
    private void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(DB_url);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Creates a new column in the specified table.
     *
     * @param TABLE_NAME the name of the table to add the column to
     * @param COLUMN_NAME the name of the column to add
     * @throws SQLException if an error occurs while creating the column

     */
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

    /**
     * Adds data to the specified table.
     *
     * @param TABLE_NAME the name of the table to add the data to
     * @param data the data to add to the table
     * @throws SQLException if an error occurs while adding the data
     */
    private void addData(String TABLE_NAME, String data) throws SQLException {
            String statement = "INSERT INTO " + TABLE_NAME + " VALUES " + "(" + data + ")";
            System.out.println(statement);
            stmt.execute(statement);
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
