package main.components;

/**
 * Created by Łukasz Szymczuk on 19.02.2017.
 */
public class Rental
{
    private int id;
    private String plate;
    private String name;
    private String d_license;
    private String startdate;
    private String returndate;
    private String parked;

    public Rental(int id, String plate, String name, String d_license, String startdate, String returndate, int parked)
    {
        this.id = id;
        this.plate = plate;
        this.name = name;
        this.d_license = d_license;
        this.startdate = startdate;
        this.returndate = returndate;
       if(parked == 1)
           this.parked = "yes";
       else
           this.parked = "no";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getD_license() {
        return d_license;
    }

    public void setD_license(String d_license) {
        this.d_license = d_license;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getReturndate() {
        return returndate;
    }

    public void setReturndate(String returndate) {
        this.returndate = returndate;
    }

    public String getParked() {
        return parked;
    }

    public void setParked(String parked) {
        this.parked = parked;
    }
}
