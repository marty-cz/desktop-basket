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
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import main.enums.ShopsEnum;

/**
 * 
 * @author martin
 */
public class BaseShopsCellFactory implements Callback<ListView<ShopsEnum>, ListCell<ShopsEnum>> {

  private boolean smallStyle;

  public BaseShopsCellFactory(boolean smallStyle) {
    this.smallStyle = smallStyle;
  }

  @Override
  public ListCell<ShopsEnum> call(ListView<ShopsEnum> p) {
    return new XCell(smallStyle);
  }

  static private class XCell extends ListCell<ShopsEnum> {

    HBox hbox = new HBox();
    Label label = new Label("(empty)");
    ImageView img = new ImageView();
    private static final String SMALL_STYLE = "-fx-font: 13px \"monospace\";"
        + " -fx-font-weight: bold; -fx-padding: 0 0 0 5;";
    private static final String NORMAL_STYLE = "-fx-font: 16px \"monospace\";"
        + " -fx-font-weight: bold; -fx-padding: 0 0 0 10;";

    public XCell(boolean smallStyle) {
      super();
      img.setPreserveRatio(true);

      img.setFitWidth(smallStyle ? 20.0 : 30.0);
      label.setStyle(smallStyle ? SMALL_STYLE : NORMAL_STYLE);
      hbox.getChildren().addAll(img, label);
      hbox.setAlignment(Pos.CENTER_LEFT);
      HBox.setHgrow(label, Priority.ALWAYS);
    }

    @Override
    protected void updateItem(ShopsEnum item, boolean empty) {
      super.updateItem(item, empty);
      setText(null); // No text in label of super class
      if (empty) {
        setGraphic(null);
      } else {
        label.setText((item != null) ? item.getName() : "(unknown)");
        img.setImage((item != null) ? item.getImage() : null);
        setGraphic(hbox);
      }
    }
  }

}
