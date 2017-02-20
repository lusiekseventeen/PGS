package main;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import main.components.Car;
import main.components.CarCard;
import main.components.Rental;
import main.tools.DBController;


import java.util.ArrayList;
import java.util.HashMap;

import static java.time.LocalDate.now;

public class Controller {

    DBController dbController;
    TableView<Rental> historyTable;
    private String current_pic;
    private HashMap<String,VBox> screen;

    //Navigation components
    @FXML public VBox leftMenu;
        @FXML public Label rent_nav;
        @FXML public Label return_nav;
        @FXML public Label list_nav;
        @FXML public Label history_nav;
        @FXML public Label cars_nav;

    //Application views
    @FXML public VBox returnView;
    @FXML public VBox rentView;
    @FXML public VBox listView;
    @FXML public VBox carsView;
    @FXML public VBox historyView;


    public void initialize()
    {
        dbController = new DBController();
        this.screen = new HashMap<String, VBox>();
        this.current_pic = "historyView";
        this.screen.put("rentView", rentView);
        this.screen.put("returnView", returnView);
        this.screen.put("listView", listView);
        this.screen.put("carsView", carsView);
        this.screen.put("historyView", historyView);
        addMenuListeners(rent_nav,return_nav,list_nav,history_nav,cars_nav);
        navigate("rentView");
        avcDate1.setValue(now());
        avcDate2.setValue(now());
    }

    /***************************************************************
     * Navigation
     */
    public void addMenuListeners(Label rent_nav, Label return_nav, Label list_nav, Label history_nav, Label cars_nav)
    {
        rent_nav.setOnMouseClicked(e -> navigate("rentView"));
        return_nav.setOnMouseClicked(e -> navigate("returnView"));
        list_nav.setOnMouseClicked(e -> navigate("listView"));
        cars_nav.setOnMouseClicked(e -> navigate("carsView"));
        history_nav.setOnMouseClicked(e -> navigate("historyView"));
    }

    public void navigate(String navTo)
    {
            loadView(navTo);
            screen.get(current_pic).setVisible(false);
            current_pic = navTo;
            screen.get(current_pic).setVisible(true);
    }

    private void loadView(String current_pic)
    {
        switch (current_pic)
        {
            case "rentView":
                prepareMainViews(rentView);
                break;
            case "returnView":
                prepareMainViews(returnView);
                break;
            case "listView":
                drawList();
                break;
            case "historyView":
                drawTable();
                break;
            case "carsView":
                prepareView(true, false);
                break;
        }
    }
    /**
     * End of Navigation
     ***************************************************************/

    /***************************************************************
     * Rent/Return View Controller
     */
    @FXML  TextField rentPlateField;
    @FXML  TextField returnPlateField;
    public CarCard tmp_cc;
    public Car tmp_c;

    public void showRentCard()
    {
        //TODO: filter
        tmp_c = dbController.getCar(rentPlateField.getText().toUpperCase());
        if(tmp_c == null)
        {
            alert("Car not found", "You have enter plate that does not exist.", "Go to List_ or Cars_ tab to see available cars");
        }
        else
        {
            tmp_cc = new CarCard(tmp_c, "rent");
            tmp_cc.dt(dbController, tmp_c.getPlate());
            tmp_cc.getAction_button().setOnAction(event -> registerRental());
            showCard(tmp_cc, rentView);
        }
    }

    public void showReturnCard()
    {
        tmp_c = dbController.getRentedCar(returnPlateField.getText().toUpperCase());
        if(tmp_c == null)
        {
            alert("Car not found", "You have enter plate that does not exist.", "Go to History_ tab to see rented cars");
        }
        else
        {
            tmp_cc = new CarCard(tmp_c, "return", true);
            tmp_cc.getAction_button().setOnAction(event -> returnCar());
            showCard(tmp_cc, returnView);
        }
    }

