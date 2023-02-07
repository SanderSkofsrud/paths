package edu.ntnu.idatt2001.actions;

import edu.ntnu.idatt2001.Player;

public class InventoryAction implements Action {
  private final String item;

  public InventoryAction(String item) {this.item = item;}

  @Override
  public void execute(Player player) { player.addItem(item);}
}