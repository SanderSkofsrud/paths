package edu.ntnu.idatt2001.paths.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

/**
 * Test class for Story
 */

public class StoryTest {
  Story story;
  Passage passage;
  Passage passage2;

  Passage passage3;
  Passage openingPassage;
  Story story2;
  Link link;

  Link link2;

  @BeforeEach
  void setUp() {
    passage = new Passage("passage", "content");
    passage2 = new Passage("passage2", "content2");
    passage3 = new Passage("passage2", "content3");
    openingPassage = new Passage("openingPassage", "content");
    story = new Story("story", openingPassage);
    story2 = new Story("story2",openingPassage);
    link = new Link("link", "passage2");
    link2 = new Link("test", "test");
    passage.addLink(link);
  }

  /**
   * Test class for constructor
   */
  @Nested
  class Constructor {
    /**
     * Test that constructor constructs correctly with valid values and indirectly test that getters work
     */
    @Test
    @DisplayName("Test that constructor constructs correctly")
    void testThatConstructorConstructsCorrectly() {
      assertEquals("story", story.getTitle());
      assertEquals(openingPassage, story.getOpeningPassage());
    }

    /**
     * Test that constructor throws NullPointerExceptions when openingPassage null
     */
    @Test
    @DisplayName("Test that constructor throws NullPointerExceptions when openingPassage is null")
    void testThatConstructorThrowsNullPointerExceptionsWhenOpeningPassageNull() {
      Passage passage = null;
      assertThrows(NullPointerException.class, () ->
          new Story("title", passage));
    }

    /**
     * Test that constructor throws NullPointerException when title is null
     */
    @Test
    @DisplayName("Test that constructor throws NullPointerException when title is null")
    void testThatConstructorThrowsNullPointerExceptionWhenTitleIsNull(){
      assertThrows(NullPointerException.class, () ->
          new Story(null, passage));
    }

    /**
     * Test that constructor throws NullPointerException when title is empty
     */
    @Test
    @DisplayName("Test that constructor throws NullPointerException when title is empty")
    void testThatConstructorThrowsNullPointerExceptionWhenTitleIsEmpty(){
      assertThrows(IllegalArgumentException.class, () ->
          new Story("", passage));
    }
  }

  /**
   * Test class for exception handling
   */
  @Nested
  class addPassage {

    /**
     * Test that addPassage adds passage correctly when valid values
     */
    @Test
    @DisplayName("Test that addPassage adds passage correctly when valid values")
    void testThatAddPassageAddsPassageCorrectlyWhenValidValues(){
      assertEquals(1, story.getPassages().size());
      story.addPassage(passage);
      assertEquals(2, story.getPassages().size());
    }

    /**
     * Test that addPassage throws NullPointerException when passage is null
     */
    @Test
    @DisplayName("Test that addPassage throws IllegalArgumentException when passage is null")
    void testThatAddPassageThrowsNullPointerExceptionWhenPassageIsNull(){
      Passage passage = null;
      assertThrows(NullPointerException.class, () ->
          story2.addPassage(passage));
    }

    /**
     * Test that addPassage throws IllegalArgumentException when passage is already in story
     */
    @Test
    @DisplayName("Test that addPassage throws IllegalArgumentException when passage is already in story")
    void testThatAddPassageThrowsIllegalArgumentExceptionWhenPassageIsAlreadyInStory(){
      story.addPassage(passage);
      assertThrows(IllegalArgumentException.class, () ->
          story.addPassage(passage));
    }
  }

  /**
   * Test class for getPassage
   */
  @Nested
  class getPassage {
    /**
     * Test that getPassage returns passage correctly when valid link
     */
    @Test
    @DisplayName("Test that getPassage returns passage correctly when valid link")
    void testThatGetPassageReturnsPassageCorrectlyWhenValidLink(){
      story.addPassage(passage2);
      assertEquals(passage2, story.getPassage(link));
    }

    /**
     * Test that getPassage throws NullPointerException when link is null
     */
    @Test
    @DisplayName("Test that getPassage throws NullPointerException when link is null")
    void testThatGetPassageThrowsNullPointerException(){
      Link link = null;
      assertThrows(NullPointerException.class, () ->
          story2.getPassage(link));
    }
  }

  /**
   * Test class for removePassage
   */
  @Nested
  class removePassage {
    /**
     * Test that removePassage removes passage from story when passage exists and no other passages have links to it
     */
    @Test
    @DisplayName("Test that removePassage removes passage from story when passage exists and no other passages have links to it")
    void testThatRemovePassageRemovesPassage() {
      story.addPassage(passage2);
      assertTrue(story.getPassages().size() == 2);
      story.removePassage(link);
      assertEquals(1, story.getPassages().size());
    }

    /**
     * Test that removePassage throws IllegalArgumentException if passage does not exist in Story
     */
    @Test
    @DisplayName("Test that removePassage throws IllegalArgumentException if passage does not exist in Story")
    void testThatRemovePassageThrowsIllegalArgumentExceptionIfPassageDocentExist(){
      assertThrows(IllegalArgumentException.class, () ->
          story.removePassage(link));
    }

    /**
     * Test that removePassage throws IllegalArgumentException if other passage has links to the passage
     */
    @Test
    void testThatRemovePassageThrowsIllegalArgumentExceptionIfOtherPassagesHaveLinksToPassage(){
      passage2.addLink(link);
      story.addPassage(passage2);
      passage3.addLink(link);
      story.addPassage(passage3);
      assertThrows(IllegalArgumentException.class, () ->
          story.removePassage(link));
    }
  }

  /**
   * Test class for brokenLinks
   */

  @Nested
  class brokenLinks {


    /**
     * Test that the Arraylist BrokenLinks returns the correct size
     */
    @Test
    @DisplayName("Test that getBrokenLinks returns correctly")
    void testThatGetBrokenLinksReturnsCorrectly() {
      assertEquals(0, story.getBrokenLinks().size());
      story.addPassage(passage);
      assertEquals(1, story.getBrokenLinks().size());
    }
  }

  /**
   * Test class for file handling
   */
  @Nested
  class fileHandling {

    /**
     * Test that toString for Story returns correctly and ensure that the format is correct.
     */
    @Test
    @DisplayName("Test toString")
    void testToString() {
      passage.addLink(link2);
      story.addPassage(passage);
      assertEquals(story.getTitle()
              + "\n"
              + story.getOpeningPassage()
              + passage.toString(), story.toString());
    }
  }
}
