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
public class DatePickerMaker {
    private ObservableList<DisabledRange> rangesToDisable;

    public DatePickerMaker(ObservableList<DisabledRange> ob)
    {
        this.rangesToDisable = ob;
    }

    public Callback<DatePicker, DateCell> getCallBackStart() {
        return new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        boolean disable = rangesToDisable.stream()
                                .filter(r -> r.getInitialDate().minusDays(1).isBefore(item))
                                .filter(r -> r.getEndDate().plusDays(1).isAfter(item))
                                .findAny()
                                .isPresent();

                        if (disable) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
    }

    public Callback<DatePicker, DateCell> getCallBackReturn(DatePicker d1) {
        return new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        boolean disable = rangesToDisable.stream()
                                .filter(r -> r.getInitialDate().minusDays(1).isBefore(item))
                                .filter(r -> r.getEndDate().plusDays(1).isAfter(item))
                                .findAny()
                                .isPresent();

                        if (item.isBefore(d1.getValue().plusDays(1))){
                            setDisable(true);
                            setStyle("-fx-background-color: chartreuse;");
                        }
                        if (disable){
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
    }

}
