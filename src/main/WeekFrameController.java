/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import main.enums.DialogTypes;
import main.enums.ShopsEnum;
import main.list.ShopsCellFactory;
import main.list.WeeksCellFactory;
import main.list.structure.BasketListItem;
import main.list.structure.ShopListItem;
import main.list.structure.StructureWrapper;
import main.list.structure.WeekListItem;
import main.table.MyCheckBoxTableCell;
import main.table.MyNumberSpinnerTableCell;
import main.tools.CommonHelper;
import main.tools.Connection;
import main.tools.Serializer;

/**
 * 
 * @author martin
 */
public class WeekFrameController implements Initializable {

  @FXML
  private Label lblShops;
  @FXML
  private Label lblBasket;
  @FXML
  private ToolBar tbMain;
  @FXML
  private HBox boxWeek;
  @FXML
  private HBox boxCtrlWeek;
  @FXML
  private HBox boxCtrlShops;
  @FXML
  private ListView<ShopListItem> lvShops;
  @FXML
  private TableView<BasketListItem> tvBasket;
  @SuppressWarnings("rawtypes")
  @FXML
  private TableColumn tcBuyed;
  @SuppressWarnings("rawtypes")
  @FXML
  private TableColumn tcProduct;
  @SuppressWarnings("rawtypes")
  @FXML
  private TableColumn tcPrice;
  @FXML
  private ComboBox<WeekListItem> cmbWeek;
  @FXML
  private Button btnWeekRm;
  @FXML
  private Button btnWeekAdd;
  @FXML
  private Button btnDownload;
  @FXML
  private Button btnUpload;
  @FXML
  private Button btnShopRm;
  @FXML
  private Button btnShopAdd;

  private StructureWrapper wrapper;
  private ObservableList<ShopListItem> shopList;
  private ObservableList<WeekListItem> weekList;
  private ObservableList<BasketListItem> basketList;

  private final static String JSON_FILE_PATH = CommonHelper.RUNNING_FOLDER + "basket.json";

  @FXML
  private void btnDownloadAction(ActionEvent event) {
    if (Connection.getInstance().receiveFile(JSON_FILE_PATH)) {
      Object o = Serializer.getInstance().deserializeFromJson(JSON_FILE_PATH,
          StructureWrapper.class);
      if (o != null) {
        wrapper = (StructureWrapper) o;
        weekList = wrapper.getWeeks();
        cmbWeek.setItems(weekList);
        if (!weekList.isEmpty()) {
          btnWeekRm.setDisable(false);
          cmbWeek.getSelectionModel().selectFirst();
        }
      } else {
        DialogControls.showTextDialog(Serializer.getInstance().getErrString(), DialogTypes.ERROR);
      }
    } else {
      DialogControls.showTextDialog(Connection.getInstance().getResponseStr(), DialogTypes.ERROR);
    }
  }

  @FXML
  private void btnUploadAction(ActionEvent event) {
    if (Serializer.getInstance().serializeToJson(JSON_FILE_PATH, wrapper, StructureWrapper.class)) {
      if (Connection.getInstance().sendFile(JSON_FILE_PATH)) {
        DialogControls.showTextDialog("Successfully uploaded :)", DialogTypes.INFORMATION);
      } else {
        DialogControls.showTextDialog(Connection.getInstance().getResponseStr(), DialogTypes.ERROR);
      }
    } else {
      DialogControls.showTextDialog(Serializer.getInstance().getErrString(), DialogTypes.ERROR);
    }
  }

  @FXML
  private void btnWeekRmAction(ActionEvent event) {
    int i = cmbWeek.getSelectionModel().getSelectedIndex();
    if (i >= 0) {
      weekList.remove(i);
      cmbWeek.requestFocus();
      if (!weekList.isEmpty()) {
        if (i < weekList.size()) {
          cmbWeek.getSelectionModel().select(i);
        } else {
          cmbWeek.getSelectionModel().select(i - 1);
        }
      } else {
        btnShopAdd.setDisable(true);
        btnShopRm.setDisable(true);
        btnWeekRm.setDisable(true);
        tvBasket.setDisable(true);
      }
    }
  }

