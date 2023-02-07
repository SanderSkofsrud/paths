package edu.ntnu.idatt2001;

import java.util.List;

public class Game {
  private Player player;
  private Story story;
  private List<Goal> goals;

  public Game(Player player, Story story, List<Goal> goals) {
    this.player = player;
    this.story = story;
    this.goals = goals;
  }

  public Game(Game game) {
    this.player = game.player;
    this.story = game.story;
    this.goals = game.goals;
  }

  public Player getPlayer() {
    return player;
  }

  public Story getStory() {
    return story;
  }

  public List<Goal> getGoals() {
    return goals;
  }

  public Passage begin(){
    return story.getOpeningPassage();
  }

  public Passage go(Link link){
    return story.getPassage(link);
  }
}
