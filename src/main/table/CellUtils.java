/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package main.table;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Cell;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import extfx.scene.control.NumberSpinner;

// Package protected - not intended for external use
@SuppressWarnings({ "rawtypes", "unchecked" })
class CellUtils {

  /***************************************************************************
   * * Private fields * *
   **************************************************************************/

  private final static StringConverter defaultStringConverter = new StringConverter<Object>() {
    @Override
    public String toString(Object t) {
      return t == null ? null : t.toString();
    }

    @Override
    public Object fromString(String string) {
      return (Object) string;
    }
  };

  private final static StringConverter defaultTreeItemStringConverter = new StringConverter<TreeItem>() {
    @Override
    public String toString(TreeItem treeItem) {
      return (treeItem == null || treeItem.getValue() == null) ? "" : treeItem.getValue()
          .toString();
    }

    @Override
    public TreeItem fromString(String string) {
      return new TreeItem(string);
    }
  };

  /***************************************************************************
   * * General convenience * *
   **************************************************************************/

  /*
   * Simple method to provide a StringConverter implementation in various cell
   * implementations.
   */
  static <T> StringConverter<T> defaultStringConverter() {
    return (StringConverter<T>) defaultStringConverter;
  }

  /*
   * Simple method to provide a TreeItem-specific StringConverter implementation
   * in various cell implementations.
   */
  static <T> StringConverter<TreeItem<T>> defaultTreeItemStringConverter() {
    return (StringConverter<TreeItem<T>>) defaultTreeItemStringConverter;
  }

  private static <T> String getItemText(Cell<T> cell, StringConverter<T> converter) {
    if (converter == null) {
      if (cell.getItem() == null) {
        return "";
      } else {
        return cell.getItem().toString();
      }
    } else if (cell.getItem() instanceof Boolean) {
      return ((Boolean) cell.getItem()).toString();
    } else if (cell.getItem() instanceof Number) {
      return ((Number) cell.getItem()).toString();
    } else {
      return converter.toString(cell.getItem());
    }
  }

  /***************************************************************************
   * * CheckBox convenience * *
   **************************************************************************/

  static <T> void updateItem(final Cell<T> cell, CheckBox checkbox,
      final StringConverter<T> converter) {
    if (cell.isEmpty()) {
      cell.setText(null);
      cell.setGraphic(null);
    } else {
      if (checkbox != null) {
        checkbox.setSelected(new Boolean(cell.getItem().toString()));
      }
      cell.setText(null);
      cell.setGraphic(checkbox);
    }
  };

  static <T> CheckBox createCheckBox(final Cell<T> cell, final Boolean item) {
    final CheckBox checkbox = new CheckBox();
    checkbox.setSelected(item);
    checkbox.focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
          Boolean newValue) {
        if (!newValue.booleanValue()) {
          cell.commitEdit((T) new Boolean(checkbox.isSelected()));
        }
      }
    });
    return checkbox;
  }

  /***************************************************************************
   * * NumberSpinner convenience * *
   **************************************************************************/

  static <T> void updateItem(Cell<T> cell, NumberSpinner spinner, StringConverter<T> converter) {
    if (cell.isEmpty()) {
      cell.setText(null);
      cell.setGraphic(null);
    } else {
      if (cell.isEditing()) {
        if (spinner != null) {
          try {
            spinner.setValue(new Double(getItemText(cell, converter)));
          } catch (Exception e) {
            spinner.setValue(0.0);
          }
        }
        cell.setText(null);
        cell.setGraphic(spinner);
      } else {
        cell.setText(getItemText(cell, converter));
        cell.setGraphic(null);
      }
    }
  }

  static <T> void startEdit(final Cell<T> cell, NumberSpinner spinner,
      final StringConverter<T> converter) {
    if (spinner == null) {
      spinner = createNumberSpinner(cell, converter);
    }
    try {
      spinner.setValue(new Double(getItemText(cell, converter)));
    } catch (Exception e) {
      spinner.setValue(0.0);
    }
    cell.setText(null);
    cell.setGraphic(spinner);
  }

  static <T> void cancelEdit(Cell<T> cell, final StringConverter<T> converter) {
    cell.setText(getItemText(cell, converter));
    cell.setGraphic(null);
  }

  static <T> NumberSpinner createNumberSpinner(final Cell<T> cell,
      final StringConverter<T> converter) {
    final NumberSpinner spinner = new NumberSpinner(0.0, 1000000.0);
    try {
      spinner.setValue(new Double(getItemText(cell, converter)));
    } catch (Exception e) {
      spinner.setValue(0.0);
    }
    spinner.focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
          Boolean newValue) {
        if (!newValue.booleanValue())
          cell.commitEdit((T) new Double(spinner.getValue().doubleValue()));
      }
    });
    spinner.setOnKeyReleased(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent t) {
        if (t.getCode() == KeyCode.ENTER) {
          cell.commitEdit((T) new Double(spinner.getValue().doubleValue()));
        } else if (t.getCode() == KeyCode.ESCAPE) {
          cell.cancelEdit();
        }
      }
    });
    return spinner;
  }
}