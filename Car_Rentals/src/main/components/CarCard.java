package main.components;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import main.tools.DBController;

/**
 * Created by Łukasz Szymczuk on 17.02.2017.
 */
public class CarCard {
    Car car;
    VBox card;
    Label plate;
    Label name;
    Label color;
    TextField licence_number;
    DatePicker start_picker;
    DatePicker return_picker;
    Button action_button;
    Button action_button2;

    public Button getAction_button() {
        return action_button;
    }

    public Car getCar() {
        return car;
    }

    /**

     * Rent View constructor
     */
    public CarCard(Car car, String a)
    {
        this.car = car;
        this.plate = new Label(car.getPlate());
            this.plate.getStyleClass().add("Plate");

        this.name = new Label(car.getName());
            this.name.getStyleClass().add("CarName");

        this.color = new Label();
            this.color.getStyleClass().add("ColorLabel");
            this.color.setStyle("-fx-background-color: #"+car.getColor()+";");

        this.action_button = new Button(a);
            this.action_button.getStyleClass().add("MyButton");

        this.licence_number = new TextField();
        this.start_picker = preparePicker();
        this.return_picker = preparePicker();

        this.card = new VBox();
            this.card.getStyleClass().add("CarCard");
            this.card.getChildren().add(plate);
            this.card.getChildren().add(name);
            this.card.getChildren().add(color);
            this.card.getChildren().add(licence_number);
            this.card.getChildren().add(start_picker);
            this.card.getChildren().add(return_picker);
            this.card.getChildren().add(action_button);
    }

    /**
     * Return View constructor
     */
    public CarCard(Car car, String a, boolean b)
    {
        this.car = car;
        this.plate = new Label(car.getPlate());
            this.plate.getStyleClass().add("Plate");

        this.name = new Label(car.getName());
            this.name.getStyleClass().add("CarName");

        this.color = new Label();
            this.color.getStyleClass().add("ColorLabel");
            this.color.setStyle("-fx-background-color: #"+car.getColor()+";");

        this.action_button = new Button(a);
            this.action_button.getStyleClass().add("MyButton");

        this.licence_number = new TextField(car.getCurr_driver());
            this.licence_number.setEditable(false);

        this.card = new VBox();
            this.card.setId("returnCard");
            this.card.getStyleClass().add("CarCard");
            this.card.getChildren().add(plate);
            this.card.getChildren().add(name);
            this.card.getChildren().add(color);
            this.card.getChildren().add(licence_number);
            this.card.getChildren().add(action_button);
    }

    /**
     * Cars View constructor
     */
    public CarCard(Car car)
    {
        this.car = car;
        this.plate = new Label(car.getPlate());
        this.plate.getStyleClass().add("Plate");

        this.name = new Label(car.getName());
        this.name.getStyleClass().add("CarName");

        this.color = new Label();
            this.color.getStyleClass().add("ColorLabel");
            this.color.setStyle("-fx-background-color: #"+car.getColor()+";");

        this.action_button = new Button("edit");
        this.action_button.getStyleClass().add("MyButton2");

        this.action_button2 = new Button("delete");
        this.action_button2.getStyleClass().add("MyButton");

        this.card = new VBox();
        this.card.getStyleClass().add("CarCard");
        this.card.getChildren().add(plate);
        this.card.getChildren().add(name);
        this.card.getChildren().add(color);
        this.card.getChildren().add(action_button);
        this.card.getChildren().add(action_button2);

    }

    private DatePicker preparePicker()
    {
        return new DatePicker();
    }

    public VBox getCarCard()
    {
        return card;
    }

    public String getInsertString()
    {
        return car.getId()+", '"+licence_number.getText()+"'"+", '"+start_picker.getValue().toString()+"', '"+return_picker.getValue().toString()+"', 0";
    }

    public String getReturnString()
    {
        return car.getId()+", '"+licence_number.getText()+"'"+", '"+start_picker.getValue().toString()+"', "+return_picker.getValue().toString()+"', 0";
    }

    public String getCarCurrDriver() {
        return car.getCurr_driver();
    }

    public int getCarID() {
        return car.getId();
    }

    public void setCarId(int id)
    {
        this.car.setID(id);
    }

    public Button getAction_button2() {
        return action_button2;
    }

    public boolean checkRequiredFields()
    {
        return !(licence_number.getText().trim().isEmpty() || start_picker.getValue().toString() == "" || return_picker.getValue().toString() == "");
    }

    public boolean checkDates()
    {
        return start_picker.getValue().isBefore(return_picker.getValue());
    }

    public void dt(DBController db, String plate) {
        DatePickerMaker d = new DatePickerMaker(db.getRanges(plate));
        start_picker.setDayCellFactory(d.getCallBackStart());
        return_picker.setDayCellFactory(d.getCallBackReturn(start_picker));
    }
}
