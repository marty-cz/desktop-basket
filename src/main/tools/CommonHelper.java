/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.tools;

import java.util.ResourceBundle;

/**
 * 
 * @author martin
 */
public class CommonHelper {

  /**
   * Constant of actual running folder.
   */
  public static String RUNNING_FOLDER = CommonHelper.class.getProtectionDomain().getCodeSource()
      .getLocation().getFile();
  /**
   * Sets parent folder from running app path.
   */
  static {
    if (RUNNING_FOLDER.length() > 0) {
      if (System.getProperty("file.separator").equals("\\")) {
        // fixing bug with bad file separators
        RUNNING_FOLDER = RUNNING_FOLDER.replace("/", System.getProperty("file.separator"));
        if (RUNNING_FOLDER.startsWith(System.getProperty("file.separator"))) {
          RUNNING_FOLDER = RUNNING_FOLDER.substring(1);
        }
      }
      if (RUNNING_FOLDER.endsWith(System.getProperty("file.separator")) == false) {
        int i = RUNNING_FOLDER.lastIndexOf(System.getProperty("file.separator"));
        if (i >= 0) {
          RUNNING_FOLDER = RUNNING_FOLDER.substring(0, i + 1);
        }
      }
    }
  }
  /**
   * Constant of path for temporary folder.
   */
  public static final String TMP_FOLDER = RUNNING_FOLDER + System.getProperty("file.separator")
      + "tmp" + System.getProperty("file.separator");

  static final private ResourceBundle resource = ResourceBundle
      .getBundle("main.resources.CommonHelper");

  static public String getImageResourcePath(String imageName) {
    try {
      return resource.getString(imageName);
    } catch (Exception ex) {
      // Do not trace this exception, because the key could be
      // an already translated string.
      return null;
    }
  }

}
