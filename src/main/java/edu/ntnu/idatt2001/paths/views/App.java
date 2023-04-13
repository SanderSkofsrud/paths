package edu.ntnu.idatt2001.paths.views;

import edu.ntnu.idatt2001.paths.controllers.ScreenController;
import edu.ntnu.idatt2001.paths.models.*;
import edu.ntnu.idatt2001.paths.models.actions.GoldAction;
import edu.ntnu.idatt2001.paths.models.goals.Goal;
import edu.ntnu.idatt2001.paths.models.goals.GoldGoal;
import edu.ntnu.idatt2001.paths.utility.FileHandler;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type App.
 */
public class App extends Application {

  private BorderPane frontPage = new BorderPane();
  private Scene frontPageScene = new Scene(frontPage);
  /**
   * The Screen controller.
   */
  protected ScreenController screenController = new ScreenController(frontPageScene);
  private NewGameView newGameView = new NewGameView(screenController);
  private ChooseGoalsView ChooseGoalsView = new ChooseGoalsView(screenController);
  /**
   * The constant primaryStage.
   */
  public static Stage primaryStage;

  @Override
  public void start(Stage primaryStage) {
    App.primaryStage = primaryStage;

    screenController.addScreen("NewGame", newGameView);
    screenController.addScreen("ChooseGoals", ChooseGoalsView);

    primaryStage.setTitle("Paths");
    primaryStage.setWidth(500);
    primaryStage.setHeight(500);
    primaryStage.setMaximized(true);

    primaryStage.setScene(frontPageScene);
    primaryStage.show();

    this.setup();
  }

  private void setup() {
    ImageView logo = new ImageView(new Image("logo.png"));
    logo.preserveRatioProperty().set(true);
    logo.setFitWidth(500);
    ImageView tagline = new ImageView(new Image("tagline.png"));
    tagline.preserveRatioProperty().set(true);
    tagline.setFitWidth(300);
    VBox vBox = new VBox();
    vBox.getChildren().addAll(logo, tagline);
    vBox.setAlignment(Pos.CENTER);
    vBox.setSpacing(25);
    frontPage.setCenter(vBox);

    Button newGame = new Button("New Game");
    Button loadGame = new Button("Load Game");
    newGame.setId("mainMenuButton");
    loadGame.setId("mainMenuButton");

    newGame.setOnAction(e -> screenController.activate("NewGame"));

    loadGame.setOnAction(e -> {
      // TODO screenController.activate("LoadGame");

      Passage openingpassage = new Passage("Beginnings", "You are in a small room");
      Story story = new Story("Haunted House", openingpassage);
      Link link = new Link("Try open the door", "Another room");
      openingpassage.addLink(link);
      Passage passage = new Passage("Another room", "The door opens to another room");
      Link link1 = new Link("Open the book", "The book of spells");
      Link link2 = new Link("Go back", "Beginnings");
      passage.addLink(link1);
      passage.addLink(link2);
      link1.addAction(new GoldAction(10));
      story.addPassage(passage);
      Player player = new Player.Builder("testPlayer").build();
      List<Goal> goals = new ArrayList<>();
      GoldGoal goldGoal = new GoldGoal(10);
      goals.add(goldGoal);
      Game game = new Game(player, story, goals);
      FileHandler.saveGame(game.getStory(), "src/main/resources/paths/story.paths");
      Story story1;
      try {
        story1 = FileHandler.loadGame("src/main/resources/paths/story.paths");
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
      System.out.println(story1.getPassages());
      Game game1 = new Game(player, story1, goals);
      FileHandler.saveGame(game1.getStory(), "src/main/resources/paths/story1.paths");
    });

    HBox hBox = new HBox();
    hBox.getChildren().addAll(newGame, loadGame);
    hBox.setAlignment(Pos.CENTER);
    hBox.getStylesheets().add("stylesheet.css");
    hBox.setSpacing(20);
    hBox.setPadding(new Insets(0, 0, 150, 0));
    frontPage.setBottom(hBox);

    Image background = new Image("background.png");
    BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, true));
    frontPage.setBackground(new Background(backgroundImage));
  }

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}