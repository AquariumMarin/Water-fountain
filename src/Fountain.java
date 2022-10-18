//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Fountain.java
///////////////////////////////////////////////////////////////////////////////

import java.util.Random;
import processing.core.PImage;
import java.io.File;

/**
 * This class develops a graphical implementation of a particle system of fountain. This class
 * contains the methods for creating and initializing the different data field and graphical
 * settings, for creating droplets, for drawing fountain and droplets and updating its content, for
 * moving the entire fountain around which new droplets are created, for calling
 * Utility.save("screenshot.png") when the key pressed is the s-key, for moving the fountain
 * position and to match the position of the mouse, and for testing testUpdateDroplet() method and
 * RemoveOldDroplets() method.
 * 
 * @author Marin Suzuki
 */
public class Fountain {
  private static Random randGen;
  private static PImage fountainImage;
  private static int positionX;
  private static int positionY;
  private static Droplet[] droplets;
  private static int startColor;
  private static int endColor;

  /**
   * Main method
   * 
   * @param args input arguments if any
   */
  public static void main(String[] args) {
    Utility.runApplication(); // starts the application
  }

  /**
   * This method creates and initializes the different data fields defined, and configures the
   * different graphical settings of this application, such as loading the background image, setting
   * the dimensions of the display window, and creating Droplet array.
   *
   */
  public static void setup() {

    testUpdateDroplet(); // test1
    testRemoveOldDroplets(); // test2

    randGen = new Random();
    positionX = Utility.width() / 2;
    positionY = Utility.height() / 2;

    // load the image of the fountain
    fountainImage = Utility.loadImage("images" + File.separator + "fountain.png");

    // create array with length of 800
    droplets = new Droplet[800];

    startColor = Utility.color(23, 141, 235); // blue
    endColor = Utility.color(23, 200, 255); // lighter blue

  }

  /**
   * This method continuously executes the lines of code contained inside its block until the
   * program is stopped. It continuously draws the application display window and updates its
   * content with respect to any change or any event which affects its appearance.
   *
   */
  public static void draw() {
    Utility.background(Utility.color(253, 245, 230));

    // Draw the fountain image to the screen at position (positionX, positionY)
    Utility.image(fountainImage, positionX, positionY);

    // call createNewDroplets() to attempt to create 10 new droplets
    createNewDroplets(10);

    for (int index = 0; index < droplets.length; index++) {
      if (droplets[index] != null) {
        updateDroplet(index);
        droplets[index].setAge(droplets[index].getAge() + 1);
      }
    }

    removeOldDroplets(80);

  }

  /**
   * This method should do all of the moving (by setting positions x and y), accelerating (updating
   * the y-velocity by adding 0.3f to it), and drawing of a droplet (by calling Utility.fill() and
   * Utility.circle()) through the provided index.
   * 
   * @param the index of a specific droplet within the droplets array.
   * 
   */
  public static void updateDroplet(int index) {

    // moving (by setting positions x)
    droplets[index].setPositionX(droplets[index].getPositionX() + droplets[index].getVelocityX());

    // accelerating (updating the y-velocity by adding 0.3f to it)
    droplets[index].setVelocityY(droplets[index].getVelocityY() + 0.3f);

    // moving (by setting positions y)
    droplets[index].setPositionY(droplets[index].getPositionY() + droplets[index].getVelocityY());

    // drawing of a droplet
    Utility.fill(droplets[index].getColor(), droplets[index].getTransparency());
    Utility.circle(droplets[index].getPositionX(), droplets[index].getPositionY(),
        droplets[index].getSize());

  }