    private void prepareMainViews(VBox view)
    {
        if(view.getChildren().size() == 5)
        {
            view.getChildren().remove(4);
        }
        else if(view.getChildren().size() > 5)
        {
            view.getChildren().remove(4,5);
        }
        rentPlateField.setText("");
        returnPlateField.setText("");
    }

    private void showCard(CarCard cc, VBox view)
    {
        if(view.getChildren().size() == 4)
            view.getChildren().add(cc.getCarCard());
        else if(view.getChildren().size() == 5)
        {
            view.getChildren().remove(4);
            view.getChildren().add(cc.getCarCard());
        }
        else
        {
            view.getChildren().remove(4,5);
            view.getChildren().add(cc.getCarCard());
        }
    }

    public void registerRental()
    {
        if(tmp_cc.checkRequiredFields() && tmp_cc.checkDates())
        {
            dbController.rentCar(tmp_cc.getInsertString());
            showInfo("rented", rentView);
            rentView.getChildren().remove(rentView.getChildren().size()-2);
        }
        else
        {
            alert("Required Fields", "All fields are required", "Starting date must be after return date!");
        }
    }

    public void returnCar()
    {
        dbController.returnCar(tmp_cc.getCarID(), tmp_cc.getCarCurrDriver());
        showInfo("returned", returnView);
        returnView.getChildren().remove(returnView.getChildren().size()-2);
    }

    private void showInfo(String info, VBox view)
    {
        Label l = new Label(info);
        l.getStyleClass().add("InfoLabel");
        view.getChildren().add(l);
    }

    /**
     * End of Rent/Return View Controller
     ***************************************************************/

    /***************************************************************
     * Available cars list View Controller
     */
    @FXML DatePicker avcDate1;
    @FXML DatePicker avcDate2;
    @FXML FlowPane avcList;

    public void drawList()
    {
        avcList.getChildren().removeAll(avcList.getChildren());
        if(avcDate1.getValue().isAfter(avcDate2.getValue()))
        {
            alert("Wrong dates!", "It is impossible to rent car with those dates...", "Second date must be after first one.");
        }
        else
        {
            ArrayList<CarCard> cc = dbController.getAvailibleCars(avcDate1.getValue().toString(), avcDate2.getValue().toString());
            for(CarCard c: cc)
            {
                avcList.getChildren().add(c.getCarCard());
                c.getAction_button().setOnAction(e->{navigate("rentView"); rentPlateField.setText(c.getCar().getPlate()); showRentCard();});
                c.getCarCard().getChildren().remove(3,6);
            }
        }
    }

    /**
     * End of Available cars list View Controller
     ***************************************************************/

    /***************************************************************
     * History View Controller
     */
    public void drawTable()
    {
        if (historyView.getChildren().size() == 3)
        {
            historyView.getChildren().remove(2);
            createTable();
        }
        else
            createTable();
    }

    public void createTable()
    {
        historyTable = new TableView<>();
        historyTable.setMinHeight(300.0);

        TableColumn<Rental, Integer> col_id = new TableColumn<>("id");
        col_id.setMinWidth(40);
        TableColumn<Rental, String> col_cplate = new TableColumn<>("car plate");
        col_cplate.setMinWidth(90);
        TableColumn<Rental, String> col_cname = new TableColumn<>("car name");
        col_cname.setMinWidth(240);
        TableColumn<Rental, String> col_dlic = new TableColumn<>("driver license");
        col_dlic.setMinWidth(130);
        TableColumn<Rental, String> col_sdate = new TableColumn<>("start date");
        col_sdate.setMinWidth(140);
        TableColumn<Rental, String> col_rdate = new TableColumn<>("return date");
        col_rdate.setMinWidth(140);
        TableColumn<Rental, String> col_ret = new TableColumn<>("returned");
        col_ret.setMinWidth(60);

        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_cplate.setCellValueFactory(new PropertyValueFactory<>("plate"));
        col_cname.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_dlic.setCellValueFactory(new PropertyValueFactory<>("d_license"));
        col_sdate.setCellValueFactory(new PropertyValueFactory<>("startdate"));
        col_rdate.setCellValueFactory(new PropertyValueFactory<>("returndate"));
        col_ret.setCellValueFactory(new PropertyValueFactory<>("parked"));

        historyTable.getColumns().addAll(col_id, col_cplate, col_cname, col_dlic, col_sdate, col_rdate, col_ret);
        historyTable.setItems(dbController.getRentalsHistory());

        historyView.getChildren().add(historyTable);
        VBox.setVgrow(historyTable, Priority.ALWAYS);
    }

