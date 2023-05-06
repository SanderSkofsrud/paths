package edu.ntnu.idatt2001.paths.utility.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.idatt2001.paths.models.Game;
import edu.ntnu.idatt2001.paths.models.Story;
import edu.ntnu.idatt2001.paths.models.actions.Action;
import edu.ntnu.idatt2001.paths.models.goals.Goal;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JsonReader {
  public static Game loadGameJSON(String fileName) throws FileNotFoundException {
    try (Reader reader = new FileReader(fileName)) {
      Gson gson = new GsonBuilder()
              .registerTypeAdapter(Story.class, new StoryDeserializer())
              .registerTypeAdapter(Goal.class, new GoalDeserializer())
              .registerTypeAdapter(Action.class, new ActionDeserializer())
              .create();

      return gson.fromJson(reader, Game.class);
    } catch (IOException e) {
      throw new FileNotFoundException("File not found or could not be read: " + fileName);
    }
  }
}
