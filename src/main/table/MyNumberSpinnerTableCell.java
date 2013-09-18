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

import extfx.scene.control.NumberSpinner;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/**
 * A class containing a {@link TableCell} implementation that draws a
 * {@link TextField} node inside the cell.
 * 
 * <p>
 * By default, the TextFieldTableCell is rendered as a {@link Label} when not
 * being edited, and as a TextField when in editing mode. The TextField will, by
 * default, stretch to fill the entire table cell.
 * 
 * @param <T>
 *          The type of the elements contained within the TableColumn.
 * @since 2.2
 */
public class MyNumberSpinnerTableCell<S, T> extends TableCell<S, T> {

  /***************************************************************************
   * * Static cell factories * *
   **************************************************************************/

  /**
   * Provides a {@link TextField} that allows editing of the cell content when
   * the cell is double-clicked, or when
   * {@link TableView#edit(int, javafx.scene.control.TableColumn)} is called.
   * This method will only work on {@link TableColumn} instances which are of
   * type String.
   * 
   * @return A {@link Callback} that can be inserted into the
   *         {@link TableColumn#cellFactoryProperty() cell factory property} of
   *         a TableColumn, that enables textual editing of the content.
   */
  public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> forTableColumn() {
    return forTableColumn(new DefaultStringConverter());
  }

  /**
   * Provides a {@link TextField} that allows editing of the cell content when
   * the cell is double-clicked, or when
   * {@link TableView#edit(int, javafx.scene.control.TableColumn) } is called.
   * This method will work on any {@link TableColumn} instance, regardless of
   * its generic type. However, to enable this, a {@link StringConverter} must
   * be provided that will convert the given String (from what the user typed
   * in) into an instance of type T. This item will then be passed along to the
   * {@link TableColumn#onEditCommitProperty()} callback.
   * 
   * @param converter
   *          A {@link StringConverter} that can convert the given String (from
   *          what the user typed in) into an instance of type T.
   * @return A {@link Callback} that can be inserted into the
   *         {@link TableColumn#cellFactoryProperty() cell factory property} of
   *         a TableColumn, that enables textual editing of the content.
   */
  public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(
      final StringConverter<T> converter) {
    return new Callback<TableColumn<S, T>, TableCell<S, T>>() {
      @Override
      public TableCell<S, T> call(TableColumn<S, T> list) {
        return new MyNumberSpinnerTableCell<S, T>(converter);
      }
    };
  }

  /***************************************************************************
   * * Fields * *
   **************************************************************************/

  private NumberSpinner spinner;

  /***************************************************************************
   * * Constructors * *
   **************************************************************************/

  /**
   * Creates a default TextFieldTableCell with a null converter. Without a
   * {@link StringConverter} specified, this cell will not be able to accept
   * input from the TextField (as it will not know how to convert this back to
   * the domain object). It is therefore strongly encouraged to not use this
   * constructor unless you intend to set the converter separately.
   */
  public MyNumberSpinnerTableCell() {
    this(null);
  }

  /**
   * Creates a TextFieldTableCell that provides a {@link TextField} when put
   * into editing mode that allows editing of the cell content. This method will
   * work on any TableColumn instance, regardless of its generic type. However,
   * to enable this, a {@link StringConverter} must be provided that will
   * convert the given String (from what the user typed in) into an instance of
   * type T. This item will then be passed along to the
   * {@link TableColumn#onEditCommitProperty()} callback.
   * 
   * @param converter
   *          A {@link StringConverter converter} that can convert the given
   *          String (from what the user typed in) into an instance of type T.
   */
  public MyNumberSpinnerTableCell(StringConverter<T> converter) {
    // this.getStyleClass().add("text-field-table-cell");
    setConverter(converter);
    spinner = CellUtils.createNumberSpinner(this, converter);
  }

  /***************************************************************************
   * * Properties * *
   **************************************************************************/

  // --- converter
  private ObjectProperty<StringConverter<T>> converter = new SimpleObjectProperty<StringConverter<T>>(
      this, "converter");

  /**
   * The {@link StringConverter} property.
   */
  public final ObjectProperty<StringConverter<T>> converterProperty() {
    return converter;
  }

  /**
   * Sets the {@link StringConverter} to be used in this cell.
   */
  public final void setConverter(StringConverter<T> value) {
    converterProperty().set(value);
  }

  /**
   * Returns the {@link StringConverter} used in this cell.
   */
  public final StringConverter<T> getConverter() {
    return converterProperty().get();
  }

  /***************************************************************************
   * * Public API * *
   **************************************************************************/

  /** {@inheritDoc} */
  @Override
  public void startEdit() {
    if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
      return;
    }
    super.startEdit();
    CellUtils.startEdit(this, spinner, getConverter());
  }

  /** {@inheritDoc} */
  @Override
  public void cancelEdit() {
    super.cancelEdit();
    CellUtils.cancelEdit(this, getConverter());
  }

  /** {@inheritDoc} */
  @Override
  public void updateItem(T item, boolean empty) {
    super.updateItem(item, empty);
    CellUtils.updateItem(this, spinner, getConverter());
  }
}