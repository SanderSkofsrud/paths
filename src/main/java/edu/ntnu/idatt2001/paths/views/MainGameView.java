package edu.ntnu.idatt2001.paths.views;

import edu.ntnu.idatt2001.paths.controllers.FileHandlerController;
import edu.ntnu.idatt2001.paths.controllers.GameController;
import edu.ntnu.idatt2001.paths.controllers.PlayerController;
import edu.ntnu.idatt2001.paths.controllers.ScreenController;
import edu.ntnu.idatt2001.paths.models.*;
import edu.ntnu.idatt2001.paths.models.actions.Action;
import edu.ntnu.idatt2001.paths.models.goals.*;
import edu.ntnu.idatt2001.paths.models.player.Player;
import edu.ntnu.idatt2001.paths.utility.SoundPlayer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The type Main game view.
 * The class is used to create the GUI of the game and to start the game.
 *
 * @author Helle R. and Sander S.
 * @version 0.1 08.05.2023
 */
public class MainGameView extends View{
  /**
   * The Border pane.
   * The borderPane is the main pane of the GUI.
   */
  protected BorderPane borderPane;
  /**
   * The Stack pane.
   * The stackPane is used to stack the different panes on top of each other.
   */
  protected StackPane stackPane;
  /**
   * The Screen controller.
   * The screenController is used to switch between the different views of the GUI.
   */
  private ScreenController screenController;
  /**
   * The Game controller.
   * The gameController is used to control the game.
   */
  private GameController gameController = GameController.getInstance();
  /**
   * The Game.
   * The game is the game that is being played.
   */
  private Game game;
  /**
   * The Current passage.
   * The currentPassage is the passage that the player is currently in.
   */
  private Passage currentPassage;
  /**
   * The player.
   * The player is the player that is playing the game.
   */
  private Player player;
  /**
   * The Goals.
   * The goals are the goals of the game.
   */
  private List<Goal> goals;
  /**
   * The Passage content.
   * The passageContent is the text of the passage that the player is currently in.
   */
  private TextArea passageContent;
  /**
   * The Player health label.
   * The playerHealthLabel is the label that shows the health of the player.
   */
  private Label playerHealthLabel = new Label();
  /**
   * The Player score label.
   * The playerScoreLabel is the label that shows the score of the player.
   */
  private Label playerScoreLabel = new Label();
  /**
   * The Player inventory label.
   * The playerInventoryLabel is the label that shows the inventory of the player.
   */
  private Label playerInventoryLabel = new Label();
  /**
   * The Player gold label.
   * The playerGoldLabel is the label that shows the gold of the player.
   */
  private Label playerGoldLabel = new Label();
  /**
   * The buttons box.
   * The buttonsBox is the box that contains the buttons of the GUI.
   */
  private HBox buttonsBox;
  /**
   * The Attributes box.
   * The attributesBox is the box that contains the attributes of the player.
   */
  private HBox attributesBox = new HBox();
  /**
   * The Inventory image box.
   * The inventoryImageBox is the box that contains the images of the inventory of the player.
   */
  private HBox inventoryImageBox = new HBox();
  /**
   * The Inventory box.
   * The inventoryBox is the box that contains the inventory of the player.
   */
  private HBox inventoryBox = new HBox();
  /**
   * The array words.
   * The words array is the array that contains the words of the passage that the player is currently in.
   * The words are stored in an array so that they can be displayed in a text area.
   */
  private String[] words;
  /**
   * The timeline.
   * The timeline is the timeline of the animation.
   * The timeline is used to animate the text of the passage.
   */
  private Timeline timeline;
  /**
   * The Story.
   * The story is the story that the player is playing.
   */
  private Story story;
  /**
   * The File handler controller.
   */
  FileHandlerController fileHandlerController = FileHandlerController.getInstance();

  /**
   * The Player controller.
   */
  PlayerController playerController = PlayerController.getInstance();

  /**
   * Instantiates a new Main game view.
   *
   * @param screenController the screen controller
   */
  public MainGameView(ScreenController screenController) {
    borderPane = new BorderPane();
    stackPane = new StackPane();
    borderPane.setCenter(stackPane);
    this.screenController = screenController;
  }

  /**
   * Returns the pane of the GUI.
   * @return the pane of the GUI.
   */
  @Override
  public Pane getPane() {
    return this.borderPane;
  }