  /**
   * This method creates new droplets,looking for null references within this array that can be
   * changed to reference newly created droplets. This loop will continue to do this until either:
   * 1) it has stepped through all valid indexes and there are no more to check, or 2) it has
   * created the specified number of new droplets and has stored references to them within the
   * droplets array.
   * 
   * @param the number of new droplets to create.
   * 
   */
  private static void createNewDroplets(int numNewDroplets) {

    // looking for null references within this array that can be changed to reference newly created
    int numCreatedDroplets = 0;
    int j = 0;

    while (j < droplets.length && numCreatedDroplets < numNewDroplets) {
      if (droplets[j] == null) {

        droplets[j] = new Droplet();

        // The x position of each droplet should begin within 3 pixels (+/-) of
        // Fountain.positionX.
        float dropletsPositionX = Fountain.positionX + randGen.nextFloat() * 6 - 3;

        // The y position of each droplet should begin within 3 pixels (+/-) of
        // Fountain.positionY.
        float dropletsPositionY = Fountain.positionY + randGen.nextFloat() * 6 - 3;

        // The size of each droplet should begin between 4 and 11(exclusive).
        float dropletsSize = randGen.nextFloat() * 7 + 4;

        // The color of each droplet should begin between Fountain.startColor and
        // Fountain.endColor
        int dropletsColor =
            Utility.lerpColor(Fountain.startColor, Fountain.endColor, randGen.nextFloat());

        // The x velocity of each droplet should begin between -1 and 1(exclusive).
        float dropletsVelocityX = randGen.nextFloat() * 2 - 1;

        // The y velocity of each droplet should begin between -5 and -10(exclusive).
        float dropletsVelocityY = randGen.nextFloat() * (-5) - 5;

        // The age of each droplet should begin between 0 and 40(inclusive).
        int dropletsAge = randGen.nextInt(41);

        // The transparency of each droplet should begin between 32 and 127(inclusive).
        int dropletsTransparency = randGen.nextInt(127 - 32 + 1) + 32;

        // initialize status for each droplet
        droplets[j].setPositionX(dropletsPositionX);
        droplets[j].setPositionY(dropletsPositionY);
        droplets[j].setSize(dropletsSize);
        droplets[j].setColor(dropletsColor);
        droplets[j].setVelocityX(dropletsVelocityX);
        droplets[j].setVelocityY(dropletsVelocityY);
        droplets[j].setAge(dropletsAge);
        droplets[j].setTransparency(dropletsTransparency);

        numCreatedDroplets++;
      }
      j++;
    }
  }


  /**
   * This method searches through the droplets array for references to droplets with an age that is
   * greater than the specified max age and remove them.
   * 
   * @param max age with which droplets can be stayed in the array
   * 
   */
  private static void removeOldDroplets(int maxAge) {
    for (int i = 0; i < droplets.length; i++) {
      if (droplets[i] != null && droplets[i].getAge() > maxAge) {
        droplets[i] = null;
      }
    }
  }

  /**
   * This method moves the Fountain.positionX and Fountain.positionY to match the position of the
   * mouse whenever the mouse button is pressed.
   * 
   */
  public static void mousePressed() {
    Fountain.positionX = Utility.mouseX();
    Fountain.positionY = Utility.mouseY();
  }

  /**
   * This method calls Utility.save("screenshot.png") whenever the key pressed happened to be the
   * s-key (either ’s’ or ’S’)
   * 
   * @param single char argument representing the key that was pressed
   * 
   */
  public static void keyPressed(char key) {
    if (key == 's' || key == 'S') {
      Utility.save("screenshot.png");
    }
  }

