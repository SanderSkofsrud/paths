package edu.ntnu.idatt2001.paths.controllers;

import edu.ntnu.idatt2001.paths.models.player.Difficulty;
import edu.ntnu.idatt2001.paths.models.player.Player;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Player controller.
 * This class is responsible for creating the player.
 *
 * @author Helle R. and Sander S.
 * @version 1.3 22.05.2023
 */
public class PlayerController {
  /**
   * The constant instance of the class.
   * This is a singleton class, and can be accessed from anywhere in the program.
   */
  private static PlayerController instance;
  /**
   * The Player.
   * The active player.
   */
  private Player player;
  /**
   * The Inventory of the player.
   * The inventory is used to store the items the player has.
   */
  private List<String> inventory = new ArrayList<>();
  /**
   * The active character.
   * The active character model.
   */
  private String activeCharacter;

  /**
   * Instantiates a new Player controller.
   */
  private PlayerController() {}

  /**
   * Returns the instance of the class.
   *
   * @return the instance of the class
   */
  public static PlayerController getInstance() {
    if (instance == null) {
      instance = new PlayerController();
    }
    return instance;
  }

  /**
   * Adds a default player.
   * The player stats are based on the difficulty.
   *
   * @param name       the name of the player
   * @param difficulty the difficulty of the game
   * @return the player that is created
   */
  public Player addDefaultPlayer(String name, Difficulty difficulty, String activeCharacter) {
    inventory.addAll(difficulty.getStartingInventory());
    player = new Player.PlayerBuilder(name)
            .health(difficulty.getHealth())
            .gold(difficulty.getGold())
            .inventory(inventory)
            .characterModel(activeCharacter)
            .build();
    return player;
  }

  /**
   * Adds a custom player.
   * The player stats are based on the parameters.
   *
   * @param name   the name of the player
   * @param health the health of the player
   * @param gold   the gold of the player
   * @return the player that is created
   */
  public Player addCustomPlayer(String name, int health, int gold, String activeCharacter) {
    player = new Player.PlayerBuilder(name)
            .health(health)
            .gold(gold)
            .inventory(inventory)
            .characterModel(activeCharacter)
            .build();
    return player;
  }

  /**
   * Returns the player.
   *
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Sets the player.
   *
   * @param player the player
   */
  public void setPlayer(Player player) {
    this.player = player;
  }

  /**
   * Resets the player.
   */
  public void resetPlayer() {
    player = null;
    inventory.clear();
  }

  /**
   * Returns the active character model.
   *
   * @return the active character model
   */
  public String getActiveCharacter() {
    return activeCharacter;
  }

  /**
   * Sets the active character model.
   *
   * @param activeCharacter the active character model
   */
  public void setActiveCharacter(String activeCharacter) {
    this.activeCharacter = activeCharacter;
  }
}
