package com.magnet.entries;

import java.io.Closeable;
import java.sql.*;

/**
 * Created by vladimir on 7/16/16.
 *
 * PostgresDBManager performs:
 * --- Writing the consecutive numbers into database,
 * --- Reading the numbers from database and returning them to the user.
 *
 * This class is NOT thread-safe.
 * This class is to be configured via dependency injection.
 * This class is intended to be singleton, but in order to achieve initialization
 * by builder, the problem of maintaining the singleton property was handed to DI framework.
 */
public final class PostgresDBManager implements Closeable {
    public PostgresDBManager() {
    }

    /**
     * Constructs the PostgresDBManager from a given builder.
     * @param builder state of the PostgresDBManager. See pattern Builder.
     */
    public void setBuilder(PostgresDBManagerBuilder builder) {
        if (builder == null)
            throw new IllegalStateException("Builder was not initialized");
        this.connectionString = builder.connectionString;
        this.userName = builder.userName;
        this.userPassword = builder.userPassword;
        this.batchSize = builder.batchSize;
        this.tableName = builder.tableName;
        this.fieldName = builder.fieldName;
        this.numberProducer = builder.numberProducer;
    }

    private String connectionString;
    private String userName;
    private String userPassword;
    private int    batchSize;
    private String tableName;
    private String fieldName;

    private Connection connection;

    private NumberProducer numberProducer;

    /**
     * Initializes connection to postgres database. Does nothing if connection already was initialized.
     * @throws SQLException when connection string does not suite for postgres DB and when exception was thrown
     * by the postgres JDBC driver.
     */
    public void connectToDB() throws SQLException {
        if (!connectionString.startsWith("jdbc:postgresql"))
            throw new SQLException("Invalid connection string. " +
                    "Expected connection string to PostgreSQL, found " + connectionString);
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(connectionString, userName, userPassword);
            // GREATLY reduces memory footprint, allowing to maintain a cursor while fetching data from
            // database instead of pulling all the data at once to a local copy.
            // See http://stackoverflow.com/questions/6942336 for more details.
            connection.setAutoCommit(false);
        }
    }

    /**
     * Writes the numbers into the database. If <b>append</b> is set to true, performs delete operation
     * before inserting. In this case performs <b>two</b> transactions: one for deleting and another one for inserting.
     * @param append indicates if the content of the table should be erased before writing.
     */
    public void writeNumbersToDB(boolean append) {
        if (!append)
            clearTable();
        insertValues();
    }

    /**
     * Removes values from injected table. Works in transaction.
     */
    private void clearTable() {
        final String clearSQL  = String.format("DELETE FROM %s", tableName);
        try(Statement clearStatement = connection.createStatement()) {
            int modifiedRows = clearStatement.executeUpdate(clearSQL);
            if (modifiedRows == 0)
                System.err.println("Delete operation on DBMS was unnecessary.");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollEx) {
                rollEx.printStackTrace();
            }
        }
    }

    /**
     * Inserts values from injected NumberProducer to the database. Works in transaction.
     */
    private void insertValues() {
        final String insertSQL = String.format("INSERT INTO %s VALUES (?)", tableName);
        try(PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {
            int counter = 0;
            while (numberProducer.hasNext()) {
                insertStatement.setInt(1, numberProducer.getNextInt());
                insertStatement.addBatch();

                if (++counter % batchSize == 0)
                    insertStatement.executeBatch();
            }
            insertStatement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollEx) {
                rollEx.printStackTrace();
            }
        }
    }

    /**
     * Writes the numbers into the database.
     */
    public void writeNumbersToDB() {
        writeNumbersToDB(false);
    }

    /**
     * Accesses a database and retrieves a numbers from the injected table. The NumberProducer which was returned
     * from function is responsible for freeing the resources. Returned NumberProducer becomes invalid
     * when PostgresDBManager is closed.
     * @return a NumberProducer object, providing access to the retrieved numbers.
     */
    public NumberProducer readNumbersFromDB() {
        final String selectSQL  = String.format("SELECT * FROM %s", tableName);
        try {
            final Statement selectStatement = connection.createStatement();
            final ResultSet results = selectStatement.executeQuery(selectSQL);
            final String field = fieldName;
            results.setFetchSize(1000);
            // Return an anonyomus closure
            return new NumberProducer() {
                @Override
                public int getNextInt() {
                    try {
                        return results.getInt(field);
                    } catch (SQLException e) {
                        e.printStackTrace(); }
                    return -1;
                }

                @Override
                public boolean hasNext() {
                    try {
                        return results.next();
                    } catch (SQLException e) {
                        e.printStackTrace(); }
                    return false;
                }

                @Override
                public void close() {
                    try {
                        selectStatement.close();
                        results.close();
                    } catch (SQLException e) {
                        e.printStackTrace(); }
                }
            };
        } catch (SQLException e) {
            e.printStackTrace(); }
        return null;
    }

    /**
    Explicit termination method. Closes the connection to a database.
     */
    public void close() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Builder object for PostgresDBManager.
     * See pattern Builder for advice.
     */
    public static class PostgresDBManagerBuilder {
        private String connectionString;
        private String userName;
        private String userPassword;
        private int    batchSize;
        private String tableName;
        private String fieldName;
        private NumberProducer numberProducer;


        public String getConnectionString() {
            return connectionString;
        }

        public void setConnectionString(String connectionString) {
            this.connectionString = connectionString;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public int getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(int batchSize) {
            this.batchSize = batchSize;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public NumberProducer getNumberProducer() {
            return numberProducer;
        }

        public void setNumberProducer(NumberProducer numberProducer) {
            this.numberProducer = numberProducer;
        }
    }
}
