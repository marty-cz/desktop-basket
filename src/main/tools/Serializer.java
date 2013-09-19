package main.tools;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/** Created by martin on 6.8.13. */
public class Serializer {

  private static Serializer instance;
  protected String errString = "";

  private Serializer() {
  }

  public static Serializer getInstance() {
    if (instance == null) {
      instance = new Serializer();
    }
    return instance;
  }

  public String getErrString() {
    return errString;
  }

  public boolean serializeToJson(String filePath, Object obj, Class<?> clazz) {
    JsonWriter writer = null;
    try {
      FileOutputStream stream = new FileOutputStream(filePath);
      writer = new JsonWriter(new OutputStreamWriter(stream, "UTF-8"));
      writer.setIndent("  ");
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(obj, clazz, writer);
      writer.close();
      return true;
    } catch (Exception e) {
      errString = e.getLocalizedMessage();
    }
    return false;
  }

  public Object deserializeFromJson(String filePath, Class<?> clazz) {
    JsonReader reader = null;
    try {
      GsonBuilder gsonB = new GsonBuilder();
      gsonB.registerTypeAdapter(ObservableList.class, new ObservableListInstanceCreator());
//      reader = new JsonReader(new FileReader(filePath));
      reader = new JsonReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
      return gsonB.create().fromJson(reader, clazz);
    } catch (Exception e) {
      errString = e.getLocalizedMessage();
    }
    return null;
  }

  private static class ObservableListInstanceCreator implements InstanceCreator<ObservableList<?>> {
    public ObservableList<?> createInstance(Type type) {
      // No need to use a parameterized list since the actual instance
      // will have the raw type anyway.
      return FXCollections.observableArrayList();
    }
  }

}