  @SuppressWarnings("deprecation")
  @FXML
  private void btnWeekAddAction(ActionEvent event) {
    Date d = DialogControls.showDatePickerDialog();
    if (d != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(d);
      // "calculate" the start date of the week
      Calendar first = (Calendar) cal.clone();
      first.add(Calendar.DAY_OF_WEEK, first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));
      // and add six days to the end date
      Calendar last = (Calendar) first.clone();
      last.add(Calendar.DAY_OF_YEAR, 6);

      WeekListItem item = new WeekListItem(cal.get(Calendar.WEEK_OF_YEAR) + ". Week",
          first.getTime());
      item.setDateTo(last.getTime());
      for (WeekListItem week : weekList) {
        if (week.getDateFrom().getDate() == item.getDateFrom().getDate()) {
          return; // do not add same week
        }
      }
      weekList.add(item);
      int i = weekList.indexOf(item);
      cmbWeek.requestFocus();
      cmbWeek.getSelectionModel().select(i);
      btnShopAdd.setDisable(false);
      btnShopRm.setDisable(true);
      btnWeekRm.setDisable(false);
      tvBasket.setDisable(true);
    }
  }

  @FXML
  private void btnShopRmAction(ActionEvent event) {
    if (cmbWeek.getSelectionModel().getSelectedItem() == null) {
      DialogControls.showTextDialog("Week must be selected!", DialogTypes.ERROR);
      return;
    }
    int i = lvShops.getSelectionModel().getSelectedIndex();
    if (i >= 0) {
      shopList.remove(i);
      lvShops.requestFocus();
      if (!shopList.isEmpty()) {
        if (i < shopList.size()) {
          lvShops.getSelectionModel().select(i);
        } else {
          lvShops.getSelectionModel().select(i - 1);
        }
      } else {
        btnShopRm.setDisable(true);
        tvBasket.setDisable(true);
      }
    }
  }

  @FXML
  private void btnShopAddAction(ActionEvent event) {
    if (cmbWeek.getSelectionModel().getSelectedItem() == null) {
      DialogControls.showTextDialog("Week must be selected!", DialogTypes.ERROR);
      return;
    }
    ShopsEnum shop = DialogControls.showShopsPickerDialog();
    if (shop != null) { // add shop
      Calendar cal = Calendar.getInstance();
      cal.setTime(cmbWeek.getSelectionModel().getSelectedItem().getDateFrom());
      while (cal.get(Calendar.DAY_OF_WEEK) != shop.getBeggingDay()) {
        cal.add(Calendar.DAY_OF_YEAR, 1);
      }
      // set the end date - add six days
      Calendar toDate = (Calendar) cal.clone();
      toDate.add(Calendar.DAY_OF_YEAR, 6);

      ShopListItem item = new ShopListItem(shop, cal.getTime(), toDate.getTime());
      for (ShopListItem sh : shopList) {
        if (sh.getShop() == item.getShop()) {
          return; // do not add same shop
        }
      }
      shopList.add(item);
      int i = shopList.indexOf(item);
      lvShops.requestFocus();
      lvShops.getSelectionModel().select(i);
      btnShopRm.setDisable(false);
    }
  }

  @FXML
  public void tvBasketKeyPressed(KeyEvent event) {
    if (tvBasket.getEditingCell() == null || tvBasket.getEditingCell().getRow() < 0) {
      if (event.getCode() == KeyCode.DELETE) {
        int i = tvBasket.getSelectionModel().getSelectedIndex();
        if (i >= 0) {
          basketList.remove(i);
          tvBasket.requestFocus();
          if (!basketList.isEmpty()) {
            if (i < basketList.size()) {
              tvBasket.getSelectionModel().select(i);
            } else {
              tvBasket.getSelectionModel().select(i - 1);
            }
          }
        }
      } else if (event.getCode() == KeyCode.INSERT) {
        BasketListItem item = new BasketListItem("", 0);
        basketList.add(item);
        int i = basketList.indexOf(item);
        tvBasket.requestFocus();
        tvBasket.getSelectionModel().select(i);
      }
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    wrapper = new StructureWrapper();

    WeeksCellFactory cellFactory = new WeeksCellFactory();
    weekList = wrapper.getWeeks();
    cmbWeek.setItems(weekList);
    cmbWeek.getSelectionModel();
    cmbWeek.setCellFactory(cellFactory);
    cmbWeek.setButtonCell(cellFactory.call(null));
    cmbWeek.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<WeekListItem>() {
          @Override
          public void changed(ObservableValue ov, WeekListItem t, WeekListItem t1) {
            if ((ov != null) && (ov.getValue() instanceof WeekListItem)) {
              shopList = ((WeekListItem) ov.getValue()).getShops();
              btnShopAdd.setDisable(false);
            } else {
              shopList = null;
              btnShopAdd.setDisable(true);
            }
            lvShops.setItems(shopList);
            if ((shopList != null) && !shopList.isEmpty()) {
              btnShopRm.setDisable(false);
              lvShops.getSelectionModel().selectFirst();
            }
          }
        });

    shopList = null;
    lvShops.setItems(shopList);
    lvShops.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    lvShops.setCellFactory(new ShopsCellFactory(true));
    lvShops.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<ShopListItem>() {
          @Override
          public void changed(ObservableValue ov, ShopListItem t, ShopListItem t1) {
            if ((ov != null) && (ov.getValue() instanceof ShopListItem)) {
              basketList = ((ShopListItem) ov.getValue()).getBasket();
              tvBasket.setDisable(false);
            } else {
              basketList = null;
              tvBasket.setDisable(true);
            }
            tvBasket.setItems(basketList);
          }
        });

    basketList = null;
    tvBasket.setItems(basketList);
    tvBasket.setEditable(true);

    tcBuyed.setCellValueFactory(new PropertyValueFactory<BasketListItem, Boolean>("buyed"));
    tcBuyed.setCellFactory(MyCheckBoxTableCell.forTableColumn());
    tcBuyed.setOnEditCommit(new EventHandler<CellEditEvent<BasketListItem, Boolean>>() {
      @Override
      public void handle(CellEditEvent<BasketListItem, Boolean> t) {
        ((BasketListItem) t.getTableView().getItems().get(t.getTablePosition().getRow()))
            .setBuyed(t.getNewValue());
      }
    });

    tcProduct.setCellValueFactory(new PropertyValueFactory<BasketListItem, String>("headLine"));
    tcProduct.setCellFactory(TextFieldTableCell.forTableColumn());
    tcProduct.setOnEditCommit(new EventHandler<CellEditEvent<BasketListItem, String>>() {
      @Override
      public void handle(CellEditEvent<BasketListItem, String> t) {
        ((BasketListItem) t.getTableView().getItems().get(t.getTablePosition().getRow()))
            .setHeadLine(t.getNewValue());
      }
    });

    tcPrice.setCellValueFactory(new PropertyValueFactory<BasketListItem, Double>("price"));
    tcPrice.setCellFactory(MyNumberSpinnerTableCell.forTableColumn());
    tcPrice.setOnEditCommit(new EventHandler<CellEditEvent<BasketListItem, Double>>() {
      @Override
      public void handle(CellEditEvent<BasketListItem, Double> t) {
        ((BasketListItem) t.getTableView().getItems().get(t.getTablePosition().getRow()))
            .setPrice(t.getNewValue());
      }
    });

    btnShopAdd.setDisable(true);
    btnShopRm.setDisable(true);
    btnWeekRm.setDisable(true);
    tvBasket.setDisable(true);
  }

}