  /**
   * This tester initializes the droplets array to hold at least three droplets. Creates a single
   * droplet at position (3,3) with velocity (-1,-2). Then checks whether calling updateDroplet() on
   * this droplet’s index correctly results in changing its position to (2.0, 1.3).
   *
   * @return true when no defect is found, and false otherwise
   */
  public static boolean testUpdateDroplet() {

    // first case: whether droplets at the first index is correctly changed as expected
    {
      droplets = new Droplet[3];
      droplets[0] = new Droplet();
      droplets[1] = new Droplet();
      droplets[2] = new Droplet();
      droplets[0].setPositionX(3f);
      droplets[0].setPositionY(3f);
      droplets[0].setVelocityX(-1f);
      droplets[0].setVelocityY(-2f);
      droplets[0].setTransparency(10); // set some transparency to implement this method
      updateDroplet(0);
      if (Math.abs(droplets[0].getPositionX() - 2.0f) > 0.001
          || Math.abs(droplets[0].getPositionY() - 1.3f) > 0.001) {
        System.out.println("The new position of droplets at the first index is not correct "
            + "as expected after calling updateDroplet() method");
        return false; // bug detected
      }
    }

    // second case: whether droplets at the last index is correctly changed as expected
    {
      droplets = new Droplet[3];
      droplets[0] = new Droplet();
      droplets[1] = new Droplet();
      droplets[2] = new Droplet();
      droplets[2].setPositionX(3f);
      droplets[2].setPositionY(3f);
      droplets[2].setVelocityX(-1f);
      droplets[2].setVelocityY(-2f);
      droplets[2].setTransparency(10); // set some transparency to implement this method
      updateDroplet(2);
      if (Math.abs(droplets[2].getPositionX() - 2.0f) > 0.001
          || Math.abs(droplets[2].getPositionY() - 1.3f) > 0.001) {
        System.out.println("The new position of droplets at the last index is not correct "
            + "as expected after calling updateDroplet() method");
        return false; // bug detected
      }
    }

    // third case: whether droplets at an arbitrary index is correctly changed as expected
    {
      droplets = new Droplet[3];
      droplets[0] = new Droplet();
      droplets[1] = new Droplet();
      droplets[2] = new Droplet();
      droplets[1].setPositionX(3f);
      droplets[1].setPositionY(3f);
      droplets[1].setVelocityX(-1f);
      droplets[1].setVelocityY(-2f);
      droplets[1].setTransparency(10); // set some transparency to implement this method
      updateDroplet(1);
      if (Math.abs(droplets[1].getPositionX() - 2.0f) > 0.001
          || Math.abs(droplets[1].getPositionY() - 1.3f) > 0.001) {
        System.out.println("The new position of droplets at an arbitrary index is not correct"
            + " as expected after calling updateDroplet() method");
        return false; // bug detected
      }
    }

    return true; // no bug detected
  }

  /**
   * This tester initializes the droplets array to hold at least three droplets. Calls
   * removeOldDroplets(6) on an array with three droplets (two of which have ages over six and
   * another that does not). Then checks whether the old droplets were removed and the young droplet
   * was left alone.
   *
   * @return true when no defect is found, and false otherwise
   */
  private static boolean testRemoveOldDroplets() {

    // first case: droplets at the first and the last index have greater age than max age
    {
      droplets = new Droplet[3];
      droplets[0] = new Droplet();
      droplets[1] = new Droplet();
      droplets[2] = new Droplet();
      droplets[0].setTransparency(10); // set some transparency to implement this method
      droplets[1].setTransparency(10);
      droplets[2].setTransparency(10);
      droplets[0].setVelocityX(-1); // set some velocity to implement this method
      droplets[0].setVelocityY(-2);
      droplets[1].setVelocityX(-1);
      droplets[1].setVelocityY(-2);
      droplets[2].setVelocityX(-1);
      droplets[2].setVelocityY(-2);
      droplets[0].setAge(8);
      droplets[1].setAge(0);
      droplets[2].setAge(100);
      removeOldDroplets(6);
      if (droplets[0] != null || droplets[1] == null || droplets[2] != null) {
        System.out.println("The new droplets array is not correct as expected after calling"
            + "removeOldDroplets() method.");
        return false; // bug detected
      }
    }

    // second case: droplets at the second and the last index have greater age than max age
    {
      droplets = new Droplet[3];
      droplets[0] = new Droplet();
      droplets[1] = new Droplet();
      droplets[2] = new Droplet();
      droplets[0].setTransparency(10);
      droplets[1].setTransparency(10);
      droplets[2].setTransparency(10);
      droplets[0].setVelocityX(-1);
      droplets[0].setVelocityY(-2);
      droplets[1].setVelocityX(-1);
      droplets[1].setVelocityY(-2);
      droplets[2].setVelocityX(-1);
      droplets[2].setVelocityY(-2);
      droplets[0].setAge(6);
      droplets[1].setAge(7);
      droplets[2].setAge(100);
      removeOldDroplets(6);
      if (droplets[0] == null || droplets[1] != null || droplets[2] != null) {
        System.out.println("The new droplets array is not correct as expected after calling"
            + "removeOldDroplets() method.");
        return false; // bug detected
      }
    }

    return true; // no bug detected
  }

}