  /**
   * Sets up the top bar of the GUI.
   */
  private void setupButtonsBox() {
    buttonsBox = new HBox();
    borderPane.setBottom(buttonsBox);
    buttonsBox.setAlignment(Pos.BOTTOM_CENTER);
  }

  /**
   * Sets up the attribute box.
   */
  private void setupAttributesBox() {
    attributesBox = new HBox();
    attributesBox.setAlignment(Pos.TOP_LEFT);
    attributesBox.setPadding(new Insets(10, 10, 10, 10));
    //borderPane.setTop(attributesBox);
  }

  /**
   * Sets up the main game view.
   * The method is used to set up the GUI of the game.
   * The method sets up the buttons, the attributes of the player,
   * the inventory of the player and the text of the passage.
   * The method also sets up the animation of the text of the passage.
   * The method also sets up the background of the GUI.
   * The method also sets up the music of the GUI.
   */
  @Override
  public void setup() {
    SoundPlayer soundPlayer = new SoundPlayer();
    soundPlayer.playOnLoop("/sounds/ambiance.wav");
    game = gameController.getGame();
    player = game.getPlayer();
    goals = game.getGoals();
    story = game.getStory();

    setupButtonsBox();
    setupTopBar();
    setupAttributesBox();

    passageContent = new TextArea();
    passageContent.setWrapText(true);
    passageContent.setEditable(false);

    TextFlow textFlow = new TextFlow(passageContent);

    ScrollPane scrollPane = new ScrollPane(textFlow);
    scrollPane.setFitToWidth(true);
    scrollPane.setMaxWidth(500);
    scrollPane.setMaxHeight(197);
    scrollPane.setPrefViewportHeight(200);

    scrollPane.setStyle("-fx-background-color: transparent;");
    passageContent.setStyle("-fx-background-color: transparent;");
    textFlow.setStyle("-fx-background-color: transparent;");

    textFlow.setOnMouseClicked(event -> {
      Pair<Timeline, Passage> timelineAndPassage = (Pair<Timeline, Passage>) textFlow.getUserData();
      timeline = timelineAndPassage.getKey();
      Passage passage = timelineAndPassage.getValue();
      if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
        timeline.stop();
        String[] displayedWords = passageContent.getText().split("\\s+");
        String[] allWords = passage.getContent().split("\\s+");
        String remainingText = String.join(" ", Arrays.copyOfRange(allWords, displayedWords.length, allWords.length));
        passageContent.appendText(remainingText);
      }
    });

    VBox scrollPaneAndButtonsBox = new VBox(scrollPane, buttonsBox);
    scrollPaneAndButtonsBox.setAlignment(Pos.BOTTOM_CENTER);
    borderPane.setBottom(scrollPaneAndButtonsBox);

    VBox.setVgrow(scrollPane, Priority.ALWAYS);
    stackPane.getChildren().add(scrollPane);
    stackPane.setStyle("-fx-background-color: transparent;");
    stackPane.setAlignment(scrollPane, Pos.CENTER);

    borderPane.setCenter(stackPane);

    HBox buttonsBox = new HBox();
    borderPane.setBottom(buttonsBox);

    textFlow.setUserData(updateUIWithPassage(textFlow, game.begin()));

