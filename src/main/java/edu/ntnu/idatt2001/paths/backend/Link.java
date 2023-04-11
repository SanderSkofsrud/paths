package edu.ntnu.idatt2001.paths.backend;

import edu.ntnu.idatt2001.paths.backend.actions.Action;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class that represents a link. The link makes it possible to navigate from one passage to another.
 * The links connect the different parts of the story.
 * A link has a text, a reference to the passage that the link will lead to and a list of actions
 */

public class Link {
  private final String text;
  private final String reference;
  private List<Action> actions;

  /**
   * Constructor for Link.
   *
   * @param text The text that will be displayed to the user.
   * @param reference The reference to the passage that the link will lead to.
   * @throws NullPointerException if text or reference is null.
   */

  public Link(String text, String reference) throws NullPointerException {
    if (text == null || text.isBlank()) {
      throw new NullPointerException("The text can´t be empty");
    }
    if (reference == null || reference.isBlank()) {
      throw new NullPointerException("The reference can´t be empty");
    }
    Objects.requireNonNull(text, "The text can´t be null");
    Objects.requireNonNull(reference, "The reference can´t be null");

    this.text = text;
    this.reference = reference;
    this.actions = new ArrayList<>();
  }

  /**
   * Copy constructor for Link.
   *
   * @param link The link to be copied.
   * @throws NullPointerException if link is null.
   */
  public Link(Link link) {
    this.text = link.text;
    this.reference = link.reference;
    this.actions = link.actions;
  }

  /**
   * Returns the text of the link.
   *
   * @return The text of the link.
   */

  public String getText() {
    return text;
  }

  /**
   * Returns the reference of the link.
   *
   * @return The reference of the link.
   */

  public String getReference() {
    return reference;
  }

  /**
   * Adds an action to the link.
   *
   * @param action The action to be added.
   * @throws IllegalArgumentException if action is null.
   */

  public void addAction(Action action) throws IllegalArgumentException {
    if (action == null) {
      throw new IllegalArgumentException("Link Action can not be null!");
    }
    actions.add(action);
  }

  /**
   * Returns the actions of the link.
   *
   * @return The actions of the link.
   */

  public List<Action> getActions() {
    return actions;
  }

  /**
   * Returns a string representation of the link.
   *
   * @return A string representation of the link.
   */

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (!actions.isEmpty()) {
      sb.append("[");
      for (Action action : actions) {
        sb.append(action.toString());
      }
      sb.append("]");
    }
    return "["
        + getText()
        + "]("
        + getReference()
        + ")"
        + sb
        + "\n";

  }

  /**
   * Returns true if the link is equal to the object passed as parameter.
   *
   * @param o The object to be compared with the link.
   * @return True if the link is equal to the object passed as parameter.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Link link = (Link) o;
    return reference != null ? reference.equals(link.reference) : link.reference == null;
  }

  /**
   * Returns the hashcode of the link.
   *
   * @return The hashcode of the link.
   */
  @Override
  public int hashCode() {
    return reference != null ? reference.hashCode() : 0;
  }

}