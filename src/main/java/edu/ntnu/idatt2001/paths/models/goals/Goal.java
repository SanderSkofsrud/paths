package edu.ntnu.idatt2001.paths.models.goals;

import edu.ntnu.idatt2001.paths.models.player.Player;

/**
 * The interface Goal.
 * A goal will be fulfilled when a player has reached a certain attribute.
 * Goal checks if the player has reached a goal.
 *
 * @author Helle R. and Sander S.
 * @version 1.0 - 11.04.2023
 */
public interface Goal {

  /**
   * Checks if a goal is fulfilled.
   *
   * @param player the player
   * @return the boolean - true if the goal is fulfilled, false if not
   */
  boolean isFulfilled(Player player);

  /**
   * Returns a string representation of the Goal object.
   * The string representation is formatted as follows:
   * (GoalType)(GoalValue)
   *
   * @return a string representation of the Goal object.
   */
  @Override
  String toString();
}
