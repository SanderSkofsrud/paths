package edu.ntnu.idatt2001.paths.models.goals;

import edu.ntnu.idatt2001.paths.models.Player;

/**
 * Gold goal.
 * A goal that is fulfilled when the player has a certain amount of gold.
 *
 * @author Helle R. & Sander S.
 * @version 0.1 - 11.04.2023
 */
public class GoldGoal implements Goal {
  private final int minimumGold;

  /**
   * Instantiates a new Gold goal.
   * Creates a new goal for a certain amount of gold.
   *
   * @param minimumGold the minimum gold for the goal to be fulfilled
   * @throws IllegalArgumentException if the minimum gold is negative
   */
  public GoldGoal(int minimumGold) {
    if (minimumGold < 0) {
      throw new IllegalArgumentException("Minimum gold cannot be negative");
    }
    this.minimumGold = minimumGold;
  }

  /**
   * Checks if a goal is fulfilled.
   * Calls the player's getGold method to check if the player has more or equal to the minimum gold.
   *
   * @param player the player
   * @return the boolean - true if the goal is fulfilled, false if not
   */
  @Override
  public boolean isFulfilled(Player player) {
    return player.getGold() >= minimumGold;
  }
}