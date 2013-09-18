package main.list.structure;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Created by martin on 6.8.13. */
public class StructureWrapper implements Serializable {

  private static final long serialVersionUID = -7161696989395057874L;

  protected ObservableList<WeekListItem> weeks;

  public StructureWrapper() {
    this(null);
  }

  public StructureWrapper(ArrayList<WeekListItem> weeks) {
    this.weeks = FXCollections.observableList((weeks != null) ? weeks
        : new ArrayList<WeekListItem>());
  }

  public ObservableList<WeekListItem> getWeeks() {
    return weeks;
  }

  public void setWeeks(ObservableList<WeekListItem> weeks) {
    this.weeks = weeks;
  }

  public boolean addWeek(WeekListItem item) {
    return weeks.add(item);
  }

  public boolean removeWeek(WeekListItem item) {
    return weeks.remove(item);
  }

  public WeekListItem removeWeek(int index) {
    return weeks.remove(index);
  }

}