    /**
     * End of History View Controller
     ***************************************************************/

    /***************************************************************
     * Cars View Controller
     */
    @FXML TextField newPlate;
    @FXML TextField newName;
    @FXML ColorPicker newColor;
    @FXML FlowPane carsList;
    @FXML Label actionLabel;
    @FXML Button carsButton;
    Car tmp_carsView;

    void prepareView(Boolean newCar, Boolean deleteCar)
    {
        if(newCar)
        {
            carsButton.setOnAction(e -> addNewCar());
            carsButton.setText("add");
            actionLabel.setText("new car?");
            newPlate.setText("");
            newPlate.setDisable(false);
            newName.setText("");
            newName.setDisable(false);
            newColor.setValue(Color.WHITE);
            newColor.setDisable(false);
        }
        else if(deleteCar)
        {
            carsButton.setOnAction(e -> deleteCar());
            carsButton.setText("delete");
            actionLabel.setText("are your sure you want to delete?");
            newPlate.setText(tmp_carsView.getPlate());
            newPlate.setDisable(true);
            newName.setText((tmp_carsView.getName()));
            newName.setDisable(true);
            newColor.setValue(tmp_carsView.getColorV());
            newColor.setDisable(true);
        }
        else
        {
            carsButton.setOnAction(e -> editCar(tmp_carsView));
            carsButton.setText("edit");
            actionLabel.setText("edit this car");
            newPlate.setText(tmp_carsView.getPlate());
            newName.setText((tmp_carsView.getName()));
            newColor.setValue(tmp_carsView.getColorV());
        }
        drawCarsView();
    }

    public void addNewCar()
    {
        //TODO: check input
        dbController.addCar(new Car(newPlate.getText(), newName.getText(), newColor.getValue().toString().substring(2)).carRegString(), newPlate.getText());
        navigate("carsView");
    }

    void deleteCar()
    {
        dbController.deleteCar(tmp_carsView.getId());
        navigate("carsView");
    }

    void editCar(Car car)
    {
        tmp_carsView.setPlate(newPlate.getText());
        tmp_carsView.setName(newName.getText());
        tmp_carsView.setColor(newColor.getValue().toString().substring(2));
        dbController.editCar(tmp_carsView);
        navigate("carsView");
    }

    public void drawCarsView()
    {
        System.out.println("a"+carsList.getChildren().size());
        if (carsList.getChildren().size() > 1)
        {
            if(carsList.getChildren().size() == 2)
                carsList.getChildren().remove(1);
            else
                carsList.getChildren().remove(1, carsList.getChildren().size());
            createCarsView();
        }
        else
            createCarsView();
    }
    public void createCarsView()
    {
        ArrayList<CarCard> cc = dbController.getCars();
        for(CarCard c: cc)
        {
            carsList.getChildren().add(c.getCarCard());
            c.getAction_button().setOnAction(e->{tmp_carsView = c.getCar(); prepareView(false, false);});
            c.getAction_button2().setOnAction(e->{tmp_carsView = c.getCar(); prepareView(false, true);});
        }
    }

    /**
     * End of cars View Controller
     ***************************************************************/

    /**
     * Notifications
     * @param title
     * @param header
     * @param ctx
     */

    void alert(String title, String header, String ctx)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(ctx);

        alert.showAndWait();
    }

}
