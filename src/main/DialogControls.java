/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Dialogs;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.enums.DialogTypes;
import main.enums.ShopsEnum;
import main.list.BaseShopsCellFactory;
import extfx.scene.control.CalendarView;

/**
 * 
 * @author martin
 */
public class DialogControls {

  public static void showTextDialog(String text, DialogTypes type) {
    Stage dialog = new Stage();
    switch (type) {
    case CONFIRM:
      Dialogs.showConfirmDialog(dialog, text);
      break;
    case INFORMATION:
      Dialogs.showInformationDialog(dialog, text);
      break;
    case WARNING:
      Dialogs.showWarningDialog(dialog, text);
      break;
    case ERROR:
      Dialogs.showErrorDialog(dialog, text);
      break;
    }
  }

  public static ShopsEnum showShopsPickerDialog() {
    BorderPane pane = new BorderPane();
    pane.setPadding(new Insets(0, 5, 0, 5));

    List<ShopsEnum> shopList = new ArrayList<>(Arrays.asList(ShopsEnum.values()));
    ObservableList<ShopsEnum> shopObservableList = FXCollections.observableList(shopList);
    final ListView<ShopsEnum> lvShops = new ListView<>(shopObservableList);
    lvShops.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    lvShops.setCellFactory(new BaseShopsCellFactory(false));

    pane.setCenter(lvShops);
    pane.setMaxHeight(150);
    Stage dialog = new Stage();

    Dialogs.DialogResponse resp = Dialogs.showCustomDialog(dialog, pane, "Please select a shop:",
        "Shops picker", Dialogs.DialogOptions.OK_CANCEL, null);
    return (resp == Dialogs.DialogResponse.OK) ? lvShops.getSelectionModel().getSelectedItem()
        : null;
  }

  public static Date showDatePickerDialog() {
    BorderPane pane = new BorderPane();
    pane.setPadding(new Insets(0, 5, 0, 5));

    final CalendarView cW = new CalendarView();
    cW.setShowWeeks(true);
    cW.selectedDateProperty().setValue(Calendar.getInstance().getTime());

    Button btn = new Button("Today");
    btn.setMinWidth(350);
    btn.setStyle("-fx-background-color: linear-gradient(to bottom, derive(-fx-base, -70%), derive(-fx-base, -90%));"
        + "\n"
        + "-fx-background-radius: 0,0,0,0;"
        + "\n"
        + "-fx-padding: 5 0 0 0;"
        + "\n"
        + "-fx-text-fill: white;"
        + "\n"
        + "-fx-effect: dropshadow( one-pass-box , rgba(0,0,0,0.8) , 0, 0.0 , 0 , 1);");
    btn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        cW.selectedDateProperty().setValue(Calendar.getInstance().getTime());
      }
    });

    pane.setCenter(cW);
    pane.setMaxHeight(250);
    pane.setMaxWidth(350);
    pane.setBottom(btn);
    Stage dialog = new Stage();
    Dialogs.DialogResponse resp = Dialogs.showCustomDialog(dialog, pane,
        "Please select a date of week:", "Date picker", Dialogs.DialogOptions.OK_CANCEL, null);
    return (resp == Dialogs.DialogResponse.OK) ? cW.getCalendar().getTime() : null;
  }

}
