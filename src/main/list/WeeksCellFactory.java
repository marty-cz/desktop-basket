/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.list;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import main.list.structure.WeekListItem;

/**
 * 
 * @author martin
 */
public class WeeksCellFactory implements Callback<ListView<WeekListItem>, ListCell<WeekListItem>> {

  @Override
  public ListCell<WeekListItem> call(ListView<WeekListItem> p) {
    return new XCell();
  }

  static private class XCell extends ListCell<WeekListItem> {

    HBox hbox = new HBox();
    Label label = new Label("(empty)");
    private static final String STYLE = "-fx-font: 13px \"monospace\";";

    public XCell() {
      super();

      label.setStyle(STYLE);
      hbox.getChildren().addAll(label);
      hbox.setAlignment(Pos.CENTER_LEFT);
      HBox.setHgrow(label, Priority.ALWAYS);
    }

    @Override
    protected void updateItem(WeekListItem item, boolean empty) {
      super.updateItem(item, empty);
      setText(null); // No text in label of super class
      if (empty) {
        setGraphic(null);
      } else {
        label.setText((item != null) ? item.getHeadLine() : "(unknown)");
        setGraphic(hbox);
      }
    }
  }

}
