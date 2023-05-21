package edu.ntnu.idatt2001.paths.controllers;

import edu.ntnu.idatt2001.paths.Main;
import edu.ntnu.idatt2001.paths.models.Game;
import edu.ntnu.idatt2001.paths.models.Link;
import edu.ntnu.idatt2001.paths.models.Passage;
import edu.ntnu.idatt2001.paths.models.Story;
import edu.ntnu.idatt2001.paths.models.actions.Action;
import edu.ntnu.idatt2001.paths.models.goals.*;
import edu.ntnu.idatt2001.paths.models.player.Player;
import edu.ntnu.idatt2001.paths.utility.Dictionary;
import edu.ntnu.idatt2001.paths.views.MainGameView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MainGameController {
  Game game;
  Player player;
  List<Goal> goals;
  Story story;
  Passage previousPassage;
  Passage currentPassage;
  int playerStartHealth;
  GameController gameController = GameController.getInstance();
  PlayerController playerController = PlayerController.getInstance();
  LanguageController languageController = LanguageController.getInstance();
  private static MainGameController instance;
  MainGameView mainGameView;
  private MainGameController(MainGameView mainGameView) {
    this.mainGameView = mainGameView;
  }
  public static MainGameController getInstance(MainGameView mainGameView) {
    if (instance == null) {
      instance = new MainGameController(mainGameView);
    }
    return instance;
  }

  public void setup() {
    game = gameController.getGame();
    player = game.getPlayer();
    goals = game.getGoals();
    story = game.getStory();
    currentPassage = game.getCurrentPassage();
    playerStartHealth = player.getHealth();
  }

  public static EventHandler<MouseEvent> createMouseClickedEventHandler(TextFlow textFlow, TextArea passageContent) {
    return event -> handleMouseClickedEvent(textFlow, passageContent);
  }

  private static void handleMouseClickedEvent(TextFlow textFlow, TextArea passageContent) {
    Pair<Timeline, Passage> timelineAndPassage = (Pair<Timeline, Passage>) textFlow.getUserData();
    Timeline timeline = timelineAndPassage.getKey();
    Passage passage = timelineAndPassage.getValue();

    if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
      timeline.stop();
      String remainingText = passage.getContent().substring(passageContent.getLength());
      passageContent.appendText(remainingText);
    } else {
      char[] characters = passage.getContent().toCharArray();
      int currentLength = passageContent.getLength();

      timeline = new Timeline();
      timeline.getKeyFrames().clear();
      for (int i = currentLength; i < characters.length; i++) {
        int charIndex = i;
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis((charIndex - currentLength) * 25), event -> {
          passageContent.appendText(Character.toString(characters[charIndex]));
        }));
      }
      timeline.play();
    }
  }

  public void executeActions(Link link) {
    for (Action action : link.getActions()) {
      action.execute(player);
    }
  }

  public boolean minigameCheck() {
    int random = (int) (Math.random() * 100) + 1;
    if (random <= 10) {
      playerController.setPlayer(player);
      return true;
    } else {
      return false;
    }
  }

  public HBox createInventoryBox() {
    HBox inventoryImageBox = new HBox();
    inventoryImageBox.setSpacing(7);
    for (String item : player.getInventory()) {
      String resourcePath = "/images/" + item + ".png";
      InputStream imageStream = getClass().getResourceAsStream(resourcePath);

      if (imageStream != null) {
        ImageView itemImageView = new ImageView(new Image(imageStream));
        itemImageView.setFitWidth(20);
        itemImageView.setFitHeight(20);
        inventoryImageBox.getChildren().add(itemImageView);
      } else {
        inventoryImageBox.getChildren().add(new Label(item));
      }
    }
    return inventoryImageBox;
  }

  public Button createExitButton() {
    Button exitButton = new Button();
    exitButton.setOnAction(event -> {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle(languageController.getTranslation(Dictionary.EXIT.getKey()));
      alert.setHeaderText(languageController.getTranslation(Dictionary.CHOOSE_AN_OPTION.getKey()));
      alert.setContentText(languageController.getTranslation(Dictionary.LOSE_PROGRESS.getKey()));

      ButtonType cancel = new ButtonType(languageController.getTranslation(Dictionary.CANCEL.getKey()));
      ButtonType saveAndExit = new ButtonType(languageController.getTranslation(Dictionary.SAVE_EXIT.getKey()));
      ButtonType exitWithoutSaving = new ButtonType(languageController.getTranslation(Dictionary.EXIT.getKey()));

      alert.getButtonTypes().setAll(saveAndExit,exitWithoutSaving, cancel);

      DialogPane dialogPane = alert.getDialogPane();
      dialogPane.getStylesheets().add("stylesheet.css");

      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent()) {
        if (result.get() == cancel) {
          alert.close();
        } else if (result.get() == saveAndExit) {
          FileHandlerController.getInstance().saveGameJson(player.getName(), null, game);
          Platform.exit();
        } else if (result.get() == exitWithoutSaving) {
          Platform.exit();
        }
      }
    });
    return exitButton;
  }

  public Button createHelpButton() {
    Button questionButton = new Button();
    questionButton.setOnAction(event -> {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle(languageController.getTranslation(Dictionary.GAME_HELP.getKey()));
      alert.setHeaderText(languageController.getTranslation(Dictionary.GAME_HELP.getKey()));
      alert.setContentText(languageController.getTranslation(Dictionary.GAME_HELP_TEXT.getKey()));

      DialogPane dialogPane = alert.getDialogPane();
      dialogPane.getStylesheets().add("stylesheet.css");

      alert.showAndWait();
    });
    return questionButton;
  }

  public Button createHomeButton(ScreenController screenController) {
    Button homeButton = new Button();
    homeButton.setOnAction(event -> {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle(languageController.getTranslation(Dictionary.HOME.getKey()));
      alert.setHeaderText(languageController.getTranslation(Dictionary.CHOOSE_AN_OPTION.getKey()));
      alert.setContentText(languageController.getTranslation(Dictionary.LOSE_PROGRESS_HOME.getKey()));

      ButtonType cancel = new ButtonType(languageController.getTranslation(Dictionary.CANCEL.getKey()));
      ButtonType saveAndGoHome = new ButtonType(languageController.getTranslation(Dictionary.SAVE_HOME.getKey()));
      ButtonType goHomeWithoutSaving = new ButtonType(languageController.getTranslation(Dictionary.HOME.getKey()));

      alert.getButtonTypes().setAll(saveAndGoHome,goHomeWithoutSaving, cancel);

      DialogPane dialogPane = alert.getDialogPane();
      dialogPane.getStylesheets().add("stylesheet.css");

      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent()) {
        if (result.get() == cancel) {
          alert.close();
        } else if (result.get() == saveAndGoHome) {
          FileHandlerController.getInstance().saveGameJson(player.getName(), null, game);
          gameController.resetGame(); // Add this line
          screenController.activate("MainMenu");
          mainGameView.resetPane();
        } else if (result.get() == goHomeWithoutSaving) {
          gameController.resetGame(); // Add this line
          screenController.activate("MainMenu");
          mainGameView.resetPane();
        }
      }
    });
    return homeButton;
  }

  public Button createRestartButton(TextFlow textFlow) {
    Button restartButton = new Button();
    restartButton.setOnAction(event -> {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Restart");
      alert.setHeaderText("Do you want to restart the game?");

      ButtonType cancel = new ButtonType("Cancel");
      ButtonType restart = new ButtonType("Restart");

      alert.getButtonTypes().setAll(restart, cancel);

      DialogPane dialogPane = alert.getDialogPane();
      dialogPane.getStylesheets().add("stylesheet.css");

      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent()) {
        if (result.get() == cancel) {
          alert.close();
        } else if (result.get() == restart) {
          try {
            game = FileHandlerController.getInstance().loadGameJson(player.getName() + ".json", "src/main/resources/initialGame/");
          } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
          }
          player = game.getPlayer();
          mainGameView.setCharacters(null);
          mainGameView.updateUIWithPassage(textFlow,game.begin());
        }
      }
    });
    return restartButton;
  }

  public ProgressBar createHealthBar() {
    ProgressBar healthBar = new ProgressBar();
    healthBar.setMinWidth(100);
    healthBar.setMaxWidth(Double.MAX_VALUE);
    healthBar.setProgress(player.getHealth() / playerStartHealth);
    if (player.getHealth() <= 0) {
      healthBar.setStyle("-fx-accent: red;");
    } else if (player.getHealth() <= playerStartHealth / 4) {
      healthBar.setStyle("-fx-accent: orange;");
    } else {
      healthBar.setStyle("-fx-accent: green;");
    }
    return healthBar;
  }

  public Label createLabel(String text, String type) {
    Label label = new Label();
    label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
    label.setPadding(new Insets(10, 10, 10, 10));
    switch (type.toLowerCase()) {
      case "health" -> label.setText(text + ": " + player.getHealth());
      case "score" -> label.setText(text + ": " + player.getScore());
      case "gold" -> label.setText(text + ": " + player.getGold());
      default -> label.setText(text);
    }
    return label;
  }

  public VBox setupGoals() {
    VBox goalsVbox = new VBox();
    goalsVbox.getChildren().add(new Label(languageController.getTranslation(Dictionary.GOALS_IN_GAME.getKey())));
    for (Goal goal : game.getGoals()) {
      HBox goalHbox = new HBox();
      Label goalLabel = new Label(goal.toString());
      ProgressBar goalProgressBar = new ProgressBar();
      goalProgressBar.setProgress(0);
      double progress = 0.0;
      if (goal.getClass() == ScoreGoal.class) {
        progress = (double) player.getScore() / ((ScoreGoal) goal).getMinimumScore();
      } else if (goal.getClass() == HealthGoal.class) {
        progress = (double) player.getHealth() / ((HealthGoal) goal).getMinimumHealth();
      } else if (goal.getClass() == InventoryGoal.class) {
        progress = (double) player.getInventory().size() / ((InventoryGoal) goal).getMandatoryItems().size();
      } else if (goal.getClass() == GoldGoal.class) {
        progress = (double) player.getGold() / ((GoldGoal) goal).getMinimumGold();
      }
      if (progress != 0.0) {
        goalProgressBar.setProgress(progress);
      } else {
        goalProgressBar.setProgress(0.0);
      }
      goalHbox.getChildren().addAll(goalLabel, goalProgressBar);
      goalsVbox.getChildren().add(goalHbox);
    }
    return goalsVbox;
  }

  public String setupCurrentPassage() {
    return game.getCurrentPassage().getContent();
  }

  public Passage getCurrentPassage() {
    return game.getCurrentPassage();
  }

  public Game getGame() {
    return game;
  }

  public void go(Link link, TextFlow textFlow) {
    Passage nextPassage = game.go(link);
    mainGameView.stopTimeline();
    mainGameView.updateUIWithPassage(textFlow, nextPassage);
    game.setCurrentPassage(nextPassage);
  }
}
