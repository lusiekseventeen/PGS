package main.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Łukasz Szymczuk on 19.02.2017.
 */
public class CreateDataBase
{
    Connection connection;
    public CreateDataBase(String dbname)
    {
        this.connection = createConnection(dbname);
        //addTables(connection);
    }

    private void addTables(Connection connection)
    {
        Statement stat;
        try {
            stat = connection.createStatement();
            String table = "CREATE TABLE cars (\n" +
                    "  id INT PRIMARY KEY AUTOINCREMENT,\n" +
                    "  plate TEXT NOT NULL,\n" +
                    "  name TEXT NOT NULL,\n" +
                    "  color TEXT NOT NULL\n" +
                    ")";
            stat.executeUpdate(table);

            table = "CREATE TABLE rentals " +
                    "(id INT PRIMARY KEY AUTOINCREMENT,\n" +
                    "  carID INT NOT NULL,\n" +
                    "  plate TEXT NOT NULL,\n" +
                    "  name TEXT NOT NULL,\n" +
                    "  d_license TEXT NOT NULL,\n" +
                    "  startdate TEXT NOT NULL,\n" +
                    "  returndate TEXT NOT NULL,\n" +
                    "  parked INT NOT NULL)";
            stat.executeUpdate(table);
        }
        catch (SQLException e)
        {
            System.out.println("Can't create table: " + e.getMessage());
        }
    }

    public static Connection createConnection(String dbname) {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:"+dbname+".db");
            System.out.println("Connect to: "+dbname);
        } catch (Exception e) {
            System.err.println("Error while connecting:: \n" + e.getMessage());
            return null;
        }
        return conn;
    }
}
