package main.tools;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.HashMap;

/**
 * Created by Łukasz Szymczuk on 18.02.2017.
 */
public class Navigation
{
    private String current_pic;
    private HashMap<String,VBox> screen;


    public Navigation(VBox rentView, VBox returnView, VBox listView, VBox historyView, VBox carsView, Label rent_nav, Label return_nav, Label list_nav, Label history_nav, Label cars_nav)
    {
        this.screen = new HashMap<String, VBox>();
        this.current_pic = "historyView";
        this.screen.put("rentView", rentView);
        this.screen.put("returnView", returnView);
        this.screen.put("listView", listView);
        this.screen.put("carsView", carsView);
        this.screen.put("historyView", historyView);
        addMenuListeners(rent_nav, return_nav, list_nav, history_nav, cars_nav);
    }

    private void addMenuListeners(Label rent_nav, Label return_nav, Label list_nav, Label cars_nav, Label history_nav)
    {
        rent_nav.setOnMouseClicked(e -> navigate("rentView"));
        return_nav.setOnMouseClicked(e -> navigate("returnView"));
        list_nav.setOnMouseClicked(e -> navigate("listView"));
        cars_nav.setOnMouseClicked(e -> navigate("carsView"));
        history_nav.setOnMouseClicked(e -> navigate("historyView"));
    }

    private void navigate(String navTo)
    {
        if(!navTo.equals(current_pic))
        {
            screen.get(current_pic).setVisible(false);
            current_pic = navTo;
            screen.get(current_pic).setVisible(true);
        }

    }
}
