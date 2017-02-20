package main.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by Łukasz Szymczuk on 20.02.2017.
 */
public class DisabledRange {
    private final LocalDate initialDate;
    private final LocalDate endDate;

    public DisabledRange(LocalDate initialDate, LocalDate endDate){
        this.initialDate=initialDate;
        this.endDate = endDate;
    }

    public LocalDate getInitialDate() { return initialDate; }
    public LocalDate getEndDate() { return endDate; }

}
