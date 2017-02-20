package main.components;

import javafx.scene.paint.Color;

/**
 * Created by Łukasz Szymczuk on 19.02.2017.
 */
public class Car
{
    private String plate;
    private String name;
    private String color;
    private int id;

    public String getCurr_driver() {
        return curr_driver;
    }

    private String curr_driver;


    public void setCurr_driver(String s)
    {
        curr_driver = s;
    }

    public Car(String plate, String name, String color)
    {
        this.plate = plate;
        this.name = name;
        this.color = color;
    }

    public Car(String plate, String name, String color, int id)
    {
        this.plate = plate;
        this.name = name;
        this.color = color;
        this.id = id;
    }

    public void setID(int id)
    {
        this.id = id;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
    public Color getColorV()
    {
        return Color.web(color);
    }

    public String carRegString  ()
    {
        return "'"+plate+"', '"+name+"', '"+color+"'";
    }

    public String rentalString()
    {
        return ", "+id+", '"+plate.toUpperCase()+"', '"+name+"'";
    }

    public int getId() {
        return id;
    }
}
