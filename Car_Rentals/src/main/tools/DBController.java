package main.tools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import main.Main;
import main.components.Car;
import main.components.CarCard;
import main.components.DisabledRange;
import main.components.Rental;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static java.time.LocalDate.now;
import static jdk.nashorn.internal.objects.NativeString.substring;

/**
 * Created by Łukasz Szymczuk on 18.02.2017.
 */
public class DBController
{
    private Connection connection;
    private Statement statment;
    String path1;

    public DBController()
    {
        this.connection = createConnection("rentals");
    }

    public Connection createConnection(String baza) {
        try {
            path1 = new String(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            path1 = path1.substring(0,path1.length()-15);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:"+path1+baza+".db");
        } catch (Exception e) {
            alert("Error while connecting: \n" + e.getMessage());
            return null;
        }
        return conn;
    }

    public void closeConnection()
    {
        try
        {
            connection.close();
        }
        catch (Exception e)
        {
            alert("Error while closing: \n" + e.getMessage());
        }
    }

    public void rentCar(String data)
    {
        statment = null;
        try
        {
            statment = connection.createStatement();
            String insertSQL = "INSERT INTO rentals (carID, license, startdate, returndate, parked) "
                    + "VALUES (" +data+");";
            statment.executeUpdate(insertSQL);
            statment.close();
        } catch (Exception e)
        {
            alert("Can't add: " + e.getMessage());
        }
    }

    public void addCar(String data, String plate)
    {
        statment = null;
        try
        {
            statment = connection.createStatement();
            String insertSQL = "INSERT INTO cars (plate,name,color) "
                    + "VALUES (" +data+");";
            statment.executeUpdate(insertSQL);
            ResultSet rs = statment.executeQuery( "SELECT id FROM cars WHERE plate ='"+plate+"'" );
            if(rs.next())
            {
                insertSQL = "INSERT INTO carsHistory (id,plate,name,color) "
                        + "VALUES ("+rs.getInt("id")+"," +data+");";
                statment.executeUpdate(insertSQL);
            }
            statment.close();
        } catch (Exception e)
        {
            alert("Can't add: " + e.getMessage());
        }
    }

    public void returnCar(int id, String lic)
    {
        statment = null;
        try
        {
            statment = connection.createStatement();
            String insertSQL = "UPDATE rentals SET parked = 1 where carID = "+id+" and license = '"+lic+"' and parked = 0 ;";
            statment.executeUpdate(insertSQL);
            statment.close();
        } catch (Exception e)
        {
            alert("Can't add: " + e.getMessage());
        }
    }


    public ArrayList<CarCard> getCars()
    {
        statment = null;
        ArrayList<CarCard> cc = new ArrayList<CarCard>();
        try
        {
            statment = connection.createStatement();
            ResultSet rs = statment.executeQuery( "SELECT * FROM cars" );
            while(rs.next())
            {
                cc.add(new CarCard(new Car(rs.getString("plate"), rs.getString("name"),rs.getString("color"), rs.getInt("id"))));
            }

            rs.close();
            statment.close();
            return cc;
        } catch (Exception e)
        {
            alert("Can't add: " + e.getMessage());
            return null;
        }
    }

    public ObservableList<DisabledRange> getRanges(String plate)
    {
        statment = null;
        ObservableList<DisabledRange> rangesToDisable = FXCollections.observableArrayList(new DisabledRange(LocalDate.of(2014,10,17), now().minusDays(1)));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try
        {
            statment = connection.createStatement();
            ResultSet rs = statment.executeQuery( "SELECT startdate, returndate FROM rentals INNER JOIN cars ON rentals.carID = cars.id WHERE cars.plate = '"+plate+"' and rentals.parked = 0" );
            while(rs.next())
            {
                rangesToDisable.add(new DisabledRange(LocalDate.parse(rs.getString("startdate"), formatter), LocalDate.parse(rs.getString("returndate"), formatter)));
            }
            rs.close();
            statment.close();
            return rangesToDisable;
        } catch (Exception e)
        {
            alert("Can't get list: " + e.getMessage());
            return null;
        }
    }

    public Car getCar(String plate)
    {
        statment = null;
        Car c;
        try
        {
            statment = connection.createStatement();
            ResultSet rs = statment.executeQuery( "SELECT * FROM cars WHERE plate = '"+plate+"'" );
            if(rs.next())
            {
                c = new Car(rs.getString("plate"),rs.getString("name"), rs.getString("color"));
                c.setID(rs.getInt("id"));
            }
            else
                return null;
            rs.close();
            statment.close();
            return c;
        } catch (Exception e)
        {
            alert("Can't get: " + e.getMessage());
            return null;
        }
    }

    public Car getRentedCar(String plate)
    {
        statment = null;
        Car c;
        try
        {
            statment = connection.createStatement();
            ResultSet rs = statment.executeQuery( "SELECT cars.id, cars.plate, cars.name, cars.color, rentals.license FROM cars INNER JOIN rentals ON cars.id = rentals.carID WHERE cars.plate = '"+plate+"' and rentals.parked = 0" );
            if(rs.next())
            {
                c = new Car(rs.getString("plate"),rs.getString("name"), rs.getString("color"));
                c.setCurr_driver(rs.getString("license"));
                c.setID(rs.getInt("id"));
            }
            else
            {
                return null;
            }
            rs.close();
            statment.close();
            return c;
        } catch (Exception e)
        {
            alert("Can't get: " + e.getMessage());
            return null;
        }
    }

    public ObservableList<Rental> getRentalsHistory()
    {
        ObservableList<Rental> rentals = FXCollections.observableArrayList();

        statment = null;
        try
        {
            statment = connection.createStatement();
            ResultSet rs = statment.executeQuery( "SELECT rentals.id, carsHistory.plate, carsHistory.name, rentals.license, rentals.startdate, rentals.returndate, rentals.parked FROM rentals INNER JOIN carsHistory ON carsHistory.id = rentals.carID ORDER BY rentals.startdate" );
            while(rs.next())
            {
                rentals.add(new Rental(rs.getInt("id"),rs.getString("plate"),rs.getString("name"),rs.getString("license"),rs.getString("startdate"),rs.getString("returndate"),rs.getInt("parked")));
            }

            rs.close();
            statment.close();
            return rentals;
        } catch (Exception e)
        {
            alert("Can't get: " + e.getMessage());
            return null;
        }
    }

    public void editCar(Car car)
    {
        statment = null;
        try
        {
            statment = connection.createStatement();
            String insertSQL = "UPDATE cars SET plate = '"+car.getPlate()+"', name = '"+car.getName()+"', color = '"+car.getColor()+"' where id = "+car.getId();
            statment.executeUpdate(insertSQL);
            statment.close();
        } catch (Exception e)
        {
            alert("Can't add: " + e.getMessage());
        }
    }

    public void deleteCar(int id)
    {
        statment = null;
        try
        {
            statment = connection.createStatement();
            String insertSQL = "DELETE FROM cars WHERE id = "+id;
            statment.executeUpdate(insertSQL);
            statment.close();
        } catch (Exception e)
        {
            alert("Can't delete: " + e.getMessage());
        }
    }

    public ArrayList<CarCard> getAvailibleCars(String d1, String d2)
    {
        statment = null;
        ArrayList<CarCard> cc = new ArrayList<CarCard>();
        try
        {
            statment = connection.createStatement();
            ResultSet rs = statment.executeQuery( "SELECT id, plate, name, color FROM Cars EXCEPT SELECT rentals.carID, cars.plate, cars.name, cars.color from rentals inner join cars on rentals.carID = cars.id where ((DATE(startdate) >= DATE('"+d1+"') AND DATE(startdate) <= DATE('"+d2+"')) OR (DATE(startdate) <= DATE('"+d1+"') AND DATE(returndate) >= DATE('"+d2+"')) OR (DATE(returndate) <= DATE('"+d1+"') AND DATE(returndate) <= DATE('"+d2+"'))) AND rentals.parked = 0" );
            while(rs.next())
            {
                cc.add(new CarCard(new Car(rs.getString("plate"), rs.getString("name"),rs.getString("color"), rs.getInt("id")), "rent"));
            }

            rs.close();
            statment.close();
            return cc;
        } catch (Exception e)
        {
            alert("Can't add: " + e.getMessage());
            return null;
        }
    }

    void alert(String header)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database error");
        alert.setHeaderText(header);

        alert.showAndWait();
    }
}
