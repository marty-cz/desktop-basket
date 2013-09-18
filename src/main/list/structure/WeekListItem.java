package main.list.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Created by martin on 28.7.13. */
public class WeekListItem extends CommonListItem implements Serializable {

  private static final long serialVersionUID = 1109385012957892836L;

  protected ObservableList<ShopListItem> shops;

  public WeekListItem(String headLine, Date dateFrom) {
    this(headLine, null, dateFrom);
  }

  public WeekListItem(String headLine, ArrayList<ShopListItem> shops, Date dateFrom) {
    super(headLine, dateFrom);
    this.shops = FXCollections.observableList((shops != null) ? shops
        : new ArrayList<ShopListItem>());
  }

  public ObservableList<ShopListItem> getShops() {
    return shops;
  }

  public void setShops(ObservableList<ShopListItem> shops) {
    this.shops = shops;
  }

  public boolean addShop(ShopListItem item) {
    return shops.add(item);
  }

  public boolean removeShop(ShopListItem item) {
    return shops.remove(item);
  }

  public ShopListItem removeShop(int index) {
    return shops.remove(index);
  }

  @Override
  public int getSubItemCount() {
    return (shops != null) ? shops.size() : 0;
  }

}