    Image background = new Image(getClass().getResourceAsStream("/images/gameBackground.png"));
    BackgroundImage backgroundImage = new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, true));
    borderPane.setBackground(new Background(backgroundImage));
    borderPane.getStylesheets().add("stylesheet.css");
  }

  /**
   * Resets the pane of the GUI.
   */
  @Override
  void resetPane() {
    stackPane.getChildren().clear();
    borderPane.setTop(null);
    borderPane.setBottom(null);
    borderPane.setCenter(null);
    inventoryBox.getChildren().clear();
    inventoryImageBox.getChildren().clear();
    words = null;
    timeline.stop();
  }

  /**
   * Updates ut the GUI with the passage.
   * The method is used to update the GUI with the passage.
   * The method updates the text of the passage and the buttons of the passage.
   * The method also updates the attributes of the player.
   *
   * @param textFlow the text flow
   * @param passage  the passage
   * @return the pair of the timeline and the passage
   */
  private Pair<Timeline, Passage> updateUIWithPassage(TextFlow textFlow, Passage passage) {
    passageContent.clear();
    updatePlayerAttributes();

    words = passage.getContent().split("\\s+");

    timeline = new Timeline();
    timeline.getKeyFrames().clear();
    for (int i = 0; i < words.length; i++) {
      int wordIndex = i;
      timeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * 100), event -> {
        passageContent.appendText(words[wordIndex] + " ");
      }));
    }
    timeline.play();

    HBox buttonsBox = (HBox) borderPane.getBottom();
    buttonsBox.getChildren().clear();
    buttonsBox.setAlignment(Pos.CENTER);
    buttonsBox.setPadding(new Insets(10, 0, 100, 0));
    buttonsBox.setSpacing(10);

    for (Link link : passage.getLinks()) {
      Button button = new Button(link.getText());
      button.setOnAction(event -> {
        for (Action action : link.getActions()) {
          System.out.println(action);
          action.execute(player);
        }
        Passage nextPassage = game.go(link);
        timeline.stop();
        updateUIWithPassage(textFlow, nextPassage);

        if (!MinigameView.hasPlayed()) {
          int random = (int) (Math.random() * 100) + 1;
          if (random <= 10) {
            playerController.setPlayer(player);
            screenController.activate("Minigame");
          }
        }
        updatePlayerInfo();
      });

      buttonsBox.getChildren().add(button);
      button.setId("subMenuButton");
    }

    updatePlayerInfo();
    System.out.println(player);
    textFlow.setUserData(new Pair<>(timeline, passage)); // Store the Pair object in userData
    return new Pair<>(timeline, passage);
  }

  /**
   * Sets up the top bar of the GUI.
   * The top bar contains the stats of the player,
   * as well as buttons to control the game.
   */
  private void setupTopBar() {
    attributesBox.getChildren().clear();

    attributesBox.setAlignment(Pos.TOP_LEFT);
    attributesBox.setPadding(new Insets(10, 10, 10, 10));

    playerHealthLabel.setText("Health: " + player.getHealth());
    playerHealthLabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
    playerHealthLabel.setPadding(new Insets(10, 10, 10, 10));

    playerGoldLabel.setText("Gold: " + player.getGold());
    playerGoldLabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
    playerGoldLabel.setPadding(new Insets(10, 10, 10, 10));

    playerScoreLabel.setText("Score: " + player.getScore());
    playerScoreLabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
    playerScoreLabel.setPadding(new Insets(10, 10, 10, 10));

    inventoryImageBox = new HBox();
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


    playerInventoryLabel.setText("Inventory: ");

    inventoryBox = new HBox(playerInventoryLabel, inventoryImageBox);
    inventoryBox.setPadding(new Insets(10, 10, 0, 10));
    inventoryBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

    Image exitImage = new Image(getClass().getResourceAsStream("/images/exit.png"));
    Image helpImage = new Image(getClass().getResourceAsStream("/images/help.png"));
    Image homeImage = new Image(getClass().getResourceAsStream("/images/home.png"));

    ImageView exitImageView = new ImageView(exitImage);
    exitImageView.setFitWidth(30);
    exitImageView.setFitHeight(30);

    ImageView helpImageView= new ImageView(helpImage);
    helpImageView.setFitWidth(30);
    helpImageView.setFitHeight(30);

    ImageView homeImageView = new ImageView(homeImage);
    homeImageView.setFitWidth(30);
    homeImageView.setFitHeight(30);

    Button exitButton = new Button();
    exitButton.setGraphic(exitImageView);
    exitButton.setStyle("-fx-background-color: transparent;");

    exitButton.setOnAction(event -> {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Exit");
      alert.setHeaderText("Choose an option:");
      alert.setContentText("You will lose all progress if you exit without saving.");

      ButtonType cancel = new ButtonType("Cancel");
      ButtonType saveAndExit = new ButtonType("Save & Exit");
      ButtonType exitWithoutSaving = new ButtonType("Exit");

      alert.getButtonTypes().setAll(saveAndExit,exitWithoutSaving, cancel);

      DialogPane dialogPane = alert.getDialogPane();
      dialogPane.getStylesheets().add("stylesheet.css");

      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent()) {
        if (result.get() == cancel) {
          alert.close();
        } else if (result.get() == saveAndExit) {
          FileHandlerController.getInstance().saveGame(player.getName(), story);
          FileHandlerController.getInstance().saveGameJson(player.getName(),game);
          Platform.exit();
        } else if (result.get() == exitWithoutSaving) {
          Platform.exit();
        }
      }
      borderPane.getStylesheets().add("stylesheet.css");
    });


    Button questionButton = new Button();
    questionButton.setGraphic(helpImageView);
    questionButton.setStyle("-fx-background-color: transparent;");
    questionButton.setOnAction(actionEvent -> {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Game help");
      alert.setHeaderText("Game help");
      alert.setContentText("""
          The top left bar shows your current health, gold, score and inventory
          
          *The goals progress bar shows your current progress in the game according to your goals
          
          *The information bar describes the current passage you are in
          
          *To play the game you must choose one of the options that appear in the bottom of the screen
          
          *You can exit the game at any time by clicking the exit button
          
          *You can return to home by clicking on the home button
          """);

      DialogPane dialogPane = alert.getDialogPane();
      dialogPane.getStylesheets().add("stylesheet.css");

      alert.showAndWait();

        });

    Button homeButton = new Button();
    homeButton.setGraphic(homeImageView);
    homeButton.setStyle("-fx-background-color: transparent;");
    homeButton.setOnAction(event -> {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Home");
      alert.setHeaderText("Choose an option:");
      alert.setContentText("You will lose all progress if you return \nto home without saving.");

      ButtonType cancel = new ButtonType("Cancel");
      ButtonType saveAndGoHome = new ButtonType("Save & home");
      ButtonType goHomeWithoutSaving = new ButtonType("Home");

      alert.getButtonTypes().setAll(saveAndGoHome,goHomeWithoutSaving, cancel);

      DialogPane dialogPane = alert.getDialogPane();
      dialogPane.getStylesheets().add("stylesheet.css");

      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent()) {
        if (result.get() == cancel) {
          alert.close();
        } else if (result.get() == saveAndGoHome) {
          FileHandlerController.getInstance().saveGame(player.getName(), story);
          FileHandlerController.getInstance().saveGameJson(player.getName(),game);
          gameController.resetGame(); // Add this line
          resetPane();
          screenController.activate("MainMenu");
        } else if (result.get() == goHomeWithoutSaving) {
          gameController.resetGame(); // Add this line
          resetPane();
          screenController.activate("MainMenu");
        }
      }
      stackPane.getStylesheets().add("stylesheet.css");
    });

    HBox topRightBox = new HBox();
    topRightBox.setAlignment(Pos.TOP_RIGHT);
    topRightBox.setPadding(new Insets(10, 10, 10, 10));
    topRightBox.getChildren().addAll(homeButton, questionButton, exitButton);

    VBox goalsVbox = goalsVbox();

    attributesBox.getChildren().addAll(playerHealthLabel, playerGoldLabel, playerScoreLabel, inventoryBox, goalsVbox);

    HBox topBox = new HBox();
    topBox.getChildren().addAll(attributesBox, topRightBox);
    topBox.setHgrow(attributesBox, Priority.ALWAYS);

    borderPane.setTop(topBox);
  }

  /**
   * Updates the player attributes.
   */
  private void updatePlayerAttributes() {
    playerHealthLabel = new Label("Health: " + player.getHealth());
    playerGoldLabel = new Label("Gold: " + player.getGold());
    playerScoreLabel = new Label("Score: " + player.getScore());
    playerInventoryLabel = new Label("Inventory: " + player.getInventory());
  }

  /**
   * Creates the goals VBox.
   *
   * @return the goals VBox
   */
  private VBox goalsVbox() {
    VBox goalsVbox = new VBox();
    goalsVbox.setPadding(new Insets(10, 10, 10, 10));
    goalsVbox.setSpacing(10);
    goalsVbox.setAlignment(Pos.TOP_CENTER);

    Label goalsLabel = new Label("Goals");
    goalsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

    goalsVbox.getChildren().add(goalsLabel);

    for (Goal goal : game.getGoals()) {
      HBox goalHbox = new HBox();
      Label goalLabel = new Label(goal.toString());
      goalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
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

  /**
   * Updates the player info.
   */
  private void updatePlayerInfo() {
    updatePlayerAttributes();
    VBox goalsVbox = goalsVbox();
    attributesBox.getChildren().setAll(playerHealthLabel, playerGoldLabel, playerScoreLabel, playerInventoryLabel, goalsVbox);
  }

}
