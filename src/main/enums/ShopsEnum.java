package main.enums;

import java.util.Calendar;

import javafx.scene.image.Image;
import main.tools.CommonHelper;

/** Created by martin on 28.7.13. */
public enum ShopsEnum {

  SHOP_INTERSPAR("Interspar", "interspar.image", Calendar.WEDNESDAY), 
  SHOP_TESCO("Tesco", "tesco.image", Calendar.WEDNESDAY), 
  SHOP_BILLA("Billa", "billa.image", Calendar.WEDNESDAY), 
  SHOP_ALBERT("Albert", "albert.image", Calendar.WEDNESDAY), SHOP_LIDL("Lidl", "lidl.image", Calendar.MONDAY), 
  SHOP_KAUFLAND("Kaufland", "kaufland.image", Calendar.THURSDAY),
  SHOP_OTHER("Other", "other.image", Calendar.MONDAY);

  private String name;
  private Image image;
  private int beggingDay;

  ShopsEnum(String name, String imgResource, int beggingDay) {
    this.name = name;
    this.image = new Image(CommonHelper.getImageResourcePath(imgResource));
    this.beggingDay = beggingDay;
  }

  public String getName() {
    return name;
  }

  public Image getImage() {
    return image;
  }

  public int getBeggingDay() {
    return beggingDay;
  }

  @Override
  public String toString() {
    return name;
  }

}
